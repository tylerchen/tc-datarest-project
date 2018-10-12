/*******************************************************************************
 * Copyright (c) Sep 30, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest.core.service;

import org.iff.netty.server.ProcessContext;
import org.iff.netty.server.handlers.RestHandler;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 30, 2016
 */
public abstract class BaseDefaultRestHandler implements RestHandler {

    @Override
    public abstract boolean process(ProcessContext ctx);

    @Override
    public RestHandler create() {
        return this;
    }

    @Override
    public boolean matchUri(String uri) {
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public static String urlDecode(String url) {
        if (url != null && url.length() > 0) {
            try {
                return URLDecoder.decode(url, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String urlEncode(String url) {
        if (url != null && url.length() > 0) {
            try {
                return URLEncoder.encode(url, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public Map<String, String> pathParams(String[] pathSplit, int start, int end) {
        Map<String, String> conditionParams = new LinkedHashMap<String, String>();
        {
            start = Math.max(0, start);
            end = Math.min(Math.max(end, 0), pathSplit.length);
            for (int i = start; i < end; i++) {
                String tmp = pathSplit[i];
                String[] tmpSplit = StringUtils.split(tmp, "=");
                if (tmpSplit.length != 2) {
                    continue;
                }
                conditionParams.put(tmpSplit[0], urlDecode(tmpSplit[1]));
            }
        }
        return conditionParams;
    }

    public Map<String, String> postParams(Map<String, List<String>> map) {
        Map<String, String> conditionParams = new HashMap<String, String>();
        for (Entry<String, List<String>> entry : map.entrySet()) {
            String value = entry.getValue() != null && entry.getValue().size() > 0 ? entry.getValue().get(0) : null;
            if (value != null) {
                conditionParams.put(entry.getKey(), value);
            }
        }
        return conditionParams;
    }
}
