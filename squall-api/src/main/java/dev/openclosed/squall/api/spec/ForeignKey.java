package dev.openclosed.squall.api.spec;

import java.util.List;
import java.util.Map;

/**
 * A foreign key constraint.
 */
public interface ForeignKey extends Constraint {

    /**
     * Returns the referenced table name.
     * @return the referenced table name.
     */
    String tableName();

    List<String> tableParents();

    String qualifiedTableName();

    String fullTableName();

    Map<String, String> columnMapping();

    default boolean containsKey(String column) {
        return columnMapping().containsKey(column);
    }
}
