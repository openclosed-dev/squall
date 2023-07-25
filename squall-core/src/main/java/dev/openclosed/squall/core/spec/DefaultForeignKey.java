package dev.openclosed.squall.core.spec;

import dev.openclosed.squall.api.spec.ForeignKey;
import dev.openclosed.squall.api.spec.TableRef;

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

    public DefaultForeignKey(Optional<String> name, TableRef table, Map<String, String> columnMapping) {
        this(name, table.databaseName(), table.schemaName(), table.tableName(), columnMapping);
    }
}
