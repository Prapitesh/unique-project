local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local leakRate = tonumber(ARGV[2])
local time = redis.call('TIME')
local currentTime = tonumber(time[1]) + tonumber(time[2]) / 1000000
local level = tonumber(redis.call('HGET', key, 'level') or '0')
local lastLeak = tonumber(redis.call('HGET', key, 'lastLeak') or currentTime)
local elapsed = currentTime - lastLeak
level = math.max(0, level - elapsed * leakRate)
if level + 1 > capacity then
    local retryAfterMs = math.floor((level + 1 - capacity) / leakRate * 1000)
    return {0, retryAfterMs}
end
level = level + 1
redis.call('HSET', key, 'level', level)
redis.call('HSET', key, 'lastLeak', currentTime)
redis.call('EXPIRE', key, math.ceil(capacity / leakRate) + 1)
return {1, 0}