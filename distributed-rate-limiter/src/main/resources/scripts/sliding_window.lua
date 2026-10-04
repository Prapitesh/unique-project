local key = KEYS[1]

local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local member = ARGV[3]

local time = redis.call('TIME')
local currentTime = tonumber(time[1]) + tonumber(time[2]) / 1000000

local cutoff = currentTime - window

redis.call('ZREMRANGEBYSCORE', key, '-inf', cutoff)

local count = redis.call('ZCARD', key)

if count < limit then
    redis.call('ZADD', key, currentTime, member)
    redis.call('EXPIRE', key, window * 2)
    return 1
end

return 0