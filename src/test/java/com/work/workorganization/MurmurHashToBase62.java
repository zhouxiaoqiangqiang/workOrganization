package com.work.workorganization;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * 将Url长连接 生成短链接 跳转
 */
public class MurmurHashToBase62 {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String toBase62(int value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.insert(0, BASE62.charAt(value % 62));
            value /= 62;
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        // 长链
        String input = "https://yuanjava.cnposts/short-link-system/design?code=xsd&page=1";
        // 长链利用 MurmurHash算法生成 32位 10进制数
        HashFunction hashFunction = Hashing.murmur3_32();
        int hash = hashFunction.hashString(input, StandardCharsets.UTF_8).asInt();
        if (hash < 0) {
            hash = hash & 0x7fffffff; // Convert to positive by dropping the sign bit
        }
        // 将 32位 10进制数 转换成 62进制
        String base62Hash = toBase62(hash);
        System.out.println("生成的短链接base62Hash:" + base62Hash);
        /**
         * 生成短链接之后
         * 在落库 至  长连接短链接关系映射前
         * 需要 在 数据库中 校验幂等性 因为 有可能 哈希冲突导致相同短链接
         */
    }
}
