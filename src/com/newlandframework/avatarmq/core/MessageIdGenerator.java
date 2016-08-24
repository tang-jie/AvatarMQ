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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * @filename:MessageIdGenerator.java
 * @description:MessageIdGenerator功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class MessageIdGenerator {

    public static final int StrategyUUID = 0;
    public static final int StrategyRandomDigital = 1;

    private int strategy = StrategyRandomDigital;
    private final SecureRandom secureRandom = new SecureRandom();

    public MessageIdGenerator() {

    }

    public MessageIdGenerator(int strategy) {
        this.strategy = strategy;
    }

    public String generate() {
        String id = "";
        switch (strategy) {
            case StrategyUUID:
                id = UUID.randomUUID().toString();
                break;
            case StrategyRandomDigital:
                id = new BigInteger(130, secureRandom).toString(10);
                break;
        }
        return id;
    }
}
