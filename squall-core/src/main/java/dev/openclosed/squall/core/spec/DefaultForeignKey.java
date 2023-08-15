package dev.openclosed.squall.core.spec;

import dev.openclosed.squall.api.spec.ForeignKey;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record DefaultForeignKey(
    Optional<String> constraintName,
    String tableName,
    List<String> tableParents,
    Map<String, String> columnMapping
    ) implements ForeignKey {

    public DefaultForeignKey {
        tableParents = List.copyOf(tableParents);
        columnMapping = Map.copyOf(columnMapping);
    }

    @Override
    public String qualifiedTableName() {
        var b = new StringBuilder();
        var parents = tableParents();
        for (int i = parents.size() - 1; i >= 0; i--) {
            var parent = parents.get(i);
            if (!parent.isEmpty()) {
                b.append(parent).append('.');
                break;
            }
        }
        return b.append(tableName()).toString();
    }

    @Override
    public String fullTableName() {
        return getFullName(tableName());
    }

    private String getFullName(String... names) {
        return Stream.concat(tableParents.stream(), Stream.of(names))
            .collect(Collectors.joining("."));
    }
}
