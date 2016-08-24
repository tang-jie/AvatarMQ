/**
 * Copyright (C) 2016 Newland Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.avatarmq.core;

import com.newlandframework.avatarmq.model.MessageDispatchTask;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @filename:SendMessageCache.java
 * @description:SendMessageCache功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class SendMessageCache extends MessageCache<MessageDispatchTask> {

    private Phaser phaser = new Phaser(0);

    private SendMessageCache() {

    }

    private static SendMessageCache cache;

    public synchronized static SendMessageCache getInstance() {
        if (cache == null) {
            cache = new SendMessageCache();
        }
        return cache;
    }

    public void parallelDispatch(LinkedList<MessageDispatchTask> list) {
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
        int startPosition = 0;
        Pair<Integer, Integer> pair = calculateBlocks(list.size(), list.size());
        int numberOfThreads = pair.getRight();
        int blocks = pair.getLeft();

        for (int i = 0; i < numberOfThreads; i++) {
            MessageDispatchTask[] task = new MessageDispatchTask[blocks];
            phaser.register();
            System.arraycopy(list.toArray(), startPosition, task, 0, blocks);
            tasks.add(new SendMessageTask(phaser, task));
            startPosition += blocks;
        }

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        for (Callable<Void> element : tasks) {
            executor.submit(element);
        }
    }
}
