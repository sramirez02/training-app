package com.tuempresa.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final AtomicLong counter = new AtomicLong(1);

    public static Long generateId() {
        return counter.getAndIncrement();
    }
}