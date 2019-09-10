package org.univr.staticimp.type;

import java.util.Objects;

public class NaturalType extends ExpType {

    public static final NaturalType INSTANCE = new NaturalType();

    private NaturalType() { }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}
