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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.newlandframework.avatarmq.netty.NettyClustersConfig;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @filename:SemaphoreCache.java
 * @description:SemaphoreCache功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class SemaphoreCache {

    private final static int hookTime = MessageSystemConfig.SemaphoreCacheHookTimeValue;

    private static final LoadingCache<String, Semaphore> cache = CacheBuilder.newBuilder().
            concurrencyLevel(NettyClustersConfig.getWorkerThreads()).
            build(new CacheLoader<String, Semaphore>() {
                public Semaphore load(String input) throws Exception {
                    return new Semaphore(0);
                }
            });

    public static int getAvailablePermits(String key) {
        try {
            return cache.get(key).availablePermits();
        } catch (ExecutionException ex) {
            Logger.getLogger(SemaphoreCache.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public static void release(String key) {
        try {
            cache.get(key).release();
            TimeUnit.MILLISECONDS.sleep(hookTime);
        } catch (ExecutionException ex) {
            Logger.getLogger(SemaphoreCache.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SemaphoreCache.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void acquire(String key) {
        try {
            cache.get(key).acquire();
            TimeUnit.MILLISECONDS.sleep(hookTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(SemaphoreCache.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(SemaphoreCache.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
