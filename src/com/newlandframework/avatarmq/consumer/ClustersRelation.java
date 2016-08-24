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

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @filename:ClustersRelation.java
 * @description:ClustersRelation功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class ClustersRelation {

    private String id;
    private ConsumerClusters clusters;

    ClustersRelation() {

    }

    ClustersRelation(String id, ConsumerClusters clusters) {
        this.clusters = clusters;
        this.id = id;
    }

    public ConsumerClusters getClusters() {
        return clusters;
    }

    public void setClusters(ConsumerClusters clusters) {
        this.clusters = clusters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && ClustersRelation.class.isAssignableFrom(obj.getClass())) {
            ClustersRelation clusters = (ClustersRelation) obj;
            result = new EqualsBuilder().append(id, clusters.getId())
                    .isEquals();
        }
        return result;
    }
}
