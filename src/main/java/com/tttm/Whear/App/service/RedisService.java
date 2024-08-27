package com.tttm.Whear.App.service;

public interface RedisService {
    public void setValue(String key, Object value);
    public void setExpire(String key, int timeOut);
    public <T> T getValue(String key, Class<T> classValue);
    public <T> T getValue(String key, String childKey, Class<T> classValue);
    public void setExpire(final String key, Object value, Long expireTime);
    public Boolean exist(String key);
    public void deleteKey(String key);
}
