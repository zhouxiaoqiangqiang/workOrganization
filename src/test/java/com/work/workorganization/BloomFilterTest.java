package com.work.workorganization;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.StandardCharsets;

public class BloomFilterTest {
    public static void main(String[] args) {
        int expectedInsertions = 10000;
        double falsePositiveProbability = 0.01;

        BloomFilter<String> bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                expectedInsertions,
                falsePositiveProbability
        );

        // 向布隆过滤器中添加数据
        bloomFilter.put("apple");
        bloomFilter.put("banana");
        bloomFilter.put("cherry");

        // 测试布隆过滤器是否包含某个元素
        System.out.println("Contains 'apple': " + bloomFilter.mightContain("apple"));
        System.out.println("Contains 'peach': " + bloomFilter.mightContain("peach"));
    }
}