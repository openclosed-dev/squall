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

package dev.openclosed.squall.core.parser.postgresql;

import dev.openclosed.squall.api.spec.DataType;
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.core.spec.StandardDataType;
import dev.openclosed.squall.core.parser.IdentifierType;
import dev.openclosed.squall.core.parser.SqlGrammar;
import dev.openclosed.squall.core.parser.Token;
import dev.openclosed.squall.core.spec.expression.Expressions;

import java.util.Map;

/**
 * Grammar for PostgreSQL.
 */
interface PostgreSqlGrammar extends SqlGrammar, PostgreSqlPredicates {

    Map<String, DataType> SUPPORTED_DATA_TYPES = PostgreSqlDataType.valuesAsMap();

    @Override
    default void createUnknownSchemaObject() {
        if (next() == PostgreSqlKeyword.DATABASE) {
            createDatabase();
        }
    }

    @Override
    default DataType keywordDataType() {
        var token = next();
        if (token instanceof PostgreSqlKeyword keyword) {
            switch (keyword) {
                case BIT -> {
                    consume();
                    return characterDataType(StandardDataType.BIT);
                }
                case TEXT -> {
                    consume();
                    // the type text is not in the SQL standard
                    return PostgreSqlDataType.TEXT;
                }
                default -> { }
            }
        }
        return SqlGrammar.super.keywordDataType();
    }

    @Override
    default DataType nonKeywordDataType() {
        var token = next();
        if (token.isIdentifier(IdentifierType.TYPE_NAME)) {
            var typeName = token.toIdentifier();
            if (SUPPORTED_DATA_TYPES.containsKey(typeName)) {
                consume();
                return SUPPORTED_DATA_TYPES.get(typeName);
            }
        }
        return SqlGrammar.super.nonKeywordDataType();
    }

    @Override
    default void checkConstraintModifier() {
        if (next() == PostgreSqlKeyword.NO) {
            consume();
            expect(PostgreSqlKeyword.INHERIT);
            consume();
        }
    }

    @Override
    default Expression postfixOperator(Expression operand) {
        Token token = next();
        if (token == OperatorSymbol.DOUBLE_COLON) {
            consume();
            return Expressions.createTypecast(operand, dataType());
        }
        return SqlGrammar.super.postfixOperator(operand);
    }
}
