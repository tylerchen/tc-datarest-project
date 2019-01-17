/*******************************************************************************
 * Copyright (c) 2018-11-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.datarest.util;

import org.apache.commons.collections.MapUtils;
import org.iff.datarest.core.MyBatisSqlSessionFactory;
import org.iff.infra.util.EventBusHelper;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.ThreadLocalHelper;
import org.iff.infra.util.mybatis.service.RepositoryService;

import javax.validation.constraints.NotNull;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SessionEventHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-14
 * auto generate by qdp.
 */
public class SessionHelper {

    public static <T> Tuple sync(String event, Tuple tuple) {
        SessionHolder holder = (SessionHolder) ThreadLocalHelper.get(SessionHolder.class.getName());
        if (holder == null) {
            holder = new SessionHolder();
            ThreadLocalHelper.set(SessionHolder.class.getName(), holder);
        } else {
            holder.add();
        }
        Throwable error = null;
        try {
            EventBusHelper.me().syncEvent(event, tuple);
        } catch (Throwable e) {
            error = e;
        } finally {
            if (tuple != null && tuple.hasError()) {
                holder.remove(tuple.error());
            } else {
                holder.remove(error);
            }
        }
        return tuple;
    }

    public static <S, T> T callback(@NotNull Callback<S, T> callback, S param) {
        SessionHolder holder = (SessionHolder) ThreadLocalHelper.get(SessionHolder.class.getName());
        if (holder == null) {
            holder = new SessionHolder();
            ThreadLocalHelper.set(SessionHolder.class.getName(), holder);
        } else {
            holder.add();
        }
        Throwable error = null;
        T result = null;
        try {
            result = callback.callback(param);
            return result;
        } catch (Throwable e) {
            error = e;
        } finally {
            Tuple tuple = null;
            if (param instanceof Tuple) {
                tuple = (Tuple) param;
                tuple.result(result);
            }
            if (tuple != null && tuple.hasError()) {
                tuple.result(error);
                holder.remove(tuple.error());
            } else {
                holder.remove(error);
            }
        }
        Exceptions.runtime("SessionEventHelper Callback invoke error!", error);
        return result;
    }

    public static interface Callback<S, T> {
        T callback(S param);
    }

    public static class SessionHolder implements AutoCloseable, Closeable {
        /**
         * 用于 Session 嵌套的计数器目的是防止提前把 Session 关闭了，Session 每打开一次就计数一次（往 count 加 1），
         * 每关闭一次就减少一次（count 减少 1），当count=0时再减少count的值时，就关闭Session。
         */
        private AtomicInteger count = new AtomicInteger(0);
        private List<Throwable> errors = new ArrayList<>();

        public synchronized void add() {
            count.incrementAndGet();
        }

        public synchronized void remove(Throwable error) {
            if (error != null) {
                errors.add(error);
            }
            if (count.get() > 0) {
                count.decrementAndGet();
            } else {
                close();
            }
        }

        public void close() {
            Map<String, RepositoryService> map = ThreadLocalHelper.get(RepositoryService.class.getName());
            Map<String, Closeable> close = ThreadLocalHelper.get(Closeable.class.getName());
            if (errors.size() > 0) {//如果出现异常，就 rollback
                if (MapUtils.isNotEmpty(map)) {
                    for (Map.Entry<String, RepositoryService> entry : map.entrySet()) {
                        MyBatisSqlSessionFactory.rollback(entry.getValue());
                    }
                }
            } else {//没有异常就提交
                if (MapUtils.isNotEmpty(map)) {
                    for (Map.Entry<String, RepositoryService> entry : map.entrySet()) {
                        MyBatisSqlSessionFactory.commit(entry.getValue());
                    }
                }
            }
            if (MapUtils.isNotEmpty(close)) {//关闭其他 Closeable 的资源
                for (Map.Entry<String, Closeable> entry : close.entrySet()) {
                    SocketHelper.closeWithoutError(entry.getValue());
                }
            }
            //删除线程变量
            ThreadLocalHelper.remove();
        }
    }
}
