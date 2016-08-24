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
package com.newlandframework.avatarmq.netty;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @filename:MessageEventAdvisor.java
 * @description:MessageEventAdvisor功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class MessageEventAdvisor implements MethodInterceptor {

    private MessageEventProxy proxy;
    private Object msg;

    public MessageEventAdvisor(MessageEventProxy proxy, Object msg) {
        this.proxy = proxy;
        this.msg = msg;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        proxy.beforeMessage(msg);
        Object obj = invocation.proceed();
        proxy.afterMessage(msg);
        return obj;
    }
}
