package com.work.workorganization.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
  * -@Desc: 分片查询工具类
  * -@Author: zhouzhiqiang
  * -@Date: 2025/6/11 14:50
 **/
public class BatchQueryUtils {

    /**
     * 通用分片查询方法
     * @param fullList 需要查询的完整列表
     * @param batchSize 每批次大小
     * @param queryFunction 查询函数（接收子列表，返回查询结果）
     * @param <T> 输入元素类型
     * @param <R> 返回结果类型
     * @return 合并后的查询结果
     */
    public static <T, R> List<R> batchQuery(List<T> fullList, int batchSize, 
                                          Function<List<T>, List<R>> queryFunction) {
        if (fullList == null || fullList.isEmpty()) {
            return Collections.emptyList();
        }

        // 不需要分片的情况
        if (fullList.size() <= batchSize) {
            return queryFunction.apply(fullList);
        }

        List<R> result = new ArrayList<>();
        for (int i = 0; i < fullList.size(); i += batchSize) {
            int toIndex = Math.min(i + batchSize, fullList.size());
            List<T> subList = fullList.subList(i, toIndex);
            
            List<R> batchResult = queryFunction.apply(subList);
            if (batchResult != null && !batchResult.isEmpty()) {
                result.addAll(batchResult);
            }
        }
        return result;
    }

    /**
     * 针对Oracle的专用分片查询方法（默认1000的限制）
     * @param fullList 需要查询的完整列表
     * @param queryFunction 查询函数
     * @param <T> 输入元素类型
     * @param <R> 返回结果类型
     * @return 合并后的查询结果
     */
    public static <T, R> List<R> oracleBatchQuery(List<T> fullList, 
                                                Function<List<T>, List<R>> queryFunction) {
        return batchQuery(fullList, 1000, queryFunction);
    }
}