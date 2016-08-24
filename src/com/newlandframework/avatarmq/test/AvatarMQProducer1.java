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
package com.newlandframework.avatarmq.test;

import com.newlandframework.avatarmq.msg.Message;
import com.newlandframework.avatarmq.msg.ProducerAckMessage;
import com.newlandframework.avatarmq.producer.AvatarMQProducer;
import org.apache.commons.lang3.StringUtils;

/**
 * @filename:AvatarMQProducer1.java
 * @description:AvatarMQProducer1功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class AvatarMQProducer1 {

    public static void main(String[] args) throws InterruptedException {
        AvatarMQProducer producer = new AvatarMQProducer("127.0.0.1:18888", "AvatarMQ-Topic-1");
        producer.setClusterId("AvatarMQCluster");
        producer.init();
        producer.start();

        System.out.println(StringUtils.center("AvatarMQProducer1 消息发送开始", 50, "*"));

        for (int i = 0; i < 1; i++) {
            Message message = new Message();
            String str = "Hello AvatarMQ From Producer1[" + i + "]";
            message.setBody(str.getBytes());
            ProducerAckMessage result = producer.delivery(message);
            if (result.getStatus() == (ProducerAckMessage.SUCCESS)) {
                System.out.printf("AvatarMQProducer1 发送消息编号:%s\n", result.getMsgId());
            }

            Thread.sleep(100);
        }

        producer.shutdown();
        System.out.println(StringUtils.center("AvatarMQProducer1 消息发送完毕", 50, "*"));
    }
}
