package dev.openclosed.squall.api.spec;

import java.util.Map;

/**
 * A foreign key constraint.
 */
public interface ForeignKey extends Constraint, SchemaObjectRef {

    /**
     * Returns the referenced table name.
     * @return the referenced table name.
     */
    String tableName();

    String qualifiedTableName();

    Map<String, String> columnMapping();

    default boolean containsKey(String column) {
        return columnMapping().containsKey(column);
    }

    default String getTargetColumn(String sourceColumn) {
        return columnMapping().get(sourceColumn);
    }

    String getFullyQualifiedTargetColumn(String sourceColumn);

    // SchemaObjectRef

    @Override
    default String objectName() {
        return tableName();
    }
}
