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
package com.newlandframework.avatarmq.broker;

import com.newlandframework.avatarmq.core.SemaphoreCache;
import com.newlandframework.avatarmq.core.MessageSystemConfig;
import com.newlandframework.avatarmq.core.MessageTaskQueue;
import com.newlandframework.avatarmq.core.SendMessageCache;
import com.newlandframework.avatarmq.model.MessageDispatchTask;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @filename:SendMessageController.java
 * @description:SendMessageController功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class SendMessageController implements Callable<Void> {

    private volatile boolean stoped = false;

    private AtomicBoolean flushTask = new AtomicBoolean(false);

    private ThreadLocal<ConcurrentLinkedQueue<MessageDispatchTask>> requestCacheList = new ThreadLocal<ConcurrentLinkedQueue<MessageDispatchTask>>() {
        protected ConcurrentLinkedQueue<MessageDispatchTask> initialValue() {
            return new ConcurrentLinkedQueue<MessageDispatchTask>();
        }
    };

    private final Timer timer = new Timer("SendMessageTaskMonitor", true);

    public void stop() {
        stoped = true;
    }

    public boolean isStoped() {
        return stoped;
    }

    public Void call() {
        int period = MessageSystemConfig.SendMessageControllerPeriodTimeValue;
        int commitNumber = MessageSystemConfig.SendMessageControllerTaskCommitValue;
        int sleepTime = MessageSystemConfig.SendMessageControllerTaskSleepTimeValue;

        ConcurrentLinkedQueue<MessageDispatchTask> queue = requestCacheList.get();
        SendMessageCache ref = SendMessageCache.getInstance();

        while (!stoped) {
            SemaphoreCache.acquire(MessageSystemConfig.NotifyTaskSemaphoreValue);
            MessageDispatchTask task = MessageTaskQueue.getInstance().getTask();

            queue.add(task);

            if (queue.size() == 0) {
                try {
                    Thread.sleep(sleepTime);
                    continue;
                } catch (InterruptedException ex) {
                    Logger.getLogger(SendMessageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (queue.size() > 0 && (queue.size() % commitNumber == 0 || flushTask.get() == true)) {
                ref.commit(queue);
                queue.clear();
                flushTask.compareAndSet(true, false);
            }

            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    try {
                        flushTask.compareAndSet(false, true);
                    } catch (Exception e) {
                        System.out.println("SendMessageTaskMonitor happen exception");
                    }
                }
            }, 1000 * 1, period);
        }
        return null;
    }
}
