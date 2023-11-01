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

package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.renderer.ColumnAttribute;
import dev.openclosed.squall.api.sql.annotation.Deprecated;
import dev.openclosed.squall.api.sql.spec.Column;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.spec.ForeignKey;
import dev.openclosed.squall.api.sql.spec.Table;

import java.util.stream.Collectors;

enum ColumnAttributeWriter implements AttributeWriter<Column> {
    ORDINAL(ALIGN_RIGHT) {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return String.valueOf(rowNo);
        }
    },
    NAME(ALIGN_LEFT) {
        @Override
        public void writeValue(Column column, int rowNo, DocBuilder builder, WriterContext context) {
            if (column.isDeprecated()) {
                builder.append("~~").append(column.name()).append("~~");
            } else {
                builder.append(column.name());
            }
            if (column.isPrimaryKey()) {
                builder.appendSpace().append(KEY_MARK);
            }
        }
    },
    LABEL(ALIGN_LEFT) {
        @Override
        public void writeValue(Column column, int rowNo, DocBuilder builder, WriterContext context) {
            if (column.label().isPresent()) {
                var label = column.label().get();
                if (column.isDeprecated()) {
                    builder.append("~~").append(label).append("~~");
                } else {
                    builder.append(label);
                }
            } else {
                builder.append("-");
            }
        }
    },
    TYPE(ALIGN_LEFT) {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.toSqlType();
        }
    },
    TYPE_NAME(ALIGN_LEFT) {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.typeName();
        }
    },
    PRECISION_LENGTH(ALIGN_RIGHT) {
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
    SCALE(ALIGN_RIGHT) {
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
    NULLABLE(ALIGN_CENTER) {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.isNullable() ? CHECK_MARK : "-";
        }
    },
    REQUIRED(ALIGN_CENTER) {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.isRequired() ? CHECK_MARK : "-";
        }
    },
    UNIQUE(ALIGN_CENTER) {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.isUnique() ? CHECK_MARK : "-";
        }
    },
    DEFAULT_VALUE(ALIGN_LEFT) {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
            return column.defaultValue()
                .map(ColumnAttributeWriter::expressionToCode)
                .orElse("-");
        }
    },
    FOREIGN_KEY(ALIGN_LEFT) {
        @Override
        String getValue(Column column, int rowNo, WriterContext context) {
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
                .append(foreignKey.simpleTableName())
                .append(" ([")
                .append(targetColumn)
                .append("](#")
                .append(fullTableName)
                .append('.')
                .append(targetColumn)
                .append("))")
                .toString();
        }
    },
    DESCRIPTION(ALIGN_LEFT) {
        @Override
        public void writeValue(Column column, int rowNo, DocBuilder builder, WriterContext context) {
            if (column.isDeprecated()) {
                writeDeprecationNote(column, builder, context);
                column.description().ifPresent(
                    description -> builder.append("<br>").append(inlined(description))
                );
            } else {
                column.description().ifPresentOrElse(
                    description -> builder.append(inlined(description)),
                    () -> builder.append('-')
                );
            }
        }

        private void writeDeprecationNote(Column column, DocBuilder builder, WriterContext context) {
            builder.append("**").append(context.bundle().deprecated()).append("**");
            String notice = column.getFirstAnnotationOf(Deprecated.class).get().value();
            if (!notice.isEmpty()) {
                builder.appendSpace().append(notice);
            }
        }
    };

    private static final String KEY_MARK = "&#x1F511;";
    private static final String CHECK_MARK = "&#x2705;";

    private final String separator;

    ColumnAttributeWriter(String separator) {
        this.separator = separator;
    }

    @Override
    public String getSeparator() {
        return this.separator;
    }

    @Override
    public void writeValue(Column component, int rowNo, DocBuilder builder, WriterContext context) {
        builder.append(getValue(component, rowNo, context));
    }

    String getValue(Column column, int rowNo, WriterContext context) {
        return "-";
    }

    private static String inlined(String text) {
        return text.replaceAll("\\n+", " ");
    }

    private static String expressionToCode(Expression expression) {
        return new StringBuilder()
            .append('`').append(expression.toSql()).append('`')
            .toString();
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
