package org.univr.staticimp;

import org.univr.staticimp.value.ExpValue;

import java.util.HashMap;
import java.util.Map;

public class DynamicConf {

    private final Map<String, ExpValue> store;

    public DynamicConf() {
        store = new HashMap<>();
    }

    public void put(String id, ExpValue<?> v) {
        store.put(id, v);
    }

    public ExpValue<?> get(String id) {
        return store.get(id);
    }
}
