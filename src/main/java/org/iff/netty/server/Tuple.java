/*******************************************************************************
 * Copyright (c) Sep 24, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.netty.server;

/**
 * A class for carry different parameter type.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 24, 2016
 */
public class Tuple<F, S> {

    private F first;
    private S second;

    private Tuple() {

    }

    private Tuple(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Tuple<F, S> empty() {
        return new Tuple<F, S>();
    }

    public static <F, S> Tuple<F, S> of(F first, S second) {
        return new Tuple<F, S>(first, second);
    }

    public static Tuple<String, String> splitString(String string, String separator) {
        Tuple<String, String> t = Tuple.empty();
        String array[] = string.split(separator, 2);
        if (array.length == 2) {
            t.setFirst(array[0]);
            t.setSecond(array[1]);
        } else {
            t.setFirst(array[0]);
        }
        return t;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public boolean isEmpty() {
        return first == null && second == null;
    }

    public String joinString(String separator) {
        return first + separator + second;
    }
}