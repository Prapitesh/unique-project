local key = KEYS[1]

local capacity = tonumber(ARGV[1])
local refillRate = tonumber(ARGV[2])
local currentTime = tonumber(ARGV[3])

local tokens = redis.call('HGET', key, 'tokens')
local lastRefillTime = redis.call('HGET', key, 'lastRefillTime')

if tokens == false then
    tokens = capacity
    lastRefillTime = currentTime
else
    tokens = tonumber(tokens)
    lastRefillTime = tonumber(lastRefillTime)

    local elapsedSeconds = (currentTime - lastRefillTime) / 1000
    local newTokens = elapsedSeconds * refillRate

    tokens = math.min(capacity, tokens + newTokens)
end

local allowed = 0

if tokens >= 1 then
    tokens = tokens - 1
    allowed = 1
end

redis.call('HSET', key,
        'tokens', tokens,
        'lastRefillTime', currentTime
)

return allowed