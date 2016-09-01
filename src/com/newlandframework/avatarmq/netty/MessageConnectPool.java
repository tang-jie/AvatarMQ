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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * @filename:MessageConnectPool.java
 * @description:MessageConnectPool功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class MessageConnectPool extends GenericObjectPool<MessageConnectFactory> {

    private static MessageConnectPool pool = null;
    private static Properties messageConnectConfigProperties = null;
    private static String configPropertiesString = "com/newlandframework/avatarmq/netty/avatarmq.messageconnect.properties";
    private static String serverAddress = "";

    public static MessageConnectPool getMessageConnectPoolInstance() {
        if (pool == null) {
            synchronized (MessageConnectPool.class) {
                if (pool == null) {
                    pool = new MessageConnectPool();
                }
            }
        }
        return pool;
    }

    private MessageConnectPool() {
        try {
            messageConnectConfigProperties = new Properties();

            InputStream inputStream = MessageConnectPool.class.getClassLoader()
                    .getResourceAsStream(configPropertiesString);

            messageConnectConfigProperties.load(inputStream);
            inputStream.close();

            this.serverAddress = serverAddress;
        } catch (IOException e) {
            e.printStackTrace();
        }

        int maxActive = Integer.parseInt(messageConnectConfigProperties.getProperty("maxActive"));
        int minIdle = Integer.parseInt(messageConnectConfigProperties.getProperty("minIdle"));
        int maxIdle = Integer.parseInt(messageConnectConfigProperties.getProperty("maxIdle"));
        int maxWait = Integer.parseInt(messageConnectConfigProperties.getProperty("maxWait"));
        int sessionTimeOut = Integer.parseInt(messageConnectConfigProperties.getProperty("sessionTimeOut"));

        System.out.printf("MessageConnectPool[maxActive=%d,minIdle=%d,maxIdle=%d,maxWait=%d,sessionTimeOut=%d]\n", maxActive, minIdle, maxIdle, maxWait, sessionTimeOut);

        this.setMaxActive(maxActive);
        this.setMaxIdle(maxIdle);
        this.setMinIdle(minIdle);
        this.setMaxWait(maxWait);
        this.setTestOnBorrow(false);
        this.setTestOnReturn(false);
        this.setTimeBetweenEvictionRunsMillis(10 * 1000);
        this.setNumTestsPerEvictionRun(maxActive + maxIdle);
        this.setMinEvictableIdleTimeMillis(30 * 60 * 1000);
        this.setTestWhileIdle(true);

        this.setFactory(new MessageConnectPoolableObjectFactory(serverAddress, sessionTimeOut));
    }

    public MessageConnectFactory borrow() {
        assert pool != null;
        try {
            return (MessageConnectFactory) pool.borrowObject();
        } catch (Exception e) {
            System.out.printf("get message connection throw the error from message connection pool, error message is %s\n",
                    e.getMessage());
        }
        return null;
    }

    public void restore() {
        assert pool != null;
        try {
            pool.close();
        } catch (Exception e) {
            System.out.printf("throw the error from close message connection pool, error message is %s\n",
                    e.getMessage());
        }
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public static void setServerAddress(String ipAddress) {
        serverAddress = ipAddress;
    }
}
