package dev.openclosed.squall.api.spec;

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
