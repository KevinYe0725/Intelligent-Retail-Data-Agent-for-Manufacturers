package com.kevinye.utils.context;

import java.util.concurrent.ConcurrentHashMap;

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    private static final ConcurrentHashMap<String, Long> sessionUserMap = new ConcurrentHashMap<>();


    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

    // **✅ 存储 WebSocket sessionId -> userId**
    public static void storeUserSession(String sessionId, Long userId) {
        sessionUserMap.put(sessionId, userId);
    }

    // **✅ 通过 sessionId 获取 userId**
    public static Long getUserBySession(String sessionId) {
        return sessionUserMap.get(sessionId);
    }

    // **✅ WebSocket 关闭时，移除 sessionId**
    public static void removeUserSession(String sessionId) {
        sessionUserMap.remove(sessionId);
    }
}
