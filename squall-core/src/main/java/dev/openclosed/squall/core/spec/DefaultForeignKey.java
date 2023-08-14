package dev.openclosed.squall.core.spec;

import dev.openclosed.squall.api.spec.ForeignKey;
import dev.openclosed.squall.api.spec.SchemaObjectRef;

import java.util.Map;
import java.util.Optional;

public record DefaultForeignKey(
    Optional<String> constraintName,
    Optional<String> databaseName,
    Optional<String> schemaName,
    String tableName,
    Map<String, String> columnMapping
    ) implements ForeignKey {

    public DefaultForeignKey {
        columnMapping = Map.copyOf(columnMapping);
    }

    public DefaultForeignKey(
        Optional<String> constraintName, SchemaObjectRef table, Map<String, String> columnMapping) {
        this(constraintName, table.databaseName(), table.schemaName(), table.objectName(), columnMapping);
    }

    @Override
    public String qualifiedTableName() {
        var b = new StringBuilder();
        schemaName().ifPresentOrElse(
            name -> b.append(name).append('.'),
            () -> databaseName().ifPresent(name -> b.append(name).append('.'))
        );
        return b.append(tableName).toString();
    }

    @Override
    public String getFullyQualifiedTargetColumn(String sourceColumn) {
        String targetColumn = getTargetColumn(sourceColumn);
        var b = new StringBuilder();
        databaseName().ifPresent(b::append);
        b.append('.');
        schemaName().ifPresent(b::append);
        b.append('.').append(tableName());
        b.append('.').append(targetColumn);
        return b.toString();
    }
}
