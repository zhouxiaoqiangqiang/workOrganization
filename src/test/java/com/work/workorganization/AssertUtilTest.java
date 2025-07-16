package com.work.workorganization;

import com.work.workorganization.utils.AssertUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * -@Desc: AssertUtil工具类测试类
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/16 10:52
 **/
public class AssertUtilTest {

    public static void main(String[] args) {
        try {
            AssertUtil.isNull(null, "Object should be null");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isNull(null) passed");
        }

        try {
            AssertUtil.isNull("not null", "Object should be null");
            System.out.println("Test isNull(\"not null\") failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isNull(\"not null\") passed");
        }

        try {
            AssertUtil.isBlank("", "String should be blank");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isBlank(\"\") passed");
        }

        try {
            AssertUtil.isBlank("not blank", "String should be blank");
            System.out.println("Test isBlank(\"not blank\") failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isBlank(\"not blank\") passed");
        }

        try {
            AssertUtil.isEmpty(new ArrayList<>(), "Collection should be empty");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEmpty(new ArrayList<>()) passed");
        }

        try {
            AssertUtil.isEmpty(Arrays.asList("item"), "Collection should be empty");
            System.out.println("Test isEmpty(Arrays.asList(\"item\")) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEmpty(Arrays.asList(\"item\")) passed");
        }

        try {
            AssertUtil.isEmpty(new HashMap<>(), "Map should be empty");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEmpty(new HashMap<>()) passed");
        }

        try {
            AssertUtil.isEmpty(Collections.singletonMap("key", "value"), "Map should be empty");
            System.out.println("Test isEmpty(Collections.singletonMap(\"key\", \"value\")) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEmpty(Collections.singletonMap(\"key\", \"value\")) passed");
        }

        try {
            AssertUtil.isEmpty(new Object[0], "Array should be empty");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEmpty(new Object[0]) passed");
        }

        try {
            AssertUtil.isEmpty(new Object[]{"item"}, "Array should be empty");
            System.out.println("Test isEmpty(new Object[]{\"item\"}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEmpty(new Object[]{\"item\"}) passed");
        }

        try {
            AssertUtil.isTrue(true, "Condition should be true");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isTrue(true) passed");
        }

        try {
            AssertUtil.isTrue(false, "Condition should be true");
            System.out.println("Test isTrue(false) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isTrue(false) passed");
        }

        try {
            AssertUtil.isFalse(false, "Condition should be false");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isFalse(false) passed");
        }

        try {
            AssertUtil.isFalse(true, "Condition should be false");
            System.out.println("Test isFalse(true) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isFalse(true) passed");
        }

        try {
            AssertUtil.isPositive(1, "Number should be positive");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isPositive(1) passed");
        }

        try {
            AssertUtil.isPositive(-1, "Number should be positive");
            System.out.println("Test isPositive(-1) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isPositive(-1) passed");
        }

        try {
            AssertUtil.isEquals("expected", "expected", "Objects should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(\"expected\", \"expected\") passed");
        }

        try {
            AssertUtil.isEquals("expected", "actual", "Objects should be equal");
            System.out.println("Test isEquals(\"expected\", \"actual\") failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(\"expected\", \"actual\") passed");
        }

        try {
            AssertUtil.isEqualsIgnoreCase("expected", "expected", "Strings should be equal ignoring case");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEqualsIgnoreCase(\"expected\", \"expected\") passed");
        }

        try {
            AssertUtil.isEqualsIgnoreCase("expected", "EXPECTED", "Strings should be equal ignoring case");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEqualsIgnoreCase(\"expected\", \"EXPECTED\") passed");
        }

        try {
            AssertUtil.isEqualsIgnoreCase("expected", "actual", "Strings should be equal ignoring case");
            System.out.println("Test isEqualsIgnoreCase(\"expected\", \"actual\") failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEqualsIgnoreCase(\"expected\", \"actual\") passed");
        }

        try {
            AssertUtil.isEquals(new Object[]{"expected"}, new Object[]{"expected"}, "Arrays should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new Object[]{\"expected\"}, new Object[]{\"expected\"}) passed");
        }

        try {
            AssertUtil.isEquals(new Object[]{"expected"}, new Object[]{"actual"}, "Arrays should be equal");
            System.out.println("Test isEquals(new Object[]{\"expected\"}, new Object[]{\"actual\"}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new Object[]{\"expected\"}, new Object[]{\"actual\"}) passed");
        }

        try {
            AssertUtil.isEquals(Collections.singletonList("expected"), Collections.singletonList("expected"), "Collections should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(Arrays.asList(\"expected\"), Arrays.asList(\"expected\")) passed");
        }

        try {
            AssertUtil.isEquals(Collections.singletonList("expected"), Collections.singletonList("actual"), "Collections should be equal");
            System.out.println("Test isEquals(Arrays.asList(\"expected\"), Arrays.asList(\"actual\")) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(Arrays.asList(\"expected\"), Arrays.asList(\"actual\")) passed");
        }

        try {
            AssertUtil.isEqualsUnordered(Arrays.asList("expected", "actual"), Arrays.asList("actual", "expected"), "Collections should have same elements");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEqualsUnordered(Arrays.asList(\"expected\", \"actual\"), Arrays.asList(\"actual\", \"expected\")) passed");
        }

        try {
            AssertUtil.isEqualsUnordered(Arrays.asList("expected", "actual"), Arrays.asList("expected", "actual"), "Collections should have same elements");
            System.out.println("Test isEqualsUnordered(Arrays.asList(\"expected\", \"actual\"), Arrays.asList(\"expected\", \"actual\")) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEqualsUnordered(Arrays.asList(\"expected\", \"actual\"), Arrays.asList(\"expected\", \"actual\")) passed");
        }

        try {
            AssertUtil.isEquals(Collections.singletonMap("key", "expected"), Collections.singletonMap("key", "expected"), "Maps should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(Collections.singletonMap(\"key\", \"expected\"), Collections.singletonMap(\"key\", \"expected\")) passed");
        }

        try {
            AssertUtil.isEquals(Collections.singletonMap("key", "expected"), Collections.singletonMap("key", "actual"), "Maps should be equal");
            System.out.println("Test isEquals(Collections.singletonMap(\"key\", \"expected\"), Collections.singletonMap(\"key\", \"actual\")) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(Collections.singletonMap(\"key\", \"expected\"), Collections.singletonMap(\"key\", \"actual\")) passed");
        }

        try {
            AssertUtil.isEquals(new int[]{1}, new int[]{1}, "Primitive arrays should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new int[]{1}, new int[]{1}) passed");
        }

        try {
            AssertUtil.isEquals(new int[]{1}, new int[]{2}, "Primitive arrays should be equal");
            System.out.println("Test isEquals(new int[]{1}, new int[]{2}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new int[]{1}, new int[]{2}) passed");
        }
        try {
            AssertUtil.isEquals(new long[]{1L}, new long[]{1L}, "Primitive arrays should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new long[]{1L}, new long[]{1L}) passed");
        }

        try {
            AssertUtil.isEquals(new long[]{1L}, new long[]{2L}, "Primitive arrays should be equal");
            System.out.println("Test isEquals(new long[]{1L}, new long[]{2L}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new long[]{1L}, new long[]{2L}) passed");
        }

        try {
            AssertUtil.isEquals(new byte[]{1}, new byte[]{1}, "Primitive arrays should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new byte[]{1}, new byte[]{1}) passed");
        }

        try {
            AssertUtil.isEquals(new byte[]{1}, new byte[]{2}, "Primitive arrays should be equal");
            System.out.println("Test isEquals(new byte[]{1}, new byte[]{2}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new byte[]{1}, new byte[]{2}) passed");
        }

        try {
            AssertUtil.isEquals(new char[]{'a'}, new char[]{'a'}, "Primitive arrays should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new char[]{'a'}, new char[]{'a'}) passed");
        }

        try {
            AssertUtil.isEquals(new char[]{'a'}, new char[]{'b'}, "Primitive arrays should be equal");
            System.out.println("Test isEquals(new char[]{'a'}, new char[]{'b'}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new char[]{'a'}, new char[]{'b'}) passed");
        }

        try {
            AssertUtil.isEquals(new double[]{1.0}, new double[]{1.0}, "Primitive arrays should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new double[]{1.0}, new double[]{1.0}) passed");
        }

        try {
            AssertUtil.isEquals(new double[]{1.0}, new double[]{2.0}, "Primitive arrays should be equal");
            System.out.println("Test isEquals(new double[]{1.0}, new double[]{2.0}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new double[]{1.0}, new double[]{2.0}) passed");
        }

        try {
            AssertUtil.isEquals(new float[]{1.0f}, new float[]{1.0f}, "Primitive arrays should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new float[]{1.0f}, new float[]{1.0f}) passed");
        }

        try {
            AssertUtil.isEquals(new float[]{1.0f}, new float[]{2.0f}, "Primitive arrays should be equal");
            System.out.println("Test isEquals(new float[]{1.0f}, new float[]{2.0f}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new float[]{1.0f}, new float[]{2.0f}) passed");
        }

        try {
            AssertUtil.isEquals(new boolean[]{true}, new boolean[]{true}, "Primitive arrays should be equal");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new boolean[]{true}, new boolean[]{true}) passed");
        }

        try {
            AssertUtil.isEquals(new boolean[]{true}, new boolean[]{false}, "Primitive arrays should be equal");
            System.out.println("Test isEquals(new boolean[]{true}, new boolean[]{false}) failed");
        } catch (IllegalArgumentException e) {
            System.out.println("Test isEquals(new boolean[]{true}, new boolean[]{false}) passed");
        }
    }
}
