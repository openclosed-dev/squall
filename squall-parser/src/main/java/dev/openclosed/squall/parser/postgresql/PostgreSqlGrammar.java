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

package dev.openclosed.squall.parser.postgresql;

import dev.openclosed.squall.api.sql.datatype.DataType;
import dev.openclosed.squall.api.sql.expression.FunctionCall;
import dev.openclosed.squall.api.sql.expression.SequenceFunctionCall;
import dev.openclosed.squall.api.sql.expression.StringLiteral;
import dev.openclosed.squall.api.sql.annotation.DocAnnotation;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.expression.Typecast;
import dev.openclosed.squall.parser.basic.IsPredicate;
import dev.openclosed.squall.api.sql.datatype.StandardDataType;
import dev.openclosed.squall.parser.basic.IdentifierType;
import dev.openclosed.squall.parser.basic.SqlGrammar;
import dev.openclosed.squall.parser.basic.Token;

import java.util.List;
import java.util.Map;

/**
 * Grammar for PostgreSQL.
 */
interface PostgreSqlGrammar extends SqlGrammar, PostgreSqlPredicates {

    Map<String, DataType> SUPPORTED_DATA_TYPES = PostgreSqlDataType.valuesAsMap();

    @Override
    default void createUnknownSchemaObject(List<DocAnnotation> annotations) {
        if (next() == PostgreSqlKeyword.DATABASE) {
            createDatabase(annotations);
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
    default Expression keywordBinaryOperator(Expression leftOperand, int rightPrecedence) {
        PostgreSqlKeyword keyword = (PostgreSqlKeyword) expectKeyword();
        return switch (keyword) {
            case ISNULL -> IsPredicate.IS_NULL.toExpression(leftOperand);
            case NOTNULL -> IsPredicate.IS_NOT_NULL.toExpression(leftOperand);
            default -> SqlGrammar.super.keywordBinaryOperator(leftOperand, rightPrecedence);
        };
    }

    @Override
    default Expression symbolBinaryOperator(Expression leftOperand, int rightPrecedence) {
        Token token = next();
        if (token == OperatorSymbol.DOUBLE_COLON) {
            consume();
            return Typecast.of(leftOperand, dataType());
        }
        return SqlGrammar.super.symbolBinaryOperator(leftOperand, rightPrecedence);
    }

    @Override
    default FunctionCall functionCall(String functionName) {
        FunctionCall functionCall = SqlGrammar.super.functionCall(functionName);

        String name = functionName.toLowerCase();
        if (!name.equals("nextval") && !name.equals("currval")) {
            return functionCall;
        }

        var arguments = functionCall.arguments();
        if (arguments.size() == 1) {
            var argument = arguments.get(0);
            if (argument instanceof Typecast typecast) {
                argument = typecast.source();
            }
            if (argument instanceof StringLiteral literal) {
                return SequenceFunctionCall.of(
                    functionCall.name(),
                    functionCall.arguments(),
                    resolver().resolveDotNotation(literal.value())
                );
            }
        }

        return functionCall;
    }
}
