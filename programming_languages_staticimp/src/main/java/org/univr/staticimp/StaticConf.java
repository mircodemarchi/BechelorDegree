package org.univr.staticimp;

import org.univr.staticimp.type.ExpType;

import java.util.HashMap;
import java.util.Map;

public class StaticConf {

    private final Map<String, ExpType> store;

    public StaticConf() {
        store = new HashMap<>();
    }

    public void put(String id, ExpType t) {
        store.put(id, t);
    }

    public ExpType get(String id) {
        return store.get(id);
    }
}
