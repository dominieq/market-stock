package org.example.marketstock.exceptions.simulation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Arrays;

public class InvalidDrawParamsException extends RuntimeException {

    private final Object[] params;

    public InvalidDrawParamsException(final String message,
                                      final Object[] params) {

        super(message + " " + Arrays.toString(params));
        this.params = params;
    }

    public Object[] getParams() {
        return params;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("params", params)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvalidDrawParamsException)) return false;
        InvalidDrawParamsException that = (InvalidDrawParamsException) o;
        return Objects.equal(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(params);
    }
}
