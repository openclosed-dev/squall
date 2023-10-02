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
import dev.openclosed.squall.api.sql.expression.SequenceFunctionCall;
import dev.openclosed.squall.api.sql.spec.Column;
import dev.openclosed.squall.api.sql.spec.DocAnnotationType;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.spec.ForeignKey;
import dev.openclosed.squall.api.sql.spec.Table;

import java.util.stream.Collectors;

enum ColumnAttributeWriter implements AttributeWriter<Column> {
    ORDINAL(">.^2") {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return String.valueOf(rowNo);
        }
    },
    NAME("<.^8") {
        @Override
        public void writeValue(Column column, int rowNo, DocBuilder builder, WriterContext context) {
            if (column.isDeprecated()) {
                builder.append("[.line-through]#").append(column.name()).append("#");
            } else {
                builder.append(column.name());
            }
            if (column.isPrimaryKey()) {
                builder.appendSpace().append(Icons.KEY);
            }
        }
    },
    LABEL("<.^8") {
        @Override
        public void writeValue(Column column, int rowNo, DocBuilder builder, WriterContext context) {
            if (column.label().isPresent()) {
                var label = column.label().get();
                if (column.isDeprecated()) {
                    builder.append("[.line-through]#").append(label).append('#');
                } else {
                    builder.append(label);
                }
            } else {
                builder.append('-');
            }
        }
    },
    TYPE("<.^6") {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.toSqlType();
        }
    },
    TYPE_NAME("<.^4") {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.typeName();
        }
    },
    PRECISION_LENGTH(">.^3") {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
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
        String getValue(Column column, int rowNo, WriterContext context) {
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
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.isNullable() ? Icons.CHECK : "-";
        }
    },
    REQUIRED("^.^3") {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.isRequired() ? Icons.CHECK : "-";
        }
    },
    UNIQUE("^.^3") {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.isUnique() ? Icons.CHECK : "-";
        }
    },
    DEFAULT_VALUE("<.^6") {
        @Override
        public void writeValue(Column column, int rowNo, DocBuilder builder, WriterContext context) {
            column.defaultValue().ifPresentOrElse(
                value -> writeDefault(value, builder),
                () -> builder.append('-')
            );
        }

        private void writeDefault(Expression value, DocBuilder builder) {
            if (value instanceof SequenceFunctionCall sequence) {
                writeSequenceFunction(sequence, builder);
            } else {
                builder.appendCode(value.toSql());
            }
        }

        private void writeSequenceFunction(SequenceFunctionCall sequence, DocBuilder builder) {
            builder.append(sequence.name())
                .append('(')
                .append("<<_").append(sequence.fullSequenceName())
                .append(",").append(sequence.simpleSequenceName())
                .append(">>)");
        }
    },
    FOREIGN_KEY("<.^6") {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            final String columnName = column.name();
            Table table = context.currentTable();
            String value = table.foreignKeysContaining(columnName)
                .map(fk -> foreignKeyToString(fk, columnName))
                .distinct()
                .collect(Collectors.joining(HARD_BREAK));
            return value.isEmpty() ? "-" : value;
        }

        private static String foreignKeyToString(ForeignKey foreignKey, String columnName) {
            String targetColumn = foreignKey.columnMapping().get(columnName);
            String fullTableName = foreignKey.fullTableName();
            return new StringBuilder()
                .append("<<_").append(fullTableName)
                .append(',').append(foreignKey.simpleTableName())
                .append(">> (<<_")
                .append(fullTableName).append('.').append(targetColumn)
                .append(",").append(targetColumn)
                .append(">>)")
                .toString();
        }
    },
    DESCRIPTION("<.<12a") {
        @Override
        public void writeValue(Column column, int rowNo, DocBuilder builder, WriterContext context) {
            if (column.isDeprecated()) {
                writeDeprecationNode(column, builder, context);
                column.description().ifPresent(
                    description -> builder.appendHardLineBreak().appendMarkdownText(description)
                );
            } else {
                column.description().ifPresentOrElse(
                    builder::appendMarkdownText,
                    () -> builder.append('-')
                );
            }
        }

        private static void writeDeprecationNode(Column column, DocBuilder builder, WriterContext context) {
            var bundle = context.bundle();
            builder.append("*").append(bundle.deprecated()).append("*");
            String notice = column.getFirstAnnotation(DocAnnotationType.DEPRECATED).get().value();
            if (!notice.isEmpty()) {
                builder.appendSpace().appendMarkdownText(notice);
            }
        }
    };

    private static final String HARD_BREAK = " +\n";

    private final String specifier;

    ColumnAttributeWriter(String specifier) {
        this.specifier = specifier;
    }

    @Override
    public final String specifier() {
        return this.specifier;
    }

    @Override
    public void writeValue(Column column, int rowNo, DocBuilder builder, WriterContext context) {
        builder.append(getValue(column, rowNo, context));
    }

    String getValue(Column column, int rowNo, WriterContext context) {
        return "-";
    }

    private static String expressionToCode(Expression expression) {
        return "`" + expression.toSql() + "`";
    }

    static ColumnAttributeWriter writing(ColumnAttribute attribute) {
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
