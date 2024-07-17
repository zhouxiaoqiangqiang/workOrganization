package com.work.workorganization.config.aop;

import cn.hutool.core.util.ObjectUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
  * -@Desc:   接口限流和黑白名单
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/17 16:00
 **/
@Aspect
@Component
public class RateLimitAspect {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private HttpServletRequest request;
	//定义黑名单key前缀
    private static final String BLACKLIST_KEY_PREFIX = "blacklist:";
    //定义白名单key前缀
    private static final String WHITELIST_KEY_PREFIX = "whitelist:";

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
    	//获取IP
        String ip = request.getRemoteAddr();
        
        //黑名单则直接异常
        if (isBlacklisted(ip)) {
            throw new RuntimeException("超出访问限制已加入黑名单，5秒后再访问");
        }

		//如果是白名单下的不做限制
        if (isWhitelisted(ip)) {
            return joinPoint.proceed();
        }

        String key = generateKey(joinPoint, ip);
        int limit = rateLimit.limit();
        int timeout = rateLimit.timeout();

        Object countObj = redisTemplate.opsForValue().get(key);
        int count;
        if (ObjectUtil.isNull(countObj)) {
            count = 0;
        } else {
            String countStr = countObj.toString();
            count = countStr == null ? 0 : Integer.parseInt(countStr);
        }

        if (count < limit) {
            redisTemplate.opsForValue().set(key, String.valueOf(count + 1), timeout, TimeUnit.SECONDS);
            return joinPoint.proceed();
        } else {
            addToBlacklist(ip);
            throw new RuntimeException("超出请求限制IP暂时被列入黑名单,请稍后再试");
        }
    }

    private boolean isBlacklisted(String ip) {
        return redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + ip);
    }

    private boolean isWhitelisted(String ip) {
        return redisTemplate.hasKey(WHITELIST_KEY_PREFIX + ip);
    }

    private void addToBlacklist(String ip) {
        redisTemplate.opsForValue().set(BLACKLIST_KEY_PREFIX + ip, "true", 5, TimeUnit.SECONDS);
    }

    private String generateKey(ProceedingJoinPoint joinPoint, String ip) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        return className + ":" + methodName + ":" + ip;
    }
}
