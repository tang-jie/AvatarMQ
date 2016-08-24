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
package com.newlandframework.avatarmq.producer;

import com.newlandframework.avatarmq.core.CallBackInvoker;
import com.newlandframework.avatarmq.core.HookMessageEvent;
import com.newlandframework.avatarmq.model.ResponseMessage;
import com.newlandframework.avatarmq.netty.MessageEventWrapper;
import com.newlandframework.avatarmq.netty.MessageProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * @filename:MessageProducerHandler.java
 * @description:MessageProducerHandler功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class MessageProducerHandler extends MessageEventWrapper<String> {

    private String key;

    public MessageProducerHandler(MessageProcessor processor) {
        this(processor, null);
        super.setWrapper(this);
    }

    public MessageProducerHandler(MessageProcessor processor, HookMessageEvent hook) {
        super(processor, hook);
        super.setWrapper(this);
    }

    public void beforeMessage(Object msg) {
        key = ((ResponseMessage) msg).getMsgId();
    }

    public void handleMessage(ChannelHandlerContext ctx, Object msg) {
        if (!factory.traceInvoker(key)) {
            return;
        }

        CallBackInvoker<Object> invoker = factory.detachInvoker(key);

        if (invoker == null) {
            return;
        }

        if (this.getCause() != null) {
            invoker.setReason(getCause());
        } else {
            invoker.setMessageResult(msg);
        }
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (hook != null) {
            hook.disconnect(ctx.channel().remoteAddress().toString());
        }
        super.channelInactive(ctx);
    }
}
