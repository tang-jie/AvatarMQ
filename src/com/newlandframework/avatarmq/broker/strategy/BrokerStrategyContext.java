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
package com.newlandframework.avatarmq.broker.strategy;

import com.newlandframework.avatarmq.broker.ConsumerMessageListener;
import com.newlandframework.avatarmq.broker.ProducerMessageListener;
import com.newlandframework.avatarmq.model.RequestMessage;
import com.newlandframework.avatarmq.model.ResponseMessage;
import com.newlandframework.avatarmq.model.MessageSource;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.TypedMap;

/**
 * @filename:BrokerStrategyContext.java
 * @description:BrokerStrategyContext功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class BrokerStrategyContext {

    public final static int AvatarMQProducerMessageStrategy = 1;
    public final static int AvatarMQConsumerMessageStrategy = 2;
    public final static int AvatarMQSubscribeStrategy = 3;
    public final static int AvatarMQUnsubscribeStrategy = 4;

    private RequestMessage request;
    private ResponseMessage response;
    private ChannelHandlerContext channelHandler;
    private ProducerMessageListener hookProducer;
    private ConsumerMessageListener hookConsumer;
    private BrokerStrategy strategy;

    private static Map strategyMap = TypedMap.decorate(new HashMap(), Integer.class, BrokerStrategy.class);

    static {
        strategyMap.put(AvatarMQProducerMessageStrategy, new BrokerProducerMessageStrategy());
        strategyMap.put(AvatarMQConsumerMessageStrategy, new BrokerConsumerMessageStrategy());
        strategyMap.put(AvatarMQSubscribeStrategy, new BrokerSubscribeStrategy());
        strategyMap.put(AvatarMQUnsubscribeStrategy, new BrokerUnsubscribeStrategy());
    }

    public BrokerStrategyContext(RequestMessage request, ResponseMessage response, ChannelHandlerContext channelHandler) {
        this.request = request;
        this.response = response;
        this.channelHandler = channelHandler;
    }

    public void setHookProducer(ProducerMessageListener hookProducer) {
        this.hookProducer = hookProducer;
    }

    public void setHookConsumer(ConsumerMessageListener hookConsumer) {
        this.hookConsumer = hookConsumer;
    }

    public void invoke() {
        switch (request.getMsgType()) {
            case AvatarMQMessage:
                strategy = (BrokerStrategy) strategyMap.get(request.getMsgSource() == MessageSource.AvatarMQProducer ? AvatarMQProducerMessageStrategy : AvatarMQConsumerMessageStrategy);
                break;
            case AvatarMQSubscribe:
                strategy = (BrokerStrategy) strategyMap.get(AvatarMQSubscribeStrategy);
                break;
            case AvatarMQUnsubscribe:
                strategy = (BrokerStrategy) strategyMap.get(AvatarMQUnsubscribeStrategy);
                break;
            default:
                break;
        }

        strategy.setChannelHandler(channelHandler);
        strategy.setHookConsumer(hookConsumer);
        strategy.setHookProducer(hookProducer);
        strategy.messageDispatch(request, response);
    }
}
