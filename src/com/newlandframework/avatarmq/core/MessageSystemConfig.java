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

/**
 * @filename:MessageSystemConfig.java
 * @description:MessageSystemConfig功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class MessageSystemConfig {

    public static final String SystemPropertySocketSndbufSize
            = "com.newlandframework.avatarmq.netty.socket.sndbuf.size";
    public static int SocketSndbufSize
            = Integer.parseInt(System.getProperty(SystemPropertySocketSndbufSize, "65535"));

    public static final String SystemPropertySocketRcvbufSize
            = "com.newlandframework.avatarmq.netty.socket.rcvbuf.size";
    public static int SocketRcvbufSize
            = Integer.parseInt(System.getProperty(SystemPropertySocketRcvbufSize, "65535"));

    public static final String SystemPropertyAckTaskSemaphoreValue
            = "com.newlandframework.avatarmq.semaphore.ackTaskSemaphoreValue";
    public static String AckTaskSemaphoreValue
            = System.getProperty(SystemPropertyAckTaskSemaphoreValue, "Ack");

    public static final String SystemPropertyNotifyTaskSemaphoreValue
            = "com.newlandframework.avatarmq.semaphore.NotifyTaskSemaphoreValue";
    public static String NotifyTaskSemaphoreValue
            = System.getProperty(SystemPropertyNotifyTaskSemaphoreValue, "Notify");

    public static final String SystemPropertySemaphoreCacheHookTimeValue
            = "com.newlandframework.avatarmq.semaphore.hooktime";
    public static int SemaphoreCacheHookTimeValue
            = Integer.parseInt(System.getProperty(SystemPropertySemaphoreCacheHookTimeValue, "5"));

    public static final String SystemPropertyMessageTimeOutValue
            = "com.newlandframework.avatarmq.system.normal.timeout";
    public static int MessageTimeOutValue
            = Integer.parseInt(System.getProperty(SystemPropertyMessageTimeOutValue, "3000"));

    public static final String SystemPropertyAckMessageControllerTimeOutValue
            = "com.newlandframework.avatarmq.system.ack.timeout";
    public static int AckMessageControllerTimeOutValue
            = Integer.parseInt(System.getProperty(SystemPropertyAckMessageControllerTimeOutValue, "1000"));

    public static final String SystemPropertySendMessageControllerPeriodTimeValue
            = "com.newlandframework.avatarmq.system.send.period";
    public static int SendMessageControllerPeriodTimeValue
            = Integer.parseInt(System.getProperty(SystemPropertySendMessageControllerPeriodTimeValue, "3000"));

    public static final String SystemPropertySendMessageControllerTaskCommitValue
            = "com.newlandframework.avatarmq.system.send.taskcommit";
    public static int SendMessageControllerTaskCommitValue
            = Integer.parseInt(System.getProperty(SystemPropertySendMessageControllerTaskCommitValue, "1"));

    public static final String SystemPropertySendMessageControllerTaskSleepTimeValue
            = "com.newlandframework.avatarmq.system.send.sleeptime";
    public static int SendMessageControllerTaskSleepTimeValue
            = Integer.parseInt(System.getProperty(SystemPropertySendMessageControllerTaskSleepTimeValue, "5000"));

    public final static String MessageDelimiter = "@";
    public final static String IpV4AddressDelimiter = ":";
}
