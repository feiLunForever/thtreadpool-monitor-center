package org.idea.threadpool.monitor.report.redis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author idea
 * @data 2020/4/1
 */
public class RedisServiceImpl implements IRedisService {

    private static Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);

    private IRedisFactory redisFactory;

    public RedisServiceImpl(IRedisFactory redisFactory) {
        this.redisFactory = redisFactory;
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.get(key);
        } catch (Exception e) {
            log.error("jedis get has error, error is ", e);
        }
        return null;
    }

    @Override
    public String setStr(String key, String value, int ttl, TimeUnit timeUnit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.set(key, value);
            jedis.expire(key, convertToMillisecond(ttl, timeUnit));
            return value;
        } catch (Exception e) {
            log.error("jedis setStr has error, error is ", e);
        }
        return null;
    }

    @Override
    public Integer setInt(String key, Integer value, int ttl, TimeUnit timeUnit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.set(key, String.valueOf(value));
            if (ttl > 0) {
                jedis.expire(key, convertToMillisecond(ttl, timeUnit));
            }
            return value;
        } catch (Exception e) {
            log.error("jedis setStr has error, error is ", e);
        }
        return null;
    }

    @Override
    public Long setLong(String key, Long value, int ttl, TimeUnit timeUnit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.set(key, String.valueOf(value));
            jedis.expire(key, convertToMillisecond(ttl, timeUnit));
            return value;
        } catch (Exception e) {
            log.error("jedis setStr has error, error is ", e);
        }
        return null;
    }

    @Override
    public Object getObj(String key, Class clazz) {
        try (Jedis jedis = redisFactory.getConnection()) {
            String jsonStr = jedis.get(key);
            return JSON.parseObject(jsonStr, clazz);
        } catch (Exception e) {
            log.error("jedis getObj has error, error is ", e);
        }
        return null;
    }

    @Override
    public Object setObj(String key, Object value, int ttl, TimeUnit timeUnit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            String jsonStr = JSON.toJSONString(value);
            jedis.set(key, jsonStr);
            jedis.expire(key, convertToMillisecond(ttl, timeUnit));
            return value;
        } catch (Exception e) {
            log.error("jedis setStr has error, error is ", e);
        }
        return null;
    }

    @Override
    public Integer getInt(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return Integer.valueOf(jedis.get(key));
        } catch (Exception e) {
            log.error("jedis setStr has error, error is ", e);
        }
        return null;
    }

    @Override
    public Long getLong(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return Long.valueOf(jedis.get(key));
        } catch (Exception e) {
            log.error("jedis setStr has error, error is ", e);
        }
        return null;
    }

    @Override
    public Boolean exists(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.exists(key);
        } catch (Exception e) {
            log.error("jedis exists has error, error is ", e);
        }
        return false;
    }

    @Override
    public Boolean setnx(String key, String value) {
        try (Jedis jedis = redisFactory.getConnection()) {
            //设置成功，返回 1 。
            //设置失败，返回 0 。
            Long result = jedis.setnx(key, value);
            return result.intValue() == 1;
        } catch (Exception e) {
            log.error("jedis setnx has error, error is ", e);
        }
        return false;
    }

    @Override
    public Long ttl(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            log.error("jedis ttl has error, error is ", e);
        }
        return null;
    }

    @Override
    public Boolean expire(String key, int ttl, TimeUnit timeUnit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.expire(key, convertToSecond(ttl, timeUnit));
            return true;
        } catch (Exception e) {
            log.error("jedis expire has error, error is ", e);
        }
        return false;
    }

    @Override
    public Boolean del(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.del(key);
            return true;
        } catch (Exception e) {
            log.error("jedis del has error, error is ", e);
        }
        return false;
    }

    @Override
    public Object execLua(String luaScript, List<String> keys, List<String> args) {
        try (Jedis jedis = redisFactory.getConnection()) {
            Object result = jedis.evalsha(jedis.scriptLoad(luaScript), keys, args);
            return result;
        } catch (Exception e) {
            log.error("jedis execLua has error, error is ", e);
        }
        return null;
    }

    @Override
    public Boolean incr(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.incr(key);
            return true;
        } catch (Exception e) {
            log.error("jedis incr has error, error is ", e);
        }
        return false;
    }

    @Override
    public Boolean incrBy(String key, Integer n) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.incrBy(key, n);
            return true;
        } catch (Exception e) {
            log.error("jedis incrBy has error, error is ", e);
        }
        return false;
    }

    @Override
    public Long incrByStep(String key, Integer step) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.incrBy(key, step);
        } catch (Exception e) {
            log.error("jedis incrByStep has error, error is ", e);
        }
        return null;
    }

    @Override
    public Boolean decr(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.decr(key);
            return true;
        } catch (Exception e) {
            log.error("jedis decr has error, error is ", e);
        }
        return false;
    }

    @Override
    public Boolean decrBy(String key, Integer n) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.decrBy(key, n);
            return true;
        } catch (Exception e) {
            log.error("jedis decrBy has error, error is ", e);
        }
        return false;
    }

    @Override
    public Boolean setMap(String key, Map<String, String> map) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.hset(key, map);
            return true;
        } catch (Exception e) {
            log.error("jedis hset has error, error is ", e);
        }
        return true;
    }

    @Override
    public Boolean setMapItem(String mapKey, String key, String value) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.hset(mapKey, key, value);
            return true;
        } catch (Exception e) {
            log.error("jedis hset has error, error is ", e);
        }
        return true;
    }

    @Override
    public Boolean hinCrByKey(String mapKey, String key, Integer incr) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.hincrBy(mapKey, key, incr);
            return true;
        } catch (Exception e) {
            log.error("jedis hset has error, error is ", e);
        }
        return false;
    }

    @Override
    public Boolean deleteMapItem(String mapKey, String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            jedis.hdel(mapKey, key);
            return true;
        } catch (Exception e) {
            log.error("jedis hset has error, error is ", e);
        }
        return true;
    }


    @Override
    public Boolean setMapEx(String key, Map<String, String> map, int expireSecond) {
        try (Jedis jedis = redisFactory.getConnection()) {
            Pipeline pipeline = jedis.pipelined();
            pipeline.hset(key, map);
            pipeline.expire(key, expireSecond);
            pipeline.sync();
            return true;
        } catch (Exception e) {
            log.error("jedis hset has error, error is ", e);
        }
        return null;
    }

    @Override
    public Map<String, String> getMap(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            log.error("jedis hset has error, error is ", e);
        }
        return null;
    }

    @Override
    public Long hlen(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.hlen(key);
        } catch (Exception e) {
            log.error("jedis hset has error, error is ", e);
        }
        return -1L;
    }

    @Override
    public <T> Object getMapField(String mapKey, String fieldName, T clazz) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return (T) jedis.hget(mapKey, fieldName);
        } catch (Exception e) {
            log.error("jedis hset has error, error is ", e);
        }
        return null;
    }

    @Override
    public Set<String> sMembers(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.smembers(key);
        } catch (Exception e) {
            log.error("jedis sMembers has error, error is ", e);
        }
        return null;
    }

    @Override
    public List<String> sRandMember(String key, int count) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.srandmember(key, count);
        } catch (Exception e) {
            log.error("jedis sRandMember has error, error is ", e);
        }
        return null;
    }

    @Override
    public Boolean sAdd(String key, String... value) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.sadd(key, value) > 0;
        } catch (Exception e) {
            log.error("jedis sAdd has error, error is ", e);
        }
        return false;
    }

    @Override
    public Boolean sRem(String key, String value) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.srem(key, value) > 0;
        } catch (Exception e) {
            log.error("jedis sRem has error, error is ", e);
        }
        return false;
    }

    @Override
    public Boolean sIsMember(String key, String value) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            log.error("jedis sIsMember has error, error is ", e);
        }
        return false;
    }

    @Override
    public Long sCard(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.scard(key);
        } catch (Exception e) {
            log.error("jedis sCard has error, error is ", e);
        }
        return -1L;
    }

    @Override
    public Set<String> sInter(String... key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.sinter(key);
        } catch (Exception e) {
            log.error("jedis sInter has error, error is ", e);
        }
        return null;
    }

    @Override
    public Set<String> sdiff(String... key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.sdiff(key);
        } catch (Exception e) {
            log.error("jedis sInter has error, error is ", e);
        }
        return null;
    }

    @Override
    public List<String> scan(String cursor, ScanParams scanParams) {
        try (Jedis jedis = redisFactory.getConnection()) {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            return scanResult.getResult();
        } catch (Exception e) {
            log.error("jedis scan has error, error is ", e);
        }
        return null;
    }

    @Override
    public Set<String> scanAll(String cursor, String pattern, Integer limit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            Set<String> scanSet = new HashSet<>();
            ScanParams scanParams = new ScanParams();
            scanParams.match(pattern);
            scanParams.count(limit);
            while (true) {
                long begin = System.currentTimeMillis();
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                scanSet.addAll(scanResult.getResult());
                if (ScanParams.SCAN_POINTER_START.equals(scanResult.getCursor())) {
                    break;
                }
                System.out.println("耗时：" + (System.currentTimeMillis() - begin) + "ms,查询数目：" + scanResult.getResult().size());
                cursor = scanResult.getCursor();
            }
            return scanSet;
        } catch (Exception e) {
            log.error("jedis scanAll has error, error is ", e);
        }
        return null;
    }

    @Override
    public List<String> sScanAll(String key, String cursor, String pattern, Integer limit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            List<String> resultList = new LinkedList<>();
            while (true) {
                ScanResult<String> scanResult = jedis.sscan(key, cursor);
                cursor = scanResult.getCursor();
                resultList.addAll(scanResult.getResult());
                if (ScanParams.SCAN_POINTER_START.equals(cursor)) {
                    break;
                }
            }
            return resultList;
        } catch (Exception e) {
            log.error("jedis sscanAll has error, error is ", e);
        }
        return null;
    }

    @Override
    public Map<String, String> hScanAll(String key, String cursor, String pattern, Integer limit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            ScanParams scanParams = new ScanParams();
            scanParams.match(pattern);
            scanParams.count(limit);
            Map<String, String> resultList = new HashMap<>();
            while (true) {
                ScanResult scanResult = jedis.hscan(key, cursor, scanParams);
                cursor = scanResult.getCursor();
                if (ScanParams.SCAN_POINTER_START.equals(cursor)) {
                    break;
                }
                List<Map.Entry<String, String>> result = scanResult.getResult();
                for (Map.Entry<String, String> entry : result) {
                    resultList.put(entry.getKey(), entry.getValue());
                }
            }
            return resultList;
        } catch (Exception e) {
            log.error("jedis sscanAll has error, error is ", e);
        }
        return null;
    }

    @Override
    public List<Tuple> zScanAll(String key, String cursor, String pattern, Integer limit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            ScanParams scanParams = new ScanParams();
            scanParams.match(pattern);
            scanParams.count(limit);
            List<Tuple> resultList = new LinkedList<>();
            while (true) {
                ScanResult<Tuple> scanResult = jedis.zscan(key, cursor, scanParams);
                cursor = scanResult.getCursor();
                resultList.addAll(scanResult.getResult());
                if (ScanParams.SCAN_POINTER_START.equals(cursor)) {
                    break;
                }
            }
            return resultList;
        } catch (Exception e) {
            log.error("jedis sscanAll has error, error is ", e);
        }
        return null;
    }

    @Override
    public Boolean multiSet(Map<String, String> multiMap, int expiredSeconds) {
        try (Jedis jedis = redisFactory.getConnection()) {
            Pipeline pipeline = jedis.pipelined();
            for (Map.Entry<String, String> entry : multiMap.entrySet()) {
                SetParams setParams = new SetParams();
                //设置过期的时间 单位为秒
                setParams.ex(expiredSeconds);
                pipeline.set(entry.getKey(), entry.getValue(), setParams);
            }
            //这里可能会堵塞等待响应
            pipeline.sync();
        } catch (Exception e) {
            log.error("jedis multiSet has error, error is ", e);
        }
        return null;
    }

    @Override
    public Long lpush(String key, String value) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.lpush(key, value);
        } catch (Exception e) {
            log.error("jedis lpush has error, error is ", e);
        }
        return null;
    }

    @Override
    public String lpop(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.lpop(key);
        } catch (Exception e) {
            log.error("jedis lpop has error, error is ", e);
        }
        return null;
    }

    @Override
    public String brPopLpush(String popListName, String backupListName, int timeOut) {
        try (Jedis jedis = redisFactory.getConnection()) {
            String result = jedis.brpoplpush(popListName, backupListName, timeOut);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("jedis brPopLpush has error, error is ", e);
        }
        return null;
    }

    @Override
    public List<String> lrange(String key, int begin, int end) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.lrange(key, begin, end);
        } catch (Exception e) {
            log.error("jedis lrange has error, error is ", e);
        }
        return null;
    }

    @Override
    public String rpop(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.rpop(key);
        } catch (Exception e) {
            log.error("jedis rpop has error, error is ", e);
        }
        return null;
    }

    @Override
    public Set<Tuple> zRangeAllWithScores(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.zrangeWithScores(key, 0, Integer.MAX_VALUE);
        } catch (Exception e) {
            log.error("jedis lpop has error, error is ", e);
        }
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<Tuple> zRangeWithScores(String key, int start, int end) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.zrangeWithScores(key, start, end);
        } catch (Exception e) {
            log.error("jedis zRangeWithScores has error, error is ", e);
        }
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> zRevRangeAll(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.zrevrange(key, 0, Integer.MAX_VALUE);
        } catch (Exception e) {
            log.error("jedis zRevRangeAll has error, error is ", e);
        }
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> zRevRange(String key, int start, int stop) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.zrevrange(key, start, stop);
        } catch (Exception e) {
            log.error("jedis zRevRange has error, error is ", e);
        }
        return Collections.EMPTY_SET;
    }

    @Override
    public Long zAdd(String key, double score, String member) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            log.error("jedis zAdd has error, error is ", e);
        }
        return null;
    }


    @Override
    public Long zCard(String key) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.zcard(key);
        } catch (Exception e) {
            log.error("jedis zcard has error, error is ", e);
        }
        return null;
    }

    @Override
    public Long zRem(String key, List<String> members) {
        try (Jedis jedis = redisFactory.getConnection()) {
            String[] membersArr = members.toArray(new String[members.size()]);
            return jedis.zrem(key, membersArr);
        } catch (Exception e) {
            log.error("jedis zRem has error, error is ", e);
        }
        return null;
    }

    @Override
    public Long zremrangeByRank(String key, int begin, int end) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.zremrangeByRank(key, begin, end);
        } catch (Exception e) {
            log.error("jedis zremrangeByRank has error, error is ", e);
        }
        return null;
    }

    @Override
    public boolean geoAdd(String key, double longitude, double latitude, String member) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.geoadd(key, longitude, latitude, member) > 0;
        } catch (Exception e) {
            log.error("jedis geoAdd has error, error is ", e);
        }
        return false;
    }

    @Override
    public Double geoDist(String key, String addr1, String addr2) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.geodist(key, addr1, addr2);
        } catch (Exception e) {
            log.error("jedis geoDist has error, error is ", e);
        }
        return null;
    }

    @Override
    public List<GeoRadiusResponse> geoRadius(String key, double longitude, double latitude, double size, GeoUnit geoUnit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.georadius(key, longitude, latitude, size, geoUnit);
        } catch (Exception e) {
            log.error("jedis geoRadius has error, error is ", e);
        }
        return null;
    }

    @Override
    public List<GeoRadiusResponse> geoRadiusByMember(String key, String member, double size, GeoUnit geoUnit) {
        try (Jedis jedis = redisFactory.getConnection()) {
            return jedis.georadiusByMember(key, member, size, geoUnit);
        } catch (Exception e) {
            log.error("jedis geoRadiusByMember has error, error is ", e);
        }
        return null;
    }

    private static Integer convertToSecond(int ttl, TimeUnit timeUnit) {
        if (ttl < 0) {
            throw new RuntimeException("illegal ttl value");
        }
        switch (timeUnit) {
            case DAYS:
                return ttl * 3600 * 24;
            case HOURS:
                return ttl * 3600;
            case MINUTES:
                return ttl * 60;
            case SECONDS:
                return ttl;
            default:
                throw new RuntimeException("illegal timeUnit");
        }
    }

    private static Integer convertToMillisecond(int ttl, TimeUnit timeUnit) {
        if (ttl < 0) {
            throw new RuntimeException("illegal ttl value");
        }
        switch (timeUnit) {
            case DAYS:
                return ttl * 3600 * 24;
            case HOURS:
                return ttl * 3600;
            case MINUTES:
                return ttl * 60;
            case SECONDS:
                return ttl ;
            default:
                throw new RuntimeException("illegal timeUnit");
        }
    }


    public static void main(String[] args) {
        TimeUnit[] timeUnits = TimeUnit.values();
    }
}
