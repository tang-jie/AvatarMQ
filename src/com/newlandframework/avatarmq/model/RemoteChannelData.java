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
package com.newlandframework.avatarmq.model;

import io.netty.channel.Channel;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @filename:RemoteChannelData.java
 * @description:RemoteChannelData功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class RemoteChannelData {

    private Channel channel;
    private String clientId;

    private SubscriptionData subcript;

    public SubscriptionData getSubcript() {
        return subcript;
    }

    public void setSubcript(SubscriptionData subcript) {
        this.subcript = subcript;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getClientId() {
        return clientId;
    }

    public RemoteChannelData(Channel channel, String clientId) {
        this.channel = channel;
        this.clientId = clientId;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && RemoteChannelData.class.isAssignableFrom(obj.getClass())) {
            RemoteChannelData info = (RemoteChannelData) obj;
            result = new EqualsBuilder().append(clientId, info.getClientId())
                    .isEquals();
        }
        return result;
    }

}
