/*******************************************************************************
 * Copyright (c) Sep 25, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.netty.server;

import org.iff.netty.server.handlers.RestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.iff.infra.util.Assert;

import java.util.List;
import java.util.Properties;

/**
 * http server.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 25, 2016
 */
public class HttpServer implements Runnable {

    private SslContext sslCtx;
    private String ip;
    private int port;
    private String context;
    private Properties config;
    private RestHandlerChain chain = RestHandlerChain.create();

    public HttpServer(Properties config, int port, List<RestHandler> list, String context) {
        this(config, "0.0.0.0", port, list, context);
    }

    public HttpServer(Properties config, String ip, int port, List<RestHandler> list, String context) {
        Assert.notBlank(ip);
        Assert.notEmpty(list);
        Assert.isTrue(port > 0);
        Assert.notBlank(context);
        Assert.notNull(config);
        this.port = port;
        this.ip = ip;
        this.context = context.trim();
        this.config = config;
        setRestHandler(list);
        try {/*Configure SSL context*/
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            this.sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void setRestHandler(List<RestHandler> list) {
        for (RestHandler restHandler : list) {
            chain.register(restHandler);
        }
    }

    public RestHandlerChain getChain() {
        return chain;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
            serverBootstrap.childHandler(new HttpServerInitializer(this, sslCtx, false));
            //serverBootstrap.childHandler(new PortUnificationServerHandler(this, sslCtx));

            serverBootstrap/**/
                    .option(ChannelOption.SO_BACKLOG, 1024)/**/
                    .option(ChannelOption.TCP_NODELAY, true)/**/
                    .option(ChannelOption.SO_KEEPALIVE, true) /**/
                    .option(ChannelOption.SO_REUSEADDR, true) /**/
                    .option(ChannelOption.SO_RCVBUF, 10 * 1024) /**/
                    .option(ChannelOption.SO_SNDBUF, 10 * 1024) /**/
                    .option(EpollChannelOption.SO_REUSEPORT, true) /**/
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            Channel channel;
            channel = serverBootstrap.bind(ip, port).sync().channel();
            channel.closeFuture().sync();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getContext() {
        return context;
    }

    public Properties getConfig() {
        return config;
    }

}