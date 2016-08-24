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
 * @filename:ClustersState.java
 * @description:ClustersState功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class ClustersState {

    public static final int ERROR = 1;
    public static final int SUCCESS = 0;
    public static final int NETWORKERR = -1;
    private String clusters;
    private int state;

    ClustersState() {

    }

    ClustersState(String clusters, int state) {
        this.clusters = clusters;
        this.state = state;
    }

    public String getClusters() {
        return clusters;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && ClustersState.class.isAssignableFrom(obj.getClass())) {
            ClustersState clusters = (ClustersState) obj;
            result = new EqualsBuilder().append(clusters, clusters.getClusters())
                    .isEquals();
        }
        return result;
    }
}
