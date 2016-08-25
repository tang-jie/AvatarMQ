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

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.newlandframework.avatarmq.core.CallBackInvoker;
import com.newlandframework.avatarmq.core.MessageSystemConfig;
import com.newlandframework.avatarmq.serialize.KryoCodecUtil;
import com.newlandframework.avatarmq.serialize.KryoPoolFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;

/**
 * @filename:MessageConnectFactory.java
 * @description:MessageConnectFactory功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class MessageConnectFactory {

    private SocketAddress remoteAddr = null;
    private ChannelInboundHandlerAdapter messageHandler = null;
    private Map<String, CallBackInvoker<Object>> callBackMap = new ConcurrentHashMap<String, CallBackInvoker<Object>>();
    private Bootstrap bootstrap = null;
    private long timeout = 10 * 1000;
    private boolean connected = false;
    private EventLoopGroup eventLoopGroup = null;
    private static KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
    private Channel messageChannel = null;
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    private NettyClustersConfig nettyClustersConfig = new NettyClustersConfig();

    private ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("MessageConnectFactory-%d")
            .setDaemon(true)
            .build();

    public MessageConnectFactory(String serverAddress) {
        String[] ipAddr = serverAddress.split(MessageSystemConfig.IpV4AddressDelimiter);
        if (ipAddr.length == 2) {
            remoteAddr = NettyUtil.string2SocketAddress(serverAddress);
        }
    }

    public void setMessageHandle(ChannelInboundHandlerAdapter messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void init() {
        try {
            defaultEventExecutorGroup = new DefaultEventExecutorGroup(NettyClustersConfig.getWorkerThreads(), threadFactory);
            eventLoopGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(defaultEventExecutorGroup);
                            channel.pipeline().addLast(new MessageObjectEncoder(util));
                            channel.pipeline().addLast(new MessageObjectDecoder(util));
                            channel.pipeline().addLast(messageHandler);
                        }
                    })
                    .option(ChannelOption.SO_SNDBUF, nettyClustersConfig.getClientSocketSndBufSize())
                    .option(ChannelOption.SO_RCVBUF, nettyClustersConfig.getClientSocketRcvBufSize())
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void connect() {
        Preconditions.checkNotNull(messageHandler, "Message's Handler is Null!");

        try {
            init();
            ChannelFuture channelFuture = bootstrap.connect(this.remoteAddr).sync();

            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    Channel channel = future.channel();
                    messageChannel = channel;
                }
            });

            System.out.println("ip address:" + this.remoteAddr.toString());
            connected = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (messageChannel != null) {
            try {
                messageChannel.close().sync();
                eventLoopGroup.shutdownGracefully();
                defaultEventExecutorGroup.shutdownGracefully();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean traceInvoker(String key) {
        if (key == null) {
            return false;
        }
        return getCallBackMap().containsKey(key);
    }

    public CallBackInvoker<Object> detachInvoker(String key) {
        if (traceInvoker(key)) {
            return getCallBackMap().remove(key);
        } else {
            return null;
        }
    }

    public void setTimeOut(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeOut() {
        return this.timeout;
    }

    public Channel getMessageChannel() {
        return messageChannel;
    }

    public Map<String, CallBackInvoker<Object>> getCallBackMap() {
        return callBackMap;
    }

    public void setCallBackMap(Map<String, CallBackInvoker<Object>> callBackMap) {
        this.callBackMap = callBackMap;
    }
}
