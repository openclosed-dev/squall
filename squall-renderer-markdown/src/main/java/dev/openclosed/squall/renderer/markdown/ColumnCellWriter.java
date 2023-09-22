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
import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.DocAnnotationType;
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.ForeignKey;
import dev.openclosed.squall.api.spec.Table;

import java.util.stream.Collectors;

enum ColumnCellWriter implements CellWriter<Column> {
    ORDINAL(ALIGN_RIGHT) {
        @Override
        String getValue(Column column, int rowNo, RenderContext context) {
            return String.valueOf(rowNo);
        }
    },
    NAME(ALIGN_LEFT) {
        @Override
        public void writeValue(Column column, int rowNo, Appender appender, RenderContext context) {
            if (column.isDeprecated()) {
                appender.append("~~").append(column.name()).append("~~");
            } else {
                appender.append(column.name());
            }
            if (column.isPrimaryKey()) {
                appender.appendSpace().append(KEY_MARK);
            }
        }
    },
    LABEL(ALIGN_LEFT) {
        @Override
        public void writeValue(Column column, int rowNo, Appender appender, RenderContext context) {
            if (column.label().isPresent()) {
                var label = column.label().get();
                if (column.isDeprecated()) {
                    appender.append("~~").append(label).append("~~");
                } else {
                    appender.append(label);
                }
            } else {
                appender.append("-");
            }
        }
    },
    TYPE(ALIGN_LEFT) {
        @Override
        String getValue(Column column, int rowNo, RenderContext context) {
            return column.toSqlType();
        }
    },
    TYPE_NAME(ALIGN_LEFT) {
        @Override
        String getValue(Column column, int rowNo, RenderContext context) {
            return column.typeName();
        }
    },
    PRECISION_LENGTH(ALIGN_RIGHT) {
        @Override
        String getValue(Column column, int rowNo, RenderContext context) {
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
        String getValue(Column column, int rowNo, RenderContext context) {
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
        String getValue(Column column, int rowNo, RenderContext context) {
            return column.isNullable() ? CHECK_MARK : "-";
        }
    },
    REQUIRED(ALIGN_CENTER) {
        @Override
        String getValue(Column column, int rowNo, RenderContext context) {
            return column.isRequired() ? CHECK_MARK : "-";
        }
    },
    UNIQUE(ALIGN_CENTER) {
        @Override
        String getValue(Column column, int rowNo, RenderContext context) {
            return column.isUnique() ? CHECK_MARK : "-";
        }
    },
    DEFAULT_VALUE(ALIGN_LEFT) {
        @Override
        String getValue(Column column, int rowNo, RenderContext context) {
            return column.defaultValue()
                .map(ColumnCellWriter::expressionToCode)
                .orElse("-");
        }
    },
    FOREIGN_KEY(ALIGN_LEFT) {
        @Override
        String getValue(Column column, int rowNo, RenderContext context) {
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
                .append(foreignKey.qualifiedTableName())
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
        public void writeValue(Column column, int rowNo, Appender appender, RenderContext context) {
            if (column.isDeprecated()) {
                writeDeprecationNote(column, appender, context);
                column.description().ifPresent(
                    description -> appender.append("<br>").append(inlined(description))
                );
            } else {
                column.description().ifPresentOrElse(
                    description -> appender.append(inlined(description)),
                    () -> appender.append('-')
                );
            }
        }

        private void writeDeprecationNote(Column column, Appender appender, RenderContext context) {
            appender.append("**").append(context.bundle().deprecated()).append("**");
            String notice = column.getFirstAnnotation(DocAnnotationType.DEPRECATED).get().value();
            if (!notice.isEmpty()) {
                appender.appendSpace().append(notice);
            }
        }
    };

    private static final String KEY_MARK = "&#x1F511;";
    private static final String CHECK_MARK = "&#x2705;";

    private final String separator;

    ColumnCellWriter(String separator) {
        this.separator = separator;
    }

    @Override
    public String getSeparator() {
        return this.separator;
    }

    @Override
    public void writeValue(Column component, int rowNo, Appender appender, RenderContext context) {
        appender.append(getValue(component, rowNo, context));
    }

    String getValue(Column column, int rowNo, RenderContext context) {
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

    static ColumnCellWriter writing(ColumnAttribute attribute) {
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
