package org.univr.staticimp.type;

import java.util.Objects;

public class ComType extends Type {

    public static final ComType INSTANCE = new ComType();

    private ComType() { }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}
