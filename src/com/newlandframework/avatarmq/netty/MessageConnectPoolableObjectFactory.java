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

import org.apache.commons.pool.PoolableObjectFactory;

/**
 * @filename:MessageConnectPoolableObjectFactory.java
 * @description:MessageConnectPoolableObjectFactory功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class MessageConnectPoolableObjectFactory implements PoolableObjectFactory<MessageConnectFactory> {

    private String serverAddress;
    private int sessionTimeOut = 3 * 1000;

    public MessageConnectPoolableObjectFactory(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public MessageConnectPoolableObjectFactory(String serverAddress, int sessionTimeOut) {
        this.serverAddress = serverAddress;
        this.sessionTimeOut = sessionTimeOut;
    }

    public MessageConnectFactory makeObject() throws Exception {
        MessageConnectFactory factory = new MessageConnectFactory(serverAddress);
        return factory;
    }

    public void destroyObject(MessageConnectFactory obj) throws Exception {
        if (obj instanceof MessageConnectFactory) {
            ((MessageConnectFactory) obj).close();
        }
    }

    public boolean validateObject(MessageConnectFactory obj) {
        return true;
    }

    public void activateObject(MessageConnectFactory obj) throws Exception {

    }

    public void passivateObject(MessageConnectFactory obj) throws Exception {
        MessageConnectFactory factory = (MessageConnectFactory) obj;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }
}
