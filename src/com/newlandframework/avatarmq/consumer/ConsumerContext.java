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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;

/**
 * @filename:ConsumerContext.java
 * @description:ConsumerContext功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class ConsumerContext {

    private static final CopyOnWriteArrayList<ClustersRelation> relationArray = new CopyOnWriteArrayList<ClustersRelation>();
    private static final CopyOnWriteArrayList<ClustersState> stateArray = new CopyOnWriteArrayList<ClustersState>();

    public static void setClustersStat(String clusters, int stat) {
        stateArray.add(new ClustersState(clusters, stat));
    }

    public static int getClustersStat(String clusters) {

        Predicate predicate = new Predicate() {
            public boolean evaluate(Object object) {
                String clustersId = ((ClustersState) object).getClusters();
                return clustersId.compareTo(clusters) == 0;
            }
        };

        Iterator iterator = new FilterIterator(stateArray.iterator(), predicate);

        ClustersState state = null;
        while (iterator.hasNext()) {
            state = (ClustersState) iterator.next();
            break;

        }
        return (state != null) ? state.getState() : 0;
    }

    public static ConsumerClusters selectByClusters(String clusters) {
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object object) {
                String id = ((ClustersRelation) object).getId();
                return id.compareTo(clusters) == 0;
            }
        };

        Iterator iterator = new FilterIterator(relationArray.iterator(), predicate);

        ClustersRelation relation = null;
        while (iterator.hasNext()) {
            relation = (ClustersRelation) iterator.next();
            break;
        }

        return (relation != null) ? relation.getClusters() : null;
    }

    public static List<ConsumerClusters> selectByTopic(String topic) {

        List<ConsumerClusters> clusters = new ArrayList<ConsumerClusters>();

        for (int i = 0; i < relationArray.size(); i++) {
            ConcurrentHashMap<String, SubscriptionData> subscriptionTable = relationArray.get(i).getClusters().getSubMap();
            if (subscriptionTable.containsKey(topic)) {
                clusters.add(relationArray.get(i).getClusters());
            }
        }

        return clusters;
    }

    public static void addClusters(String clusters, RemoteChannelData channelinfo) {
        ConsumerClusters manage = selectByClusters(clusters);
        if (manage == null) {
            ConsumerClusters newClusters = new ConsumerClusters(clusters);
            newClusters.attachRemoteChannelData(channelinfo.getClientId(), channelinfo);
            relationArray.add(new ClustersRelation(clusters, newClusters));
        } else if (manage.findRemoteChannelData(channelinfo.getClientId()) != null) {
            manage.detachRemoteChannelData(channelinfo.getClientId());
            manage.attachRemoteChannelData(channelinfo.getClientId(), channelinfo);
        } else {
            String topic = channelinfo.getSubcript().getTopic();
            boolean touchChannel = manage.getSubMap().containsKey(topic);
            if (touchChannel) {
                manage.attachRemoteChannelData(channelinfo.getClientId(), channelinfo);
            } else {
                manage.getSubMap().clear();
                manage.getChannelMap().clear();
                manage.attachRemoteChannelData(channelinfo.getClientId(), channelinfo);
            }
        }
    }

    public static void unLoad(String clientId) {

        for (int i = 0; i < relationArray.size(); i++) {
            String id = relationArray.get(i).getId();
            ConsumerClusters manage = relationArray.get(i).getClusters();

            if (manage.findRemoteChannelData(clientId) != null) {
                manage.detachRemoteChannelData(clientId);
            }

            if (manage.getChannelMap().size() == 0) {
                ClustersRelation relation = new ClustersRelation();
                relation.setId(id);
                relationArray.remove(id);
            }
        }
    }
}
