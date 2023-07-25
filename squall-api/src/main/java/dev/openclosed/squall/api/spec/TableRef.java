package dev.openclosed.squall.api.spec;

import java.util.Objects;
import java.util.Optional;

/**
 * A reference to a table.
 */
public interface TableRef {

    default Optional<String> databaseName() {
        return Optional.empty();
    }

    default Optional<String> schemaName() {
        return Optional.empty();
    }

    String tableName();

    static TableRef tableInSchema(String tableName, String schemaName) {
        Objects.requireNonNull(tableName);
        Objects.requireNonNull(schemaName);
        return new TableRef() {
            @Override
            public Optional<String> schemaName() {
                return Optional.of(schemaName);
            }
            @Override
            public String tableName() {
                return tableName;
            }
        };
    }
}
