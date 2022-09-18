package org.idea.threadpool.monitor.report.redis;

import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author idea
 * @data 2020/4/1
 */
public interface IRedisService {

    String get(String key);

    String setStr(String key, String value, int ttl, TimeUnit timeUnit);

    Integer setInt(String key, Integer value, int ttl, TimeUnit timeUnit);

    Long setLong(String key, Long value, int ttl, TimeUnit timeUnit);

    Object getObj(String key, Class clazz);

    Object setObj(String key, Object value, int ttl, TimeUnit timeUnit);

    Integer getInt(String key);

    Long getLong(String key);

    Boolean exists(String key);

    Boolean setnx(String key, String value);

    /**
     * @param key
     * @return >0存活时长 -2不存在 -1没有时长限制
     */
    Long ttl(String key);

    /**
     * 给键值延长时长
     *
     * @param key
     * @return
     */
    Boolean expire(String key, int ttl, TimeUnit timeUnit);

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    Boolean del(String key);

    /**
     * 执行lua脚本内容
     *
     * @param luaScript
     * @param keys
     * @param args
     * @return
     */
    Object execLua(String luaScript, List<String> keys, List<String> args);

    /**
     * 默认增加1
     *
     * @param key
     * @return
     */
    Boolean incr(String key);

    /**
     * 增加n
     *
     * @param key
     * @param n
     * @return
     */
    Boolean incrBy(String key, Integer n);


    /**
     *
     * @param key
     * @param step
     * @return
     */
    Long incrByStep(String key, Integer step);

    /**
     * 默认减少1
     *
     * @param key
     * @return
     */
    Boolean decr(String key);

    /**
     * 默认减少n
     *
     * @param key
     * @param n
     * @return
     */
    Boolean decrBy(String key, Integer n);

    /**
     * 存储map格式参数到redia
     *
     * @param key
     * @param map
     * @return
     */
    Boolean setMap(String key, Map<String, String> map);

    /**
     * 存储map的一行格式
     *
     * @param mapKey
     * @param key
     * @param value
     * @return
     */
    Boolean setMapItem(String mapKey, String key, String value);


    /**
     * 对map对某一个key增加一定incr
     *
     * @param mapKey
     * @param key
     * @param incr
     * @return
     */
    Boolean hinCrByKey(String mapKey, String key, Integer incr);

    /**
     * 删除map的某一项属性值
     *
     * @param mapKey
     * @param key
     * @return
     */
    Boolean deleteMapItem(String mapKey, String key);

    /**
     * 设置过期时间
     *
     * @param key
     * @param map
     * @param expireSeconds
     * @return
     */
    Boolean setMapEx(String key, Map<String, String> map, int expireSeconds);

    /**
     * 获取redis内部的map
     *
     * @param key
     * @return
     */
    Map<String, String> getMap(String key);


    /**
     * 获取map的总长度
     *
     * @param key
     * @return
     */
    Long hlen(String key);

    /**
     * 获取map里面的某个字段
     *
     * @param mapKey
     * @param fieldName
     * @param clazz
     * @return
     */
    <T> Object getMapField(String mapKey, String fieldName, T clazz);

    /**
     * 返回集合中的所有key成员
     *
     * @param key
     * @return
     */
    Set<String> sMembers(String key);

    /**
     * 随机从set集合中抽取部分数据
     *
     * @param key
     * @param count
     * @return
     */
    List<String> sRandMember(String key, int count);

    /**
     * 往set集合中新增数值
     *
     * @param key
     * @param value
     * @return
     */
    Boolean sAdd(String key, String... value);

    /**
     * 移除set集合中的某个值
     *
     * @param key
     * @param value
     * @return
     */
    Boolean sRem(String key, String value);

    /**
     * 判断某个value是否存在于set集合中
     *
     * @param key
     * @param value
     * @return
     */
    Boolean sIsMember(String key, String value);

    /**
     * 返回set集合中元素的数量
     *
     * @param key
     * @return
     */
    Long sCard(String key);


    /**
     * 查询多个key的set集合之间共同部分的交集
     *
     * @param key
     * @return
     */
    Set<String> sInter(String... key);

    /**
     * 多个集合之间不同的部分
     *
     * @param key
     * @return
     */
    Set<String> sdiff(String... key);

    /**
     * 提供redis的渐进式遍历
     *
     * @param cursor
     * @param scanParams
     * @return
     */
    List<String> scan(String cursor, ScanParams scanParams);


    /**
     * 提供redis的从头到尾的扫描遍历
     *
     * @param cursor
     * @param pattern
     * @param limit
     * @return
     */
    Set<String> scanAll(String cursor, String pattern, Integer limit);


    /**
     * 遍历set集合里面的键元素
     *
     * @param key
     * @param cursor
     * @param pattern
     * @param limit
     * @return
     */
    List<String> sScanAll(String key, String cursor, String pattern, Integer limit);


    /**
     * 遍历hash集合里面的键元素
     *
     * @param key
     * @param cursor
     * @param pattern
     * @param limit
     * @return
     */
    Map<String,String> hScanAll(String key, String cursor, String pattern, Integer limit);


    /**
     * 遍历hash集合里面的键元素
     *
     * @param key
     * @param cursor
     * @param pattern
     * @param limit
     * @return
     */
    List<Tuple> zScanAll(String key, String cursor, String pattern, Integer limit);

    /**
     * 批量插入数据
     *
     * @param multiMap
     * @return
     */
    Boolean multiSet(Map<String, String> multiMap, int expiredSeconds);

    /**
     * 往list里面塞
     *
     * @param key
     * @param value
     * @return
     */
    Long lpush(String key, String value);

    /**
     * 从list移出最后一个元素
     *
     * @return
     */
    String lpop(String key);

    /**
     * 从popListName弹出，然后写入到backupListName，超时时长是timeout
     * @param popListName
     * @param backupListName
     * @param timeOut
     */
    String brPopLpush(String popListName, String backupListName, int timeOut);

    /**
     * 查询某个list集合中的指定元素
     *
     * @param key
     * @param begin
     * @param end
     * @return
     */
    List<String> lrange(String key, int begin, int end);

    /**
     * 从list弹出第一个元素
     * @param key
     * @return
     */
    String rpop(String key);

    /**
     * 基于zset做全量的排序 从小到大
     *
     * @param key
     * @return
     */
    Set<Tuple> zRangeAllWithScores(String key);

    /**
     * 基于zset做定量排序 从小到大
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<Tuple>  zRangeWithScores(String key, int start, int end);

    /**
     * 基于zset做全量排序 从大到小
     * @param key
     * @return
     */
    Set<String>  zRevRangeAll(String key);

    /**
     * 基于zset做定量排序 从大到小
     * @param key
     * @param start
     * @param stop
     * @return
     */
    Set<String>  zRevRange(String key, int start, int stop);

    /**
     * 添加元素到zset中
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    Long zAdd(String key, double score, String member);

    Long zCard(String key);

    /**
     * 从zset中移除批量元素
     *
     * @param key
     * @param members
     * @return
     */
    Long zRem(String key, List<String> members);

    Long zremrangeByRank(String key, int begin, int end);

    /**
     * 创建指定key的经纬度存储
     *
     * @param key
     * @param longitude
     * @param latitude
     * @param member
     * @return
     */
    boolean geoAdd(String key, double longitude, double latitude, String member);

    /**
     * 计算两个位置点之间的距离
     *
     * @param key
     * @param addr1
     * @param addr2
     * @return
     */
    Double geoDist(String key, String addr1, String addr2);


    /**
     * 根据给定的经纬度计算size范围内的几个地理位置
     *
     * @param key
     * @param longitude
     * @param latitude
     * @param size
     * @param geoUnit
     * @return
     */
    List<GeoRadiusResponse> geoRadius(String key, double longitude, double latitude, double size, GeoUnit geoUnit);

    /**
     * 根据指定的地理位置，返回size范围内的所有地理坐标
     *
     * @param key
     * @param member
     * @param size
     * @param geoUnit
     * @return
     */
    List<GeoRadiusResponse> geoRadiusByMember(String key, String member, double size, GeoUnit geoUnit);

}
