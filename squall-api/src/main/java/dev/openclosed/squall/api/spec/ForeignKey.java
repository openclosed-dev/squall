package dev.openclosed.squall.api.spec;

import java.util.Map;

/**
 * A foreign key constraint.
 */
public interface ForeignKey extends Constraint, TableRef {

    Map<String, String> columnMapping();
}
