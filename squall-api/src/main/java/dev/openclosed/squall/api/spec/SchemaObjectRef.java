package dev.openclosed.squall.api.spec;

import java.util.Objects;
import java.util.Optional;

/**
 * A reference to a schema object.
 */
public interface SchemaObjectRef {

    default Optional<String> databaseName() {
        return Optional.empty();
    }

    default Optional<String> schemaName() {
        return Optional.empty();
    }

    String objectName();

    static SchemaObjectRef schemaQualified(String objectName, String schemaName) {
        Objects.requireNonNull(objectName);
        Objects.requireNonNull(schemaName);
        record SchemaQualified(String objectName, Optional<String> schemaName) implements SchemaObjectRef {
        }
        return new SchemaQualified(objectName, Optional.of(schemaName));
    }
}
