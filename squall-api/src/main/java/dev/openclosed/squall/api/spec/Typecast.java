package dev.openclosed.squall.api.spec;

import dev.openclosed.squall.api.expression.Expression;

/**
 * Typecast expression.
 */
public interface Typecast extends Expression, DataType {

    /**
     * Returns the source expression to be typecasted.
     * @return the source expression.
     */
    Expression source();
}
