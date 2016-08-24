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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @filename:CallBackInvoker.java
 * @description:CallBackInvoker功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class CallBackInvoker<T> {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private T messageResult;
    private List<CallBackListener<T>> listeners = Collections.synchronizedList(new ArrayList<CallBackListener<T>>());
    private String requestId;
    private Throwable reason;

    public void setReason(Throwable reason) {
        this.reason = reason;
        publish();
        countDownLatch.countDown();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public CallBackInvoker() {
    }

    public void setMessageResult(T messageResult) {
        this.messageResult = messageResult;
        publish();
        countDownLatch.countDown();
    }

    public Object getMessageResult(long timeout, TimeUnit unit) {
        try {
            countDownLatch.await(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        if (reason != null) {
            return null;
        }
        return messageResult;
    }

    public void join(CallBackListener<T> listener) {
        this.listeners.add(listener);
    }

    private void publish() {
        for (CallBackListener<T> listener : listeners) {
            listener.onCallBack(messageResult);
        }
    }
}
