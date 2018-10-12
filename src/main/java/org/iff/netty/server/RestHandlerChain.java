/*******************************************************************************
 * Copyright (c) Sep 24, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.netty.server;

import org.iff.netty.server.handlers.RestHandler;
import org.iff.infra.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * a handler chain to contains all the handler.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 24, 2016
 */
public class RestHandlerChain implements Iterator<RestHandler>, Iterable<RestHandler> {

    protected List<ProxyRestHandler> restHandlers = new ArrayList<ProxyRestHandler>();
    private int pos = -1;

    public static RestHandlerChain create() {
        return new RestHandlerChain();
    }

    public RestHandlerChain register(RestHandler restHandler) {
        Assert.notNull(restHandler);
        if (!restHandlers.contains(restHandler)) {
            restHandlers.add(new ProxyRestHandler(restHandler));
            Collections.sort(restHandlers);
        }
        return this;
    }

    public RestHandlerChain UnRegister(RestHandler restHandler) {
        Assert.notNull(restHandler);
        if (restHandlers.contains(restHandler)) {
            int indexOf = restHandlers.indexOf(restHandler);
            restHandlers.remove(indexOf);
        }
        return this;
    }

    public List<RestHandler> getRestHandlers() {
        ArrayList<RestHandler> list = new ArrayList<RestHandler>();
        for (ProxyRestHandler proxyRestHandler : restHandlers) {
            list.add(proxyRestHandler.getTarget());
        }
        return list;
    }

    @Override
    public Iterator<RestHandler> iterator() {
        pos = -1;
        return this;
    }

    @Override
    public boolean hasNext() {
        return restHandlers.size() > 0 && restHandlers.size() - 1 > pos;
    }

    @Override
    public RestHandler next() {
        pos = pos + 1;
        return restHandlers.get(pos);
    }

    @Override
    public void remove() {
        UnRegister(restHandlers.get(pos));
    }

    public class ProxyRestHandler implements RestHandler, Comparable<RestHandler> {
        RestHandler target;

        public ProxyRestHandler(RestHandler target) {
            super();
            Assert.notNull(target);
            this.target = target;
        }

        public RestHandler getTarget() {
            return this.target;
        }

        @Override
        public int compareTo(RestHandler other) {
            if (other == null) {
                return -1;
            }
            if (getOrder() == other.getOrder()) {
                return 0;
            }
            return getOrder() > other.getOrder() ? 1 : -1;
        }

        @Override
        public boolean process(ProcessContext ctx) {
            return target.process(ctx);
        }

        @Override
        public boolean matchUri(String uri) {
            return target.matchUri(uri);
        }

        @Override
        public int getOrder() {
            return target.getOrder();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result;
            result = prime * result + ((target == null) ? 0 : target.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj || this.target == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                return this.target == ((ProxyRestHandler) obj).target;
            }
            return true;
        }

        @Override
        public String toString() {
            return target.toString();
        }

        @Override
        public RestHandler create() {
            return target.create();
        }
    }
}
