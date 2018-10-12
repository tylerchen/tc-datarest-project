/*******************************************************************************
 * Copyright (c) Sep 25, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.netty.server;

import org.iff.netty.server.handlers.ErrorRestHandler;
import org.iff.netty.server.handlers.NoRestHandler;
import org.iff.netty.server.handlers.RestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ClientCookieDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Assert;

import java.io.PrintWriter;
import java.util.Iterator;

/**
 * http server in bound handler.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 25, 2016
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    protected HttpServer server;
    protected NoRestHandler noRestHandler = new NoRestHandler();
    protected ErrorRestHandler errorRestHandler = new ErrorRestHandler();

    public HttpServerInboundHandler(HttpServer server) {
        super();
        Assert.notNull(server);
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof HttpRequest)) {
            return;
        }
        HttpRequest request = (HttpRequest) msg;
        String uri = request.uri();
        Iterator<RestHandler> iterator = server.getChain().iterator();
        ProcessContext processContext = ProcessContext.create(server.getConfig(), ctx, request, msg,
                this.server.getContext());
        boolean hasProcess = false;
        uri = StringUtils.removeStart(uri, this.server.getContext());
        uri = uri.length() > 0 && uri.charAt(0) != '/' ? ("/" + uri) : uri;
        try {
            while (iterator.hasNext()) {
                RestHandler restHandler = iterator.next();
                if (restHandler.matchUri(uri)) {
                    hasProcess = hasProcess || restHandler.create().process(processContext);
                }
            }
            if (!hasProcess) {
                noRestHandler.create().process(processContext);
            }
            if (HttpUtil.is100ContinueExpected(request)) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }
        } catch (Throwable e) {
            try {
                StringBuilderWriter writer = new StringBuilderWriter(1024);
                e.printStackTrace(new PrintWriter(writer));
                processContext.addAttribute("error", writer.toString());
                errorRestHandler.create().process(processContext);
            } catch (Exception nothrow) {
            }
        } finally {
            if (!processContext.isHasInvokeOutput()) {
                try {
                    processContext.outputText();
                } catch (Exception e) {
                }
            }
            ReferenceCountUtil.release(msg);
        }
    }

    protected Cookie getCookie(HttpRequest request) {
        String value = request.headers().get(HttpHeaderNames.COOKIE);
        if (value != null) {
            return ClientCookieDecoder.LAX.decode(value);
        }
        return null;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}