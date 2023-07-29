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
import dev.openclosed.squall.api.spec.Table;

import java.util.List;
import java.util.ResourceBundle;

enum ColumnCellProvider implements CellProvider<Column, Table> {
    ORDINAL(ALIGN_RIGHT) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
            return String.valueOf(ordinal);
        }
    },
    NAME(ALIGN_LEFT) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
            var sb = new StringBuilder();
            sb.append('`').append(column.name()).append('`');
            if (column.isPrimaryKey()) {
                sb.append(' ').append(KEY_MARK);
            }
            return sb.toString();
        }
    },
    LABEL(ALIGN_LEFT) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
            return column.label().orElse("-");
        }
    },
    DATA_TYPE(ALIGN_LEFT) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
            return column.typeName();
        }
    },
    PRECISION_LENGTH(ALIGN_RIGHT) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
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
        public String getValue(Column column, Table table, int ordinal) {
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
        public String getValue(Column column, Table table, int ordinal) {
            return column.isNullable() ? CHECK_MARK : "-";
        }
    },
    REQUIRED(ALIGN_CENTER) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
            return column.isRequired() ? CHECK_MARK : "-";
        }
    },
    UNIQUE(ALIGN_CENTER) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
            return column.isUnique() ? CHECK_MARK : "-";
        }
    },
    DEFAULT_VALUE(ALIGN_LEFT) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
            return column.defaultValue()
                .map(value -> new StringBuilder()
                    .append('`').append(value).append('`').toString()
                ).orElse("-");
        }
    },
    DESCRIPTION(ALIGN_LEFT) {
        @Override
        public String getValue(Column column, Table table, int ordinal) {
            return column.description()
                .map(ColumnCellProvider::inlined)
                .orElse("-");
        }
    };

    private static final String KEY_MARK = ":key:";
    private static final String CHECK_MARK = "&#x2713;";

    private final String separator;

    ColumnCellProvider(String separator) {
        this.separator = separator;
    }

    @Override
    public String getSeparator() {
        return this.separator;
    }

    private static String inlined(String text) {
        return text.replaceAll("\\n+", " ");
    }

    static ColumnCellProvider provider(ColumnAttribute attribute) {
        return switch (attribute) {
            case ORDINAL -> ORDINAL;
            case NAME -> NAME;
            case LABEL -> LABEL;
            case DATA_TYPE -> DATA_TYPE;
            case PRECISION_LENGTH -> PRECISION_LENGTH;
            case SCALE -> SCALE;
            case NULLABLE -> NULLABLE;
            case REQUIRED -> REQUIRED;
            case UNIQUE -> UNIQUE;
            case DEFAULT_VALUE -> DEFAULT_VALUE;
            case DESCRIPTION -> DESCRIPTION;
        };
    }

    static MarkdownTableWriter<Column, Table> tableWriter(ResourceBundle bundle, List<ColumnAttribute> attributes) {
        List<ColumnCellProvider> providers = attributes.stream()
            .map(ColumnCellProvider::provider).toList();
        return MarkdownTableWriter.withProviders(providers, bundle);
    }
}
