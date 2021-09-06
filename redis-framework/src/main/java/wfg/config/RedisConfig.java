package wfg.config;


import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {


    //修改缓存存储的序列化方式
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){


        RedisTemplate<String,Object>  template= new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());


        // value的序列化采用fastjson
       FastJson2JsonRedisSerializer<Object> fastJsonRedis = new FastJson2JsonRedisSerializer<>(Object.class);

        // 设置value序列化
        template.setValueSerializer(fastJsonRedis);
        template.setHashValueSerializer(fastJsonRedis);

        template.afterPropertiesSet();
        return  template;
    }


}
