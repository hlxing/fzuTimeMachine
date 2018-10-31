for i, v in ipairs(ARGV) do
    redis.call('rpush', KEYS[1], v)
end