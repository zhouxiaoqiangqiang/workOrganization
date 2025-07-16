package com.work.workorganization.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * -@Desc:  校验工具类 (包含判空和对象相等性比较功能)
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/16 10:08
 **/
public class AssertUtil {

    // ===================== 判空方法 =====================

    /**
     * 判断对象是否为空
     *
     * @param object  要检查的对象
     * @param message 异常消息
     * @throws IllegalArgumentException 如果对象为空
     */
    public static void isNull(Object object, String message) {
        if (ObjectUtil.isNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断字符串是否为空或空白
     *
     * @param text    要检查的字符串
     * @param message 异常消息
     * @throws IllegalArgumentException 如果字符串为空或空白
     */
    public static void isBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 要检查的集合
     * @param message    异常消息
     * @throws IllegalArgumentException 如果集合为空或空集合
     */
    public static void isEmpty(Collection<?> collection, String message) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断Map是否为空
     *
     * @param map     要检查的Map
     * @param message 异常消息
     * @throws IllegalArgumentException 如果Map为空或空Map
     */
    public static void isEmpty(Map<?, ?> map, String message) {
        if (CollectionUtil.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断数组是否为空
     *
     * @param array   要检查的数组
     * @param message 异常消息
     * @throws IllegalArgumentException 如果数组为空或空数组
     */
    public static void isEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断条件是否为true
     *
     * @param condition 要检查的条件
     * @param message   异常消息
     * @throws IllegalArgumentException 如果条件为false
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断条件是否为false
     *
     * @param condition 要检查的条件
     * @param message   异常消息
     * @throws IllegalArgumentException 如果条件为true
     */
    public static void isFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断数字是否为正数
     *
     * @param number  要检查的数字
     * @param message 异常消息
     * @throws IllegalArgumentException 如果数字不是正数
     */
    public static void isPositive(Number number, String message) {
        if (number == null || number.doubleValue() <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    // ===================== 相等性比较方法 =====================

    /**
     * 判断两个对象是否相等
     *
     * @param expected 预期值
     * @param actual   实际值
     * @param message  异常消息
     * @throws IllegalArgumentException 如果对象不相等
     */
    public static void isEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断两个字符串是否相等（忽略大小写）
     *
     * @param expected 预期字符串
     * @param actual   实际字符串
     * @param message  异常消息
     * @throws IllegalArgumentException 如果字符串不相等 或者 其中有一个为空或者空字符串
     */
    public static void isEqualsIgnoreCase(String expected, String actual, String message) {
        if (StringUtils.isAnyBlank(expected, actual) || !expected.equalsIgnoreCase(actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断两个数组是否相等
     *
     * @param expected 预期数组
     * @param actual   实际数组
     * @param message  异常消息
     * @throws IllegalArgumentException 如果数组不相等
     */
    public static void isEquals(Object[] expected, Object[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断两个集合是否相等（顺序敏感）
     *
     * @param expected 预期集合
     * @param actual   实际集合
     * @param message  异常消息
     * @throws IllegalArgumentException 如果集合不相等
     */
    public static void isEquals(Collection<?> expected, Collection<?> actual, String message) {
        if (!CollectionUtil.isEqualList(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断两个集合内容是否相同（顺序不敏感）
     *
     * @param expected 预期集合
     * @param actual   实际集合
     * @param message  异常消息
     * @throws IllegalArgumentException 如果集合内容不相同
     */
    public static void isEqualsUnordered(Collection<?> expected, Collection<?> actual, String message) {
        if (!CollectionUtil.containsAll(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断两个Map是否相等
     *
     * @param expected 预期Map
     * @param actual   实际Map
     * @param message  异常消息
     * @throws IllegalArgumentException 如果Map不相等
     */
    public static void isEquals(Map<?, ?> expected, Map<?, ?> actual, String message) {
        if (
                (CollectionUtil.isNotEmpty(expected) && CollectionUtil.isEmpty(actual))
                        || (CollectionUtil.isNotEmpty(actual) && CollectionUtil.isEmpty(expected))
                        || !expected.equals(actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断两个原始类型数组是否相等
     *
     * @param expected 预期数组
     * @param actual   实际数组
     * @param message  异常消息
     * @throws IllegalArgumentException 如果数组不相等
     */
    public static void isEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEquals(long[] expected, long[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEquals(byte[] expected, byte[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEquals(char[] expected, char[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEquals(double[] expected, double[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEquals(float[] expected, float[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEquals(boolean[] expected, boolean[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }
}