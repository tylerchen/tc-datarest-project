/*******************************************************************************
 * Copyright (c) Sep 24, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest.core;

import org.iff.datarest.core.service.ManagementControllerFactory;
import org.iff.datarest.core.service.QueryControllerFactory;
import org.iff.netty.server.ProcessContext;
import org.iff.netty.server.handlers.RestHandler;
import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Assert;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 24, 2016
 */
public class DefaultRestHandler implements RestHandler {

    public static final String URI_SPLIT = "uriSplit";
    public static final String uriPrefix = "/default";

    private static final Map<String, RestHandler> services = new HashMap<String, RestHandler>();

    public static void addService(String path, RestHandler service) {
        Assert.notBlank(path);
        Assert.notNull(service);
        services.put(path, service);
    }

    public static void removeService(String path) {
        Assert.notBlank(path);
        services.remove(path);
    }

    @Override
    public boolean process(ProcessContext ctx) {
        String[] split = StringUtils.split(StringUtils.substringBefore(ctx.getUri(), "?"), '/');
        ctx.addAttribute(URI_SPLIT, split);
        RestHandler restHandler = null;
        if (split == null || split.length < 2 || (restHandler = services.get("/" + split[1])) == null) {
            ctx.getOutputBuffer().writeCharSequence("[404]: no handler found!", Charset.forName("UTF-8"));
            ctx.outputText();
        } else {
            try {
                restHandler.create().process(ctx);
                DBConnectionHoler.commitAndCloseQuietly();
            } catch (Exception e) {
                DBConnectionHoler.rollbackAndCloseQuietly();
                throw e;
            }
        }
        return true;
    }

    @Override
    public boolean matchUri(String uri) {
        return uriPrefix.equals(uri) || (uri.startsWith(uriPrefix)
                && (uri.charAt(uriPrefix.length()) == '/' || uri.charAt(uriPrefix.length()) == '?'));
    }

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public RestHandler create() {
        if (services.isEmpty()) {
            ManagementControllerFactory.addAllService();
            QueryControllerFactory.addAllService();
        }
        return this;
    }

}
