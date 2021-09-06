package redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis 操作工具类
 */
@Component
public class RedisCache {

    @Autowired
    public RedisTemplate redisTemplate;
    /**
     * 缓存的基本对象，Integer，String，实体类
     *
     */
    public  <T> void setCacheObject(String key,final T value){
        redisTemplate.opsForValue().set(key,value);
    }
    /**
     * 设置有效时间
     * 时间单位：秒
     */
    public boolean expire(final String key,final long timeout){
        return  expire(key,timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public boolean expire(final String key,final long timeout,final TimeUnit unit){
        return redisTemplate.expire(key,timeout,unit);
    }

    /**
     * 获得缓存的基本对象
     */
    public <T> T getCacheObject(final String key){
        ValueOperations<String,T> operations=redisTemplate.opsForValue();
        return  operations.get(key);
    }
    /**
     * 删除单个对象
     *
     */
    public boolean deleteObject(final String key){
        return  redisTemplate.delete(key);
    }
    /**
     * 删除集合对象
     */
    public long deleteObject(final Collection collection){
        return  redisTemplate.delete(collection);
    }

    /**
     * 缓存list对象
     * @param key
     * @param dataList
     * @param <T>
     * @return
     */
    public <T> long setCacheList(final String key,final List<T> dataList){
        //Long 类型
        Long count = redisTemplate.opsForList().rightPushAll(key,dataList);
        return  count==null?0:count;
    }

    /**
     * 获得缓存的list对象
     * @param key
     * @param <T>
     * @return
     */
    public <T> List<T> getCacheList(final String key){
        return   redisTemplate.opsForList().range(key,0,-1);
    }

    /**
     * 缓存 map
     * @param key
     * @param dataMap
     * @param <T>
     */
    public <T> void setCacheMap(final String key ,final Map<String,T> dataMap){
        if(dataMap != null){
            redisTemplate.opsForHash().putAll(key,dataMap);
        }
    }

    /**
     *  增加给定hash键的值
     * @param key
     * @param dataMapKey
     * @param t
     * @param <T>
     */
    public <T> void setCacheMapKey(final String key ,final T dataMapKey,T t){

            redisTemplate.opsForHash().put(key,dataMapKey,t);

    }

    /**
     *  map 键自增
     * @param key
     * @param mapKey
     * @param <T>
     */
    public <T> void setCacheMapIncrement(final String key ,final String mapKey){
        if(mapKey != null){
            redisTemplate.opsForHash().increment(key,mapKey,1);
        }
    }


    /**
     *   entries
     *   用于以Map的格式获取一个Hash键的所有值。
     * 获取缓存的map
     * @param key
     * @param <T>
     * @return
     */
    public <T> Map<String,T> getCacheMap(final String key){
        return  redisTemplate.opsForHash().entries(key);
    }
    /**
     *   entries
     *   用于以Map的格式获取一个Hash键的给定键的值。
     * 获取缓存的map
     * @param <T>
     * @param key
     * @return
     */
    public <T> Object getCacheMapKey(final String key, final T mapKey){
        return  redisTemplate.opsForHash().get(key,mapKey);
    }
    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
