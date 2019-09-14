package org.univr.staticimp.type;

import java.util.Objects;

public class BooleanType extends ExpType {

    public static final BooleanType INSTANCE = new BooleanType();

    private BooleanType() { }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }

}
