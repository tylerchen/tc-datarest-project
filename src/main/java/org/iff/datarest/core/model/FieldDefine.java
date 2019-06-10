/*******************************************************************************
 * Copyright (c) 2019-03-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 ******************************************************************************/
package org.iff.datarest.core.model;

import org.apache.commons.collections.SetUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * FieldDefine
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-03-28
 * auto generate by qdp.
 */
public class FieldDefine implements Serializable {
    protected Class type;
    protected String name;

    public static FieldDefine create(Class<?> cls, String name) {
        return new FieldDefine().type(cls).name(name);
    }

    public static FieldDefine createString(String name) {
        return new FieldDefine().type(String.class).name(name);
    }

    public static Set<FieldDefine> set(FieldDefine... fields) {
        HashSet<FieldDefine> set = new HashSet<>();
        if (fields.length > 0) {
            set.addAll(Arrays.asList(fields));
        }
        return set;
    }

    public static Set<FieldDefine> unmodifiableSet(FieldDefine... fields) {
        HashSet<FieldDefine> set = new HashSet<>();
        if (fields.length > 0) {
            set.addAll(Arrays.asList(fields));
        }
        return SetUtils.unmodifiableSet(set);
    }

    public String name() {
        return name;
    }

    public FieldDefine name(String name) {
        this.name = name;
        return this;
    }

    public Class type() {
        return type;
    }

    public FieldDefine type(Class type) {
        this.type = type;
        return this;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || (getClass() != o.getClass() && o.getClass() != String.class)) {
            return false;
        }
        FieldDefine that = (FieldDefine) o;
        return Objects.equals(name, that.name);
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return "FieldDefine{" + "type=" + type + ", name='" + name + '\'' + '}';
    }
}
