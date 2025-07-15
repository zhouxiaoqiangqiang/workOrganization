package com.work.workorganization.utils;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * -@Desc:  分片查询工具类 (适用于根据某单个参数集合 来进行查询 时的场景)
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/15 17:52
 **/
public class BatchQueryDBUtil {

    /**
     * 分片查询
     *
     * @param inputList           输入参数集合
     * @param batchSize           每次查询的参数集合大小
     * @param queryMapperFunction 查询方法
     * @param <T>                 输入参数类型
     * @param <R>                 输出参数类型
     */
    public static <T, R> List<R> batchQuery(List<T> inputList, int batchSize, Function<List<T>, List<R>> queryMapperFunction) {
        if (CollectionUtil.isEmpty(inputList)) {
            return Collections.emptyList();
        }

        //不需要分片的情况
        if (inputList.size() <= batchSize) {
            return queryMapperFunction.apply(inputList);
        }

        //需要分片的情况
        List<R> resultList = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i += batchSize) {

            int toIndex = Math.min(i + batchSize, inputList.size());

            List<T> subList = inputList.subList(i, toIndex);

            List<R> subResultList = queryMapperFunction.apply(subList);

            if (CollectionUtil.isNotEmpty(subResultList)) {
                resultList.addAll(subResultList);
            }
        }
        return resultList;
    }

    /**
     * 针对于 Oracle数据库的分片查询 默认1000条
     *
     * @param inputList           输入参数集合
     * @param batchSize           每次查询的参数集合大小
     * @param queryMapperFunction 查询方法
     * @param <T>                 输入参数类型
     * @param <R>                 输出参数类型
     */
    public static <T, R> List<R> batchQueryForOracle(List<T> inputList, int batchSize, Function<List<T>, List<R>> queryMapperFunction) {
        return batchQuery(inputList, 1000, queryMapperFunction);
    }

}
