package com.code.baseservice.util;

import com.code.baseservice.base.constant.RedisConstant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * redis操作实现类
 *
 * @author parker
 * @date 2019/12/19
 */
@Service
public class RedisUtil{
    /**
     * 日期格式，月日时，例如：20092316
     */
    public static final String DATE_TIME_FORMAT_YYMMDDHHMMSS = "YYMMddHHmmss";
    /**  7位尾数拼接 */
    public static final long DATE_TIME_BASE=1000000L;
    /**  7位尾数拼接,超过500万 */
    public static final long MAX_DATE_TIME_BASE=5000000L;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private  StringRedisTemplate    stringRedisTemplate;
    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递增
     *
     * @param key 键
     * @return
     */
    public long incr(String key) {
        if (StringUtils.isEmpty(key)) {
            key = RedisConstant.DEFAULTLIST;
        }
        return redisTemplate.opsForValue().increment(key, 1);
    }


    /**
     * 递减操作
     *
     * @param key
     * @param by
     * @return
     */
    
    public Long decr(String key, Long by) {
        return redisTemplate.opsForValue().increment(key, -by);
    }

    /**
     * 递减操作
     *
     * @param by
     * @param key
     * @return
     */
    
    public double incrByDouble(String key, double increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * 递减操作
     * @param key
     * @return
     */
    
    public double decrByDouble(String key, double increment) {
        return redisTemplate.opsForValue().increment(key, -increment);
    }
    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public Double incrementScore(String key, String value,Double score){
        return (Double) redisTemplate.execute((RedisCallback<Double>) connection -> connection.zIncrBy(key.getBytes(),score,value.getBytes()));
    }

    
    public Long  rank(String key, String value){
        return  (Long) redisTemplate.execute((RedisCallback<Long>) connection -> connection.zRank(key.getBytes(),value.getBytes()));
    }


    
    public void zrem(String key, String member) {
        redisTemplate.execute((RedisCallback) connection -> connection.zRem(key.getBytes(),member.getBytes()));
    }

    
    public void zrem(String value, int startIndex, int endIndex) {
        redisTemplate.execute((RedisCallback) connection -> connection.zRemRange(value.getBytes(),startIndex,endIndex));
    }


    
    public Set<RedisZSetCommands.Tuple> zrange(String key, int startIndex, int endIndex) {
        return (Set<RedisZSetCommands.Tuple>) redisTemplate.execute((RedisCallback<Set<RedisZSetCommands.Tuple>>) connection -> connection.zRangeWithScores(key.getBytes(),startIndex,endIndex));
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    
    public boolean setString(String key, String value, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    
    public boolean set(String key, String value) {
        boolean result = (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.set(serializer.serialize(key), serializer.serialize(value));
            return true;
        });
        return result;
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     * 217
     */
    
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @return true 成功 false失败
     * 217
     */
    
    public boolean hasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }
    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @return 值
     */
    
    public Set<String> hkeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }
    public   long hincrement(String key,String key2,long sco){
        return redisTemplate.opsForHash().increment(key, key2,sco);
    }
    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param map 值
     * @return true 成功 false失败
     * 217
     */
    
    public boolean hsetMap(String key, Map map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key      键
     * @param map      值
     * @param time     时间 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @param timeUnit 时间单位
     * @return true 成功 false失败
     * 217
     */
    
    public boolean hsetMap(String key, Map map, int time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            redisTemplate.expire(key, time, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    
    public Object get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            return getValue(key);
        }
    }

    private Object getValue(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            Object object = redisTemplate.opsForValue().get(key);
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
            return object;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    
    public Object getString(String key) {
        try {
            String objValue=stringRedisTemplate.opsForValue().get(key);
            if(null!=objValue){
                objValue= objValue.replaceAll("\"","");
            }
            return objValue;
        } catch (Exception e) {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        }
    }

    
    public Object getWithOutDefaultSerializer(String key) {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        Object obj = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(obj)) {
            return null;
        }
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 获取某一微秒时间的自增序列（用于订单号生成）
     *
     * @param seqKey key名称
     * @return 自增值
     */
    
    public long getCurrentIncrementSeq(String seqKey) {
        RedisAtomicLong counter = new RedisAtomicLong(seqKey, redisTemplate.getConnectionFactory());
        //设置过期时间
        counter.expire(10L,TimeUnit.SECONDS);
        //返回redis计数
        return counter.incrementAndGet();
    }

    /**
     * 获取自增序列
     *
     * @param seqKey key名称
     * @return 自增值
     */
    
    public long getIncrementSeq(String seqKey) {
        RedisAtomicLong counter = new RedisAtomicLong(seqKey, redisTemplate.getConnectionFactory());
        long inLong=counter.incrementAndGet();
        if(inLong<=1){
            counter.expire(10L,TimeUnit.DAYS);
        }else if(inLong==MAX_DATE_TIME_BASE){
            //超过最大值重置
            counter.expire(1L,TimeUnit.MILLISECONDS);
        }else if(inLong>MAX_DATE_TIME_BASE+100L){
            //inLong==MAX_DATE_TIME_BASE处理异常情况下的兼容性处理
            counter.expire(1L,TimeUnit.MILLISECONDS);
        }
        //唯一id：当前时间字符串精确到秒+redis计数
        String nowStr = DateUtil.format(new Date(),DATE_TIME_FORMAT_YYMMDDHHMMSS)+(DATE_TIME_BASE+inLong) ;
        int iLen=nowStr.length();
        if(iLen > 19){
            if(iLen == 20){
                //当长度到达20位时，时间在9月23至9月30，会出现超过long类型范围的情况,需要截取首位，排除掉
                nowStr=  nowStr.substring(iLen-18, iLen);
            }else{
                nowStr = nowStr.substring(iLen-19, iLen);
            }
        }
        return Long.parseLong(nowStr);
    }

    
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    
    public RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }


    
    public RLock mlock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.MILLISECONDS);
        return lock;
    }


    
    public boolean tryLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock();
    }

    
    public boolean tryLock(String lockKey, int waitTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    
    public boolean tryLockWithLeaseTime(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(0, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    
    public boolean tryLockWithLeaseTime(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(0, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    
    public boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }



    
    public void unlock(String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        } catch (Exception e) {
            //
        }
    }

    
    public void unlock(RLock rLock) {
        try {
            if (null != rLock && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 缓存放入List
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    
    public boolean leftPushAll(String key, Object... value) {
        try {
            redisTemplate.opsForList().leftPushAll(key,value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
    public boolean leftPushAll(String key, List<Integer> value) {
        try {
            redisTemplate.opsForList().leftPushAll(key,value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 缓存放入List
     *
     * @param key   键
     * @return true成功 false失败
     */
    
    public Object rightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 缓存List通过index查找原始
     *
     * @param key   键
     * @return true成功 false失败
     */
    
    public Object indexList(String key,long index) {
        try {
            return   redisTemplate.opsForList().index(key,index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    
    public boolean removeList(String key,Object value) {
        try {
            redisTemplate.opsForList().remove(key,1,value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 删除List缓存中的值
     *
     * @param key   键
     * @param index 索引
     * @return true成功 false失败
     */
    
    public  Object removeListByIndex(String key, long index){
        Object objData=  indexList(key,index);
        if(removeList(key,objData)){
            return  objData;
        }
        return null;
    }
    /**
     * 删除List缓存中的值
     *
     * @param key   键
     * @return true成功 false失败
     */
    
    public  Object list(String key){
        return redisTemplate.opsForList().range(key, 0, -1);
    }
    /**
     *  ist缓存中size
     *
     * @param key   键
     * @return true成功 false失败
     */
    
    public  long listSize(String key){
        return redisTemplate.opsForList().size(key);
    }
}