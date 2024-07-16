package com.work.workorganization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class redisTest {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 操作String类型的数据
     */
    @Test
    public void test01String(){
        //获取操作String类型的接口对象
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //存值
        valueOperations.set("city123","beijing");

        //取值
        String city123 = (String) valueOperations.get("city123");
        System.out.println("city123 = " + city123);

        //存值，同时设置过期时间   TimeUnit：超时时间单位
        /*
            void set(K key, V value, long timeout, TimeUnit unit);
                key ：字段key
                value：key对应的值
                timeout：超时时间
                TimeUnit：超时时间单位
         */
        valueOperations.set("username","suoge",10000, TimeUnit.SECONDS);
        String username = valueOperations.get("username").toString();
        System.out.println("username = " + username);

        //存值，如果存在则不执行任何操作
        Boolean aBoolean = valueOperations.setIfAbsent("city1234", "nanjing");
        System.out.println(aBoolean);
        String city1234 = valueOperations.get("city1234").toString();
        System.out.println("city1234 = " + city1234);


    }


    /**
     * 操作Hash类型数据
     */
    @Test
    public void testHash(){
        //获取操作Hash类型的接口对象
        HashOperations hashOperations = redisTemplate.opsForHash();
        //存值 下面的代码相当于命令:hset person name xiaoming
        //pseron表示键，name表示字段名  xiaoming表示字段值
        hashOperations.put("person","name","xiaoming");
        hashOperations.put("person","age","20");
        hashOperations.put("person","address","bj");

        //取值
        //下面的代码相当于执行命令：hget 键 字段===》hget person age===>表示根据键和字段名获取字段值
        String age = (String) hashOperations.get("person", "age");
        System.out.println(age);
        //获得hash结构中的所有字段
        //下面的代码相当于执行命令：HKEYS 键===》HKEYS person
        Set keys = hashOperations.keys("person");
        for (Object key : keys) {
            System.out.println(key);
        }

        //获得hash结构中的所有值
        //HVALS 键
        List values = hashOperations.values("person");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作List类型的数据
     */
    @Test
    public void testList(){
        //获取操作列表类型的接口对象
        ListOperations listOperations = redisTemplate.opsForList();

        //存值
        //命令lpush 键 元素 元素...
        listOperations.leftPush("mylist","a");
        listOperations.leftPushAll("mylist","b","c","d");

        //取值
        //命令：lrange 键 开始 结束
        //下面的代码是查询所有
        List<String> mylist = listOperations.range("mylist", 0, -1);
        for (String value : mylist) {
            System.out.println(value);
        }

        //获得列表长度 命令：llen 键
        Long size = listOperations.size("mylist");
        for (int i = 0; i < size; i++) {
            //出队列
            //命令：rpop 键
            //从右边删除一个元素，返回被删除的元素
            String element = (String) listOperations.rightPop("mylist");
            System.out.println(element);
        }
    }

    /**
     * 操作Set类型的数据
     */
    @Test
    public void testSet(){
        //获取操作set类型的接口对象
        SetOperations setOperations = redisTemplate.opsForSet();

        //存值
        //sadd 键 元素 元素...
        setOperations.add("myset","a","b","c","a");

        //取值
        //smembers 键 : 得到这个集合中所有的元素
        Set<String> myset = setOperations.members("myset");
        for (String o : myset) {
            System.out.println(o);
        }

        //删除成员
        //srem 键 元素 元素...
        setOperations.remove("myset","a","b");

        //取值
        myset = setOperations.members("myset");
        for (String o : myset) {
            System.out.println(o);
        }

    }

    /**
     * 操作ZSet类型的数据
     */
    @Test
    public void testZset(){
        //获取操作zSet类型的接口对象
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        //存值
        //Boolean add(K var1, V var2, double var3)  var1 表示键  var2 表示值   var3表示分数
        zSetOperations.add("myZset","a",10.0);//myZset 表示键  a 表示值   10.0 表示分数
        zSetOperations.add("myZset","b",11.0);
        zSetOperations.add("myZset","c",12.0);
        zSetOperations.add("myZset","a",13.0);

        //取值
        //命令：zrange 键 开始索引 结束索引
        //获取指定范围的元素，得到所有的元素，索引是0到-1
        Set<String> myZset = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }
        //修改分数
        //下面的方法表示在原来分数上进行加20
        zSetOperations.incrementScore("myZset","c",20.0);

        //删除成员
        zSetOperations.remove("myZset","a","b");

        //取值
        Set<ZSetOperations.TypedTuple> myZset1 = zSetOperations.rangeWithScores("myZset", 0, -1);
        for (ZSetOperations.TypedTuple typedTuple : myZset1) {
            Double score = typedTuple.getScore();
            Object value = typedTuple.getValue();
            System.out.println(score+"---"+value);
        }
    }

    /**
     * 通用操作，针对不同的数据类型都可以操作
     */
    @Test
    public void testCommon(){
        //获取Redis中所有的key
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        //判断某个key是否存在
        Boolean itcast = redisTemplate.hasKey("itcast");
        System.out.println(itcast);

        //删除指定key
        redisTemplate.delete("myZset");

        //获取指定key对应的value的数据类型
        DataType dataType = redisTemplate.type("myset");
        System.out.println(dataType.name());

    }
}
