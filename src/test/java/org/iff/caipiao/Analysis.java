/*******************************************************************************
 * Copyright (c) 2019-02-25 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.caipiao;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.iff.datarest.util.Tuple;
import org.iff.infra.util.FCS;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Analysis
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-02-25
 * auto generate by qdp.
 */
public class Analysis {

    //^.{20,}03 .*06 .*11 .*21 .*31
    private static final Map<Integer, int[]> numbers = new LinkedHashMap<>();
    private static final Map<Integer, int[]> repeats4 = new LinkedHashMap<>();
    private static final Map<Integer, Integer> repeats4Counts = new LinkedHashMap<>();
    private static final Map<Integer, List<Integer>> repeats4Distance = new LinkedHashMap<>();
    private static final Map<Long, int[]> repeats5 = new LinkedHashMap<>();
    private static final Map<Long, Integer> repeats5Counts = new LinkedHashMap<>();
    private static final Map<Long, List<Integer>> repeats5Distance = new LinkedHashMap<>();
    private static final Map<String, Integer> redRates = new LinkedHashMap<>();

    public static void main(String[] args) throws Exception {
        parse();
        redRates();
        {
            Map<String, Integer> analysis = analysisBlue();
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : analysis.entrySet()) {
                list.add(new Tuple.Two<>(entry.getKey(), entry.getValue()));
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    //return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    return o1.first().compareTo(o2.first());
                }
            });
            printChart(list, null);
        }
        {
            Map<String, Integer> analysis = analysisRed();
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : analysis.entrySet()) {
                list.add(new Tuple.Two<>(entry.getKey(), entry.getValue()));
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    //return o1.first().compareTo(o2.first());
                }
            });
            printChart(list, null);
        }
        {
            Map<String, Integer> analysis = analysisRedInBlue08();
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : analysis.entrySet()) {
                list.add(new Tuple.Two<>(entry.getKey(), entry.getValue()));
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    //return o1.first().compareTo(o2.first());
                }
            });
            printChart(list, null);
        }
        {
            Map<String, Integer> analysis = analysisReds();
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : analysis.entrySet()) {
                list.add(new Tuple.Two<>(entry.getKey(), entry.getValue()));
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    //return new Integer(o1.first()).compareTo(new Integer(o2.first()));
                }
            });
            printChart(list, null);
        }
        {
            Map<String, Integer> analysis = redRates;
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : analysis.entrySet()) {
                list.add(new Tuple.Two<>(entry.getKey(), entry.getValue()));
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    //return new Integer(o1.first()).compareTo(new Integer(o2.first()));
                }
            });
            printChart(list, null);
        }
        {
            Map<String, Integer> analysis = analysis4Repeats();
            System.out.println("==analysis4Repeats==" + analysis.size());
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : analysis.entrySet()) {
                list.add(new Tuple.Two<>(entry.getKey(), entry.getValue()));
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    //return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    //return new Integer(o1.first()).compareTo(new Integer(o2.first()));
                    return o1.first().compareTo(o2.first());
                }
            });
            printChart(list, null);
        }
        {
            Map<Integer, List<Integer>> analysis = repeats4Distance;
            System.out.println("==repeats4Distance==" + analysis.size());
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<Integer, List<Integer>> entry : analysis.entrySet()) {
                for (Integer issue : entry.getValue()) {
                    list.add(new Tuple.Two<>(entry.getKey().toString(), issue));
                }
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    //return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    return o1.first().compareTo(o2.first());
                }
            });
            printChart(list, null);
        }
        {
            Map<String, Integer> analysis = analysis5Repeats();
            System.out.println("==analysis5Repeats==" + analysis.size());
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : analysis.entrySet()) {
                list.add(new Tuple.Two<>(entry.getKey(), entry.getValue()));
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    //return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    //return new Integer(o1.first()).compareTo(new Integer(o2.first()));
                    return o1.first().compareTo(o2.first());
                }
            });
            printChart(list, null);
        }
        {
            Map<Long, List<Integer>> analysis = repeats5Distance;
            System.out.println("==repeats5Distance==" + analysis.size());
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<Long, List<Integer>> entry : analysis.entrySet()) {
                for (Integer issue : entry.getValue()) {
                    list.add(new Tuple.Two<>(entry.getKey().toString(), issue));
                }
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    //return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    return o1.first().compareTo(o2.first());
                }
            });
            printChart(list, null);
        }
        {
            Map<String, Integer> analysis = redRateAll();
            List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : analysis.entrySet()) {
                list.add(new Tuple.Two<>(entry.getKey(), entry.getValue()));
            }
            Collections.sort(list, new Comparator<Tuple.Two<Object, String, Integer>>() {
                public int compare(Tuple.Two<Object, String, Integer> o1, Tuple.Two<Object, String, Integer> o2) {
                    //return o1.second() > o2.second() ? -1 : (o1.second().intValue() == o2.second().intValue() ? 0 : 1);
                    return new Integer(o1.first()).compareTo(new Integer(o2.first()));
                }
            });
            printChart(list, null);
        }
    }


    public static Map<String, Integer> analysisBlue() {
        Map<String, Integer> total = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : numbers.entrySet()) {
            int[] counts = new int[]{0/*01-04*/, 0/*05-08*/, 0/*09-12*/, 0/*13-16*/};
            int issue = entry.getKey();
            int[] nums = entry.getValue();
            {
                int num = nums[nums.length - 1];
                counts[BigDecimal.valueOf(num).divide(BigDecimal.valueOf(4.1), RoundingMode.DOWN).intValue()] += 1;
            }
            String key = counts[0] + "" + counts[1] + "" + counts[2] + "" + counts[3];
            if (total.containsKey(key)) {
                total.put(key, total.get(key) + 1);
            } else {
                total.put(key, 1);
            }
        }
        return total;
    }

    public static Map<String, Integer> analysisRed() {
        Map<String, Integer> total = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : numbers.entrySet()) {
            int[] counts = new int[]{0/*01-07*/, 0/*08-14*/, 0/*15-21*/, 0/*22-28*/, 0/*29-33*/};
            int issue = entry.getKey();
            int[] nums = entry.getValue();
            for (int i = 0; i < nums.length - 1; i++) {
                int num = nums[i];
                counts[BigDecimal.valueOf(num).divide(BigDecimal.valueOf(7.1), RoundingMode.DOWN).intValue()] += 1;
            }
            String key = counts[0] + "" + counts[1] + "" + counts[2] + "" + counts[3] + "" + counts[4];
            if (total.containsKey(key)) {
                total.put(key, total.get(key) + 1);
            } else {
                total.put(key, 1);
            }
        }
        return total;
    }

    public static Map<String, Integer> analysisRedInBlue08() {
        Map<String, Integer> total = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : numbers.entrySet()) {
            int[] counts = new int[]{0/*01-07*/, 0/*08-14*/, 0/*15-21*/, 0/*22-28*/, 0/*29-33*/};
            int issue = entry.getKey();
            int[] nums = entry.getValue();
            if (nums[nums.length - 1] > 8) {
                continue;
            }
            for (int i = 0; i < nums.length - 1; i++) {
                int num = nums[i];
                counts[BigDecimal.valueOf(num).divide(BigDecimal.valueOf(7.1), RoundingMode.DOWN).intValue()] += 1;
            }
            String key = counts[0] + "" + counts[1] + "" + counts[2] + "" + counts[3] + "" + counts[4];
            if (total.containsKey(key)) {
                total.put(key, total.get(key) + 1);
            } else {
                total.put(key, 1);
            }
        }
        return total;
    }

    public static Map<String, Integer> analysisReds() {
        Map<String, Integer> total = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : numbers.entrySet()) {
            int issue = entry.getKey();
            int[] nums = entry.getValue();
            for (int i = 0; i < nums.length - 1; i++) {
                int num = nums[i];
                String key = String.valueOf(num);
                if (total.containsKey(key)) {
                    total.put(key, total.get(key) + 1);
                } else {
                    total.put(key, 1);
                }
            }
        }
        return total;
    }

    public static Map<String, Integer> analysis4Repeats() {
        Map<String, Integer> total = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : numbers.entrySet()) {
            int issue = entry.getKey();
            int[] nums = entry.getValue();
            for (int i = nums.length - 2; i >= 1; i--) {//remove 1 number, not contains the blue number.
                for (int j = i - 1; j >= 0 && j < i; j--) {//remove 2 number.
                    int value = 1_00_00_00_00;
                    int[] nums4 = new int[4];
                    int pos = 0;
                    for (int k = 0; k < nums.length - 1; k++) {
                        if (k == i || k == j) {
                            continue;
                        }
                        nums4[pos] = nums[k];
                        pos = pos + 1;
                    }
                    value = value + nums4[0] * 1_00_00_00 + nums4[1] * 1_00_00 + nums4[2] * 1_00 + nums4[3];
                    if (repeats4.containsKey(value)) {
                        repeats4Counts.put(value, (repeats4Counts.containsKey(value) ? repeats4Counts.get(value) : 1) + 1);
                    } else {
                        repeats4.put(value, nums4);
                    }
                    if (!repeats4Distance.containsKey(value)) {
                        repeats4Distance.put(value, new ArrayList<>());
                    }
                    repeats4Distance.get(value).add(issue);
                }
            }
        }
        for (Integer key : repeats4Distance.keySet().toArray(new Integer[repeats4Distance.size()])) {
            if (!repeats4Counts.containsKey(key)) {
                repeats4Distance.remove(key);
            }
        }
        for (Map.Entry<Integer, Integer> entry1 : repeats4Counts.entrySet()) {
            total.put(entry1.getKey().toString(), entry1.getValue());
        }
        return total;
    }

    public static Map<String, Integer> analysis5Repeats() {
        Map<String, Integer> total = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : numbers.entrySet()) {
            int issue = entry.getKey();
            int[] nums = entry.getValue();
            for (int i = nums.length - 2; i >= 1; i--) {//remove 1 number, not contains the blue number.
                long value = 1_00_00_00_00_00L;
                int[] nums5 = new int[5];
                int pos = 0;
                for (int k = 0; k < nums.length - 1; k++) {
                    if (k == i) {
                        continue;
                    }
                    nums5[pos] = nums[k];
                    pos = pos + 1;
                }
                value = value + nums5[0] * 1_00_00_00_00 + nums5[1] * 1_00_00_00 + nums5[2] * 1_00_00 + nums5[3] * 1_00 + nums5[4];
                if (repeats5.containsKey(value)) {
                    repeats5Counts.put(value, (repeats5Counts.containsKey(value) ? repeats5Counts.get(value) : 1) + 1);
                } else {
                    repeats5.put(value, nums5);
                }
                if (!repeats5Distance.containsKey(value)) {
                    repeats5Distance.put(value, new ArrayList<>());
                }
                repeats5Distance.get(value).add(issue);
            }
        }
        for (Long key : repeats5Distance.keySet().toArray(new Long[repeats5Distance.size()])) {
            if (!repeats5Counts.containsKey(key)) {
                repeats5Distance.remove(key);
            }
        }
        for (Map.Entry<Long, Integer> entry1 : repeats5Counts.entrySet()) {
            total.put(entry1.getKey().toString(), entry1.getValue());
        }
        return total;
    }

    public static Map<String, Integer> redRateAll() throws Exception {
        Map<String, Integer> all = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : numbers.entrySet()) {
            int issue = entry.getKey();
            int[] nums = entry.getValue();
            int rateTotal = 0;
            for (int i = 0; i < nums.length - 1; i++) {
                rateTotal += redRates.get(String.valueOf(nums[i]));
            }
            all.put(String.valueOf(issue), rateTotal);
        }
        return all;
    }

    public static void redRates() throws Exception {
        Map<String, Integer> total = new HashMap<>();
        int counts = 0;
        for (Map.Entry<Integer, int[]> entry : numbers.entrySet()) {
            int issue = entry.getKey();
            int[] nums = entry.getValue();
            for (int i = 0; i < nums.length - 1; i++) {
                int num = nums[i];
                String key = String.valueOf(num);
                if (total.containsKey(key)) {
                    total.put(key, total.get(key) + 1);
                } else {
                    total.put(key, 1);
                }
                counts += 1;
            }
        }
        for (Map.Entry<String, Integer> entry : total.entrySet()) {
            redRates.put(entry.getKey(), BigDecimal.valueOf(entry.getValue() * 100000).divide(BigDecimal.valueOf(counts), RoundingMode.DOWN).intValue());
        }
    }

    public static void parse() throws Exception {
        String fileName = FCS.get("/Users/zhaochen/dev/caipiao/caipiao-{begin}-{end}.txt", GetData.beginIssue, GetData.endIssue).toString();
        String content = FileUtils.readFileToString(new File(fileName), "UTF-8");
        String[] lines = StringUtils.split(content, '\n');
        for (String line : lines) {
            String[] split = StringUtils.split(line, '|');
            if (split.length < 10) {
                continue;
            }
            String[] numbersStr = StringUtils.split(split[3], ' ');
            if (numbersStr.length != 7) {
                continue;
            }
            int[] ns = new int[]{new Integer(numbersStr[0]), new Integer(numbersStr[1]), new Integer(numbersStr[2]), new Integer(numbersStr[3]),
                    new Integer(numbersStr[4]), new Integer(numbersStr[5]), new Integer(numbersStr[6])};
            numbers.put(new Integer(split[0]), ns);
        }
    }

    /**
     * <pre>
     * 20-|
     * 19-|
     * 18-|
     * 17-|
     * 16-|
     * 15-|
     * 14-|
     * 13-|
     * 12-|
     * 11-|
     * 10-|
     * 09-|
     * 08-|    -
     * 07-|    -
     * 06-|    -
     * 05-|    -
     * 04-|    -
     * 03-|    -
     * 02-|    -
     * 01-|    -
     * 00-|------------------------------------------------------>
     *        01
     * </pre>
     *
     * @param list
     */
    public static List<char[]> printChart(List<Tuple.Two<Object, String/*name*/, Integer/*value*/>> list, int[] ranges) {
        int maxNameLen = 3;
        for (Tuple.Two<Object, String/*name*/, Integer/*value*/> two : list) {
            maxNameLen = Math.max(two.first().length(), maxNameLen);
            maxNameLen = Math.max(String.valueOf(two.second()).length(), maxNameLen);
        }
        int total = 0, maxValue = 0, minValue = Integer.MAX_VALUE;
        for (Tuple.Two<Object, String/*name*/, Integer/*value*/> two : list) {
            total = total + two.second();
            maxValue = Math.max(maxValue, two.second());
            minValue = Math.min(minValue, two.second());
        }
        minValue = Math.min(minValue, maxValue);
        int vSize = 22;
        List<char[]> vertical = new ArrayList<>();
        ranges = ranges == null ? autoRange(maxValue, minValue) : ranges;
        {//add y coordinate
            {
                char[] cs = new char[vSize];
                for (int i = 0; i < vSize - 1; i++) {
                    cs[i] = String.valueOf(120 - i).charAt(1);
                }
                cs[vSize - 1] = ' ';
                vertical.add(cs);
            }
            {
                char[] cs = new char[vSize];
                for (int i = 0; i < vSize - 1; i++) {
                    cs[i] = String.valueOf(120 - i).charAt(2);
                }
                cs[vSize - 1] = ' ';
                vertical.add(cs);
            }
            {
                char[] cs = new char[vSize];
                Arrays.fill(cs, '-');
                cs[vSize - 1] = ' ';
                vertical.add(cs);
            }
            {
                char[] cs = new char[vSize];
                Arrays.fill(cs, '|');
                cs[vSize - 1] = ' ';
                vertical.add(cs);
            }
        }
        for (Tuple.Two<Object, String/*name*/, Integer/*value*/> two : list) {// fill x coordinate
            String name = two.first();
            Integer value = two.second();
            String valueLabel = String.valueOf(value);
            int valueColumn = 1 + maxNameLen / 2 + maxNameLen % 2;
            int nameColumn = 1 + (maxNameLen - name.length()) / 2;
            int valueLabelColumn = 1 + (maxNameLen - valueLabel.length()) / 2 + maxNameLen % 2;
            //int blankColumn = 1 + (maxNameLen - name.length()) / 2;
            int valueHigh = 0;
            for (int r = ranges.length - 1; r >= 0; r--) {
                if (value >= ranges[r]) {
                    valueHigh = r + 1;
                    break;
                }
            }
            for (int col = 0; col < (2 + maxNameLen); col++) {// add column
                char[] cs = new char[vSize];
                Arrays.fill(cs, ' ');
                cs[vSize - 2] = '-';
                vertical.add(cs);
                if (col >= nameColumn && (col - nameColumn) < name.length()) {//set name
                    cs[vSize - 1] = name.charAt(col - nameColumn);
                }
                if (col >= valueLabelColumn && (col - valueLabelColumn) < valueLabel.length()) {//set valueLabel
                    cs[vSize - 2 - valueHigh - 1] = valueLabel.charAt(col - valueLabelColumn);
                }
                if (col == valueColumn - 1) {// set valueHigh value
                    for (int p = 1; p <= valueHigh; p++) {
                        cs[vSize - 2 - p] = '-';
                    }
                }
            }
        }
        {//add last y coordinate
            {
                char[] cs = new char[vSize];
                Arrays.fill(cs, ' ');
                cs[vSize - 2] = '-';
                vertical.add(cs);
            }
            {
                char[] cs = new char[vSize];
                Arrays.fill(cs, ' ');
                cs[vSize - 2] = '>';
                vertical.add(cs);
            }
            {
                char[] cs = new char[vSize];
                Arrays.fill(cs, '\n');
                vertical.add(cs);
            }
        }
        {//print
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < vSize; col++) {
                for (int row = 0; row < vertical.size(); row++) {
                    sb.append(vertical.get(row)[col]);
                }
            }
            System.out.println(sb);
        }
        return vertical;
    }

    private static int[] autoRange(int maxValue, int minValue) {
        maxValue = Math.max(maxValue, 20 + minValue);
        int[] ranges = new int[20];
        ranges[0] = minValue;
        ranges[ranges.length - 2] = maxValue;
        ranges[ranges.length - 1] = maxValue * 10;
        int avg = (maxValue - minValue) / 17;
        for (int i = 1; i < ranges.length - 2; i++) {
            ranges[i] = ranges[i - 1] + avg;
        }
        //System.out.println(Arrays.toString(ranges));
        return ranges;
    }

}
