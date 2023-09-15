/*
 * Copyright 2022-2023 The Squall Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.openclosed.squall.renderer.asciidoc;

import dev.openclosed.squall.api.renderer.ColumnAttribute;
import dev.openclosed.squall.api.renderer.MessageBundle;
import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.DocAnnotationType;
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.ForeignKey;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.api.spec.Table;

import java.util.stream.Collectors;

enum ColumnCellProvider implements CellProvider<Column> {
    ORDINAL(">.^2") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            return String.valueOf(ordinal);
        }
    },
    NAME("<.^8") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            var sb = new StringBuilder();
            if (column.isDeprecated()) {
                sb.append("[.line-through]#").append(column.name()).append("#");
            } else {
                sb.append(column.name());
            }
            if (column.isPrimaryKey()) {
                sb.append(' ').append(Icons.KEY);
            }
            return sb.toString();
        }
    },
    LABEL("<.^8") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            return column.label().map(label -> {
                if (column.isDeprecated()) {
                    return "[.line-through]#" + label + "#";
                } else {
                    return label;
                }
            }).orElse("-");
        }
    },
    TYPE("<.^6") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            return column.toSqlType();
        }
    },
    TYPE_NAME("<.^4") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            return column.typeName();
        }
    },
    PRECISION_LENGTH(">.^3") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            var value = column.precision();
            if (value.isPresent()) {
                return String.valueOf(value.getAsInt());
            } else {
                value = column.length();
                if (value.isPresent()) {
                    return String.valueOf(value.getAsInt());
                } else {
                    return "-";
                }
            }
        }
    },
    SCALE(">.^3") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            var value = column.scale();
            if (value.isPresent()) {
                return String.valueOf(value.getAsInt());
            } else {
                return "-";
            }
        }
    },
    NULLABLE("^.^3") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            return column.isNullable() ? Icons.CHECK : "-";
        }
    },
    REQUIRED("^.^3") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            return column.isRequired() ? Icons.CHECK : "-";
        }
    },
    UNIQUE("^.^3") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            return column.isUnique() ? Icons.CHECK : "-";
        }
    },
    DEFAULT_VALUE("<.^6") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            return column.defaultValue()
                .map(ColumnCellProvider::expressionToCode)
                .orElse("-");
        }
    },
    FOREIGN_KEY("<.^6") {
        @Override
        public String getValue(Column column, int ordinal, SpecVisitor.Context context) {
            final String columnName = column.name();
            Table table = context.currentTable();
            String value = table.foreignKeysContaining(columnName)
                .map(fk -> foreignKeyToString(fk, columnName))
                .distinct()
                .collect(Collectors.joining("<br>"));
            return value.isEmpty() ? "-" : value;
        }

        private static String foreignKeyToString(ForeignKey foreignKey, String columnName) {
            String targetColumn = foreignKey.columnMapping().get(columnName);
            String fullTableName = foreignKey.fullTableName();
            return new StringBuilder()
                .append("<<").append(fullTableName)
                .append(',').append(foreignKey.qualifiedTableName())
                .append(">> (<<")
                .append(fullTableName).append('.').append(targetColumn)
                .append(",").append(targetColumn)
                .append(">>)")
                .toString();
        }
    },
    DESCRIPTION("<.<12a") {
        @Override
        public String getLocalizedValue(
            Column column, int ordinal, SpecVisitor.Context context, MessageBundle bundle) {
            return column.description()
                .map(d -> withDeprecationNotice(d, column, bundle))
                .orElse("-");
        }

        private static String withDeprecationNotice(String description, Column column, MessageBundle bundle) {
            if (!column.isDeprecated()) {
                return description;
            }
            var sb = new StringBuilder();
            sb.append("*").append(bundle.deprecated()).append("*");
            String notice = column.getFirstAnnotation(DocAnnotationType.DEPRECATED).get().value();
            if (!notice.isEmpty()) {
                sb.append(' ').append(notice);
            }
            sb.append(" +\n").append(description);
            return sb.toString();
        }
    };

    private final String specifier;

    ColumnCellProvider(String specifier) {
        this.specifier = specifier;
    }

    @Override
    public final String specifier() {
        return this.specifier;
    }

    private static String expressionToCode(Expression expression) {
        return "`" + expression.toSql() + "`";
    }

    static ColumnCellProvider provider(ColumnAttribute attribute) {
        // Mapping of ColumnAttribute to this type.
        return switch (attribute) {
            case ORDINAL -> ORDINAL;
            case NAME -> NAME;
            case LABEL -> LABEL;
            case TYPE -> TYPE;
            case TYPE_NAME -> TYPE_NAME;
            case PRECISION_LENGTH -> PRECISION_LENGTH;
            case SCALE -> SCALE;
            case NULLABLE -> NULLABLE;
            case REQUIRED -> REQUIRED;
            case UNIQUE -> UNIQUE;
            case DEFAULT_VALUE -> DEFAULT_VALUE;
            case FOREIGN_KEY -> FOREIGN_KEY;
            case DESCRIPTION -> DESCRIPTION;
        };
    }
}
