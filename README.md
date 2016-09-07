# AvatarMQ
![](AvatarMQ-logo.png)
## AvatarMQ中文简介：
**AvatarMQ是基于Netty构建的分布式消息队列系统，支持多个生产者和多个消费者之间的消息路由、传递。**
* AvatarMQ基于Java语言进行编写，网络通讯依赖Netty。
* 若干个消费者可以组成一个消费者集群，生产者可以向这个消费者集群投递消息。并且每个消费者只消费自己关注的生产者发送的消息。
* 支持动态新增、删除生产者、消费者。
* 消息投递支持负载均衡策略。
* 消息处理转发服务器（AvatarMQServerStartup）基于异步多线程消息队列进行组织架构，并且和Spring无缝集成。
* 生产者、消费者测试用例参考：com.newlandframework.avatarmq.test包。
* 启动AvatarMQ的消息处理转发服务器（AvatarMQServerStartup）之后，才能进行分布式消息队列系统的消息路由、传递。
* 基于Netty的主从事件线程池模型，网络传输中的消息序列化采用Kryo序列化框架，进一步提升消息序列化性能。
* Broker消息的投递，目前支持严格的消息顺序。其中Broker还支持消息的缓冲派发，即Broker会缓存一定数量的消息之后，再批量分配给对此消息感兴趣的消费者。

----------

## AvatarMQ English Introduction:
**AvatarMQ is a distributed message queue system based on Netty(java nio framework), which supports message routing and delivery between multiple producers and multiple consumers.**
* AvatarMQ based on the preparation of Java language, network communications rely on Netty.
* A number of consumers can form a consumer cluster, the producer can send messages to this consumer cluster. And each consumer only consumes the message that is sent by the producer.
* To support the dynamic add, delete the producers, consumers.
* Message delivery support load balancing strategy.
* Message processing and forwarding server (AvatarMQServerStartup) based on asynchronous multi thread message queue organization architecture, and seamless integration with Spring.
* Producer, consumer test case reference: com.newlandframework.avatarmq.test package.
* Start AvatarMQ message processing and forwarding server (AvatarMQServerStartup), in order to carry out the distributed message queue system message routing, delivery.
* Based on the Netty master slave event thread pool model, network transmission in the message serialization using Kryo serialization framework, to further enhance the performance of message serialization.
* Broker message delivery, currently supports strict message order. Broker also supports message buffer distribution, that is, Broker will cache a certain number of messages, and then batch distribution to the message of interest to consumers.

----------
## AvatarMQ In Action
**Netty构建分布式消息队列（AvatarMQ）设计指南之架构篇**

**Architecture of Netty to build a distributed message queue system (AvatarMQ) design guide**

http://www.cnblogs.com/jietang/p/5808735.html

**Netty构建分布式消息队列实现原理浅析**

**Analysis of the principle of distributed message queue implementation by Netty**

http://www.cnblogs.com/jietang/p/5847458.html

----------
## Usage at a glance
This simple example for AvatarMQ Producer:
~~~~~~~~~~java
package com.newlandframework.avatarmq.test;

import com.newlandframework.avatarmq.msg.Message;
import com.newlandframework.avatarmq.msg.ProducerAckMessage;
import com.newlandframework.avatarmq.producer.AvatarMQProducer;
import org.apache.commons.lang3.StringUtils;

public class AvatarMQProducer {

    public static void main(String[] args) throws InterruptedException {
        AvatarMQProducer producer = new AvatarMQProducer("127.0.0.1:18888", "AvatarMQ-Topic-1");
        producer.setClusterId("AvatarMQCluster");
        producer.init();
        producer.start();

        System.out.println(StringUtils.center("AvatarMQProducer 消息发送开始", 50, "*"));

        for (int i = 0; i < 1; i++) {
            Message message = new Message();
            String str = "Hello AvatarMQ From Producer1[" + i + "]";
            message.setBody(str.getBytes());
            ProducerAckMessage result = producer.delivery(message);
            if (result.getStatus() == (ProducerAckMessage.SUCCESS)) {
                System.out.printf("AvatarMQProducer 发送消息编号:%s\n", result.getMsgId());
            }

            Thread.sleep(100);
        }

        producer.shutdown();
        System.out.println(StringUtils.center("AvatarMQProducer 消息发送完毕", 50, "*"));
    }
}
~~~~~~~~~~
This simple example for AvatarMQ Consumer:
~~~~~~~~~~java
package com.newlandframework.avatarmq.test;

import com.newlandframework.avatarmq.consumer.AvatarMQConsumer;
import com.newlandframework.avatarmq.consumer.ProducerMessageHook;
import com.newlandframework.avatarmq.msg.ConsumerAckMessage;
import com.newlandframework.avatarmq.msg.Message;

public class AvatarMQConsumer {

    private static ProducerMessageHook hook = new ProducerMessageHook() {
        public ConsumerAckMessage hookMessage(Message message) {
            System.out.printf("AvatarMQConsumer 收到消息编号:%s,消息内容:%s\n", message.getMsgId(), new String(message.getBody()));
            ConsumerAckMessage result = new ConsumerAckMessage();
            result.setStatus(ConsumerAckMessage.SUCCESS);
            return result;
        }
    };

    public static void main(String[] args) {
        AvatarMQConsumer consumer = new AvatarMQConsumer("127.0.0.1:18888", "AvatarMQ-Topic-1", hook);
        consumer.init();
        consumer.setClusterId("AvatarMQCluster");
        consumer.receiveMode();
        consumer.start();
    }
}
~~~~~~~~~~
----------

## Author
唐洁（tangjie） http://www.cnblogs.com/jietang/

----------

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) 2016 Newland Group Holding Limited
