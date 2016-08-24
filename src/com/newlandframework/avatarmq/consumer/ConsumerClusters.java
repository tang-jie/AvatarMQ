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
package com.newlandframework.avatarmq.consumer;

import com.newlandframework.avatarmq.model.RemoteChannelData;
import com.newlandframework.avatarmq.model.SubscriptionData;
import com.newlandframework.avatarmq.netty.NettyUtil;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;

/**
 * @filename:ConsumerClusters.java
 * @description:ConsumerClusters功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class ConsumerClusters {

    private int next = 0;
    private final String clustersId;
    private final ConcurrentHashMap<String, SubscriptionData> subMap
            = new ConcurrentHashMap<String, SubscriptionData>();

    private final ConcurrentHashMap<String, RemoteChannelData> channelMap
            = new ConcurrentHashMap<String, RemoteChannelData>();

    private final List<RemoteChannelData> channelList = Collections.synchronizedList(new ArrayList<RemoteChannelData>());

    public ConsumerClusters(String clustersId) {
        this.clustersId = clustersId;
    }

    public String getClustersId() {
        return clustersId;
    }

    public ConcurrentHashMap<String, SubscriptionData> getSubMap() {
        return subMap;
    }

    public ConcurrentHashMap<String, RemoteChannelData> getChannelMap() {
        return channelMap;
    }

    public void attachRemoteChannelData(String clientId, RemoteChannelData channelinfo) {
        if (findRemoteChannelData(channelinfo.getClientId()) == null) {
            channelMap.put(clientId, channelinfo);
            subMap.put(channelinfo.getSubcript().getTopic(), channelinfo.getSubcript());
            channelList.add(channelinfo);
        } else {
            System.out.println("consumer clusters exists! it's clientId:" + clientId);
        }
    }

    public void detachRemoteChannelData(String clientId) {
        channelMap.remove(clientId);

        Predicate predicate = new Predicate() {
            public boolean evaluate(Object object) {
                String id = ((RemoteChannelData) object).getClientId();
                return id.compareTo(clientId) == 0;
            }
        };

        RemoteChannelData data = (RemoteChannelData) CollectionUtils.find(channelList, predicate);
        if (data != null) {
            channelList.remove(data);
        }
    }

    public RemoteChannelData findRemoteChannelData(String clientId) {
        return (RemoteChannelData) MapUtils.getObject(channelMap, clientId);
    }

    public RemoteChannelData nextRemoteChannelData() {

        Predicate predicate = new Predicate() {
            public boolean evaluate(Object object) {
                RemoteChannelData data = (RemoteChannelData) object;
                Channel channel = data.getChannel();
                return NettyUtil.validateChannel(channel);
            }
        };

        CollectionUtils.filter(channelList, predicate);
        return channelList.get(next++ % channelList.size());
    }

    public SubscriptionData findSubscriptionData(String topic) {
        return this.subMap.get(topic);
    }
}
