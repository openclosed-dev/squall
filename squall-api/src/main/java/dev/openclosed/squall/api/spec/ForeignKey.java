package dev.openclosed.squall.api.spec;

import java.util.Map;

/**
 * A foreign key constraint.
 */
public interface ForeignKey extends Constraint, TableRef {

    Map<String, String> columnMapping();

    default boolean containsKey(String column) {
        return columnMapping().containsKey(column);
    }
}
