package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @ClassName: FastJson2JsonRedisSerializer
 * @Auther: zhangyingqi
 * @Date: 2018/8/28 16:11
 * @Description: 自定义序列化
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
 
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
 
    private Class<T> clazz;
 
    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super(); this.clazz = clazz;
    }
 
        @Override
        public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSONObject.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }
 
        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return (T) JSONObject.parseObject(str, clazz);
    }
}