package com.work.workorganization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class testFunctionMethod {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItem{
        private long orderId;
        private String orderName;
        private long orderPrice;
    }

    public static void main(String[] args) {
        List<OrderItem> list = new ArrayList<>();
        list.add(OrderItem.builder().orderId(1).orderName("小米").orderPrice(10).build());
        list.add(OrderItem.builder().orderId(2).orderName("三星").orderPrice(20).build());
        list.add(OrderItem.builder().orderId(3).orderName("苹果").orderPrice(30).build());

        Collection<OrderItem> collection = list;

        Set<OrderItem> set = new HashSet<>(collection);

        Map<Long, OrderItem> map = toMap(set, OrderItem::getOrderId);

        System.out.println("map = " + map);
    }



    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper) {
        return toMap(collection, keyMapper, Function.identity());
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> collection,
                                            Function<? super T, ? extends K> keyFunction,
                                            Function<? super T, ? extends V> valueFunction) {
        return toMap(collection, keyFunction, valueFunction, pickSecond());
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> collection,
                                            Function<? super T, ? extends K> keyFunction,
                                            Function<? super T, ? extends V> valueFunction,
                                            BinaryOperator<V> mergeFunction) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashMap<>(0);
        }

        return collection.stream().collect(Collectors.toMap(keyFunction, valueFunction, mergeFunction));
    }

    public static <T> BinaryOperator<T> pickFirst() {
        return (k1, k2) -> k1;
    }
    public static <T> BinaryOperator<T> pickSecond() {
        return (k1, k2) -> k2;

    }
}
