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

package dev.openclosed.squall.core.parser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.DataType;
import dev.openclosed.squall.api.spec.IntegerDataType;
import dev.openclosed.squall.api.spec.TableRef;
import dev.openclosed.squall.core.spec.StandardDataType;
import dev.openclosed.squall.core.spec.expression.Expressions;

/**
 * Standard SQL grammar.
 */
public interface SqlGrammar extends SqlGrammarEntry, SqlGrammarSupport, SqlPredicates, SqlHandler {

    @Override
    default void statements() {
        while (next() != Token.EOI) {
            withRecovery(this::statement);
            consume();
        }
    }

    @Override
    default Expression expression() {
        return expression(OperatorGroup.LOWEST_PRECEDENCE);
    }

    default void statement() {
        var token = next();
        if (token instanceof Keyword keyword) {
            switch (keyword.standard()) {
                case CREATE -> createStatement();
                case ALTER -> alterStatement();
                default -> { }
            }
            find(SpecialSymbol.SEMICOLON);
        } else if (token != SpecialSymbol.SEMICOLON) {
            syntaxError(token);
        }
    }

    default void createStatement() {
        expect(StandardKeyword.CREATE);
        consume();

        Set<Keyword> modifiers = new HashSet<>();
        Keyword keyword = expectKeyword();
        while (testSchemaObjectModifier(keyword)) {
            modifiers.add(keyword);
            consume();
            keyword = expectKeyword();
        }

        switch (keyword.standard()) {
            case SCHEMA -> createSchema();
            case TABLE -> createTable();
            case SEQUENCE -> createSequence();
            case VIEW -> { }
            default -> createUnknownSchemaObject();
        }
    }

    default void alterStatement() {
        expect(StandardKeyword.ALTER);
        consume();
        Keyword keyword = expectKeyword();
        switch (keyword.standard()) {
            case TABLE -> alterTable();
            default -> { }
        }
    }

    default void createUnknownSchemaObject() {
    }

    default void createDatabase() {
        consume(); // DATABASE
        var databaseName = expectIdentifier(IdentifierType.OBJECT_NAME);
        consume();
        handleDatabase(databaseName);
    }

    default void createSchema() {
        expect(StandardKeyword.SCHEMA);
        consume();
        if (next().isSameAs(StandardKeyword.IF)) {
            ifNotExists();
        }
        var schemaName = expectIdentifier(IdentifierType.OBJECT_NAME);
        consume();
        handleSchema(schemaName);
    }

    default void ifExists() {
        expect(StandardKeyword.IF);
        consume();
        expect(StandardKeyword.EXISTS);
        consume();
    }

    /**
     * Parses IF NOT EXISTS, which is optional.
     */
    default void ifNotExists() {
        expect(StandardKeyword.IF);
        consume();
        expect(StandardKeyword.NOT);
        consume();
        expect(StandardKeyword.EXISTS);
        consume();
    }

    default void createSequence() {
        expect(StandardKeyword.SEQUENCE);
        consume();
        if (next().isSameAs(StandardKeyword.IF)) {
            ifNotExists();
        }
        String[] name = schemaObjectName();
        handleSequence(name[0], name[1]);

        Token token = next();
        while (token != SpecialSymbol.SEMICOLON) {
            if (token instanceof Keyword keyword) {
                switch (keyword.standard()) {
                    case AS -> sequenceDataType();
                    case INCREMENT -> sequenceIncrement();
                    case START -> sequenceStart();
                    case MAXVALUE -> sequenceMaxValue();
                    case MINVALUE -> sequenceMinValue();
                    case CYCLE -> consume();
                    case NO -> {
                        consume();
                        keyword = expectKeyword();
                        switch (keyword.standard()) {
                            case CYCLE, MAXVALUE, MINVALUE -> consume();
                            default -> syntaxError(keyword);
                        }
                    }
                    default -> consume();
                }
            } else {
                consume();
            }
            token = next();
        }
    }

    default void sequenceDataType() {
        expect(StandardKeyword.AS);
        consume();
        Keyword keyword = expectKeyword();
        IntegerDataType dataType = switch (keyword.standard()) {
            case BIGINT -> IntegerDataType.BIGINT;
            case SMALLINT -> IntegerDataType.SMALLINT;
            case INTEGER -> IntegerDataType.INTEGER;
            default -> {
                syntaxError(keyword);
                // never reach here
                yield null;
            }
        };
        this.handleSequenceDataType(dataType);
        consume();
    }

    default void sequenceStart() {
        expect(StandardKeyword.START);
        consume();
        if (next().isSameAs(StandardKeyword.WITH)) {
            consume();
        }
        handleSequenceStart(integerLiteral());
    }

    default void sequenceIncrement() {
        expect(StandardKeyword.INCREMENT);
        consume();
        if (next().isSameAs(StandardKeyword.BY)) {
            consume();
        }
        handleSequenceIncrement(integerLiteral());
    }

    default void sequenceMaxValue() {
        expect(StandardKeyword.MAXVALUE);
        consume();
        handleSequenceMaxValue(integerLiteral());
    }

    default void sequenceMinValue() {
        expect(StandardKeyword.MINVALUE);
        consume();
        handleSequenceMinValue(integerLiteral());
    }

    default void createTable() {
        expect(StandardKeyword.TABLE);
        consume();
        if (next().isSameAs(StandardKeyword.IF)) {
            ifNotExists();
        }
        String[] name = schemaObjectName();
        handleTable(name[0], name[1]);
        tableComponents();
    }

    default void alterTable() {
        expect(StandardKeyword.TABLE);
        consume();
        if (next().isSameAs(StandardKeyword.IF)) {
            ifExists();
        }
        if (next().isSameAs(StandardKeyword.ONLY)) {
            consume();
        }
        String[] name = schemaObjectName();
        handleTableToAlter(name[0], name[1]);

        for (;;) {
            Token token = next();
            if (token.isSameAs(StandardKeyword.ADD)) {
                addTableConstraint();
            }
            if (next() != SpecialSymbol.COMMA) {
                break;
            }
            consume();
        }
    }

    default void addTableConstraint() {
        expect(StandardKeyword.ADD);
        consume();
        String constraintName = constraintNameOrNull();
        Token token = next();
        if (token instanceof Keyword keyword) {
            switch (keyword.standard()) {
                case CHECK -> checkConstraint(constraintName);
                case PRIMARY -> tablePrimaryKey(constraintName);
                case UNIQUE -> tableUniqueConstraint(constraintName);
                case FOREIGN -> tableForeignKey(constraintName);
                default -> {
                }
            }
        }
    }

    default String[] schemaObjectName() {
        String schemaName;
        String tableName = expectIdentifier(IdentifierType.OBJECT_NAME);
        consume();
        if (next() == SpecialSymbol.PERIOD) {
            consume();
            schemaName = tableName;
            tableName = expectIdentifier(IdentifierType.OBJECT_NAME);
            consume();
        } else {
            schemaName = config().getDefaultSchema();
        }
        return new String[] {schemaName, tableName};
     }

    default void tableComponents() {
        expect(SpecialSymbol.OPEN_PAREN);
        consume();
        int i = 0;
        while (next() != SpecialSymbol.CLOSE_PAREN) {
            if (i++ > 0) {
                expect(SpecialSymbol.COMMA);
                consume();
            }
            tableComponent();
        }
        consume();
    }

    default void tableComponent() {
        String constraintName = constraintNameOrNull();
        var token = next();
        if (next() instanceof Keyword keyword) {
            switch (keyword.standard()) {
                case CHECK -> {
                    tableCheckConstraint(constraintName);
                    return;
                }
                case PRIMARY -> {
                    tablePrimaryKey(constraintName);
                    return;
                }
                case UNIQUE -> {
                    tableUniqueConstraint(constraintName);
                    return;
                }
                case FOREIGN -> {
                    tableForeignKey(constraintName);
                    return;
                }
                default -> { }
            }
        }

        if (constraintName != null) {
            syntaxError(token);
        } else {
            tableColumn();
        }
    }

    default String constraintNameOrNull() {
        if (next().isSameAs(StandardKeyword.CONSTRAINT)) {
            consume();
            String constraintName = expectIdentifier(IdentifierType.OBJECT_NAME);
            consume();
            return constraintName;
        } else {
            return null;
        }
    }

    default void tableCheckConstraint(String constraintName) {
        checkConstraint(constraintName);
    }

    default void tablePrimaryKey(String constraintName) {
        expect(StandardKeyword.PRIMARY);
        consume();
        expect(StandardKeyword.KEY);
        consume();
        var columns = columnNameList();
        commonConstraintModifiers();
        handleTablePrimaryKey(constraintName, columns);
    }

    default void tableUniqueConstraint(String constraintName) {
        expect(StandardKeyword.UNIQUE);
        consume();
        if (next().isSameAs(StandardKeyword.NULLS)) {
            nullsDistinct();
        }
        var columns = columnNameList();
        commonConstraintModifiers();
        handleTableUniqueConstraint(constraintName, columns);
    }

    default void tableForeignKey(String constraintName) {
        expect(StandardKeyword.FOREIGN);
        consume();
        expect(StandardKeyword.KEY);
        consume();
        var columns = columnNameList();
        references(constraintName, columns);
    }

    default void commonConstraintModifiers() {
        var token = next();
        while (token instanceof Keyword keyword) {
            switch (keyword.standard()) {
                case DEFERRABLE -> consume();
                case NOT -> {
                    consume();
                    expect(StandardKeyword.DEFERRABLE);
                    consume();
                }
                case INITIALLY -> {
                    consume();
                    keyword = expectKeyword();
                    switch (keyword.standard()) {
                        case DEFERRED, IMMEDIATE -> {
                            consume();
                        }
                        default -> syntaxError(keyword);
                    }
                }
                default -> {
                    return;
                }
            }
            token = next();
        }
    }

    default void match() {
        expect(StandardKeyword.MATCH);
        consume();
        var keyword = expectKeyword();
        switch (keyword.standard()) {
            case FULL, PARTIAL, SIMPLE -> {
                consume();
            }
            default -> syntaxError(keyword);
        }
    }

    default void foreignKeyEvent() {
        expect(StandardKeyword.ON);
        consume();
        var keyword = expectKeyword();
        switch (keyword.standard()) {
            case DELETE, UPDATE -> referentialAction();
            default -> syntaxError(keyword);
        }
    }

    default void referentialAction() {
        consume();
        var keyword = expectKeyword();
        switch (keyword.standard()) {
            case NO -> {
                consume();
                expect(StandardKeyword.ACTION);
                consume();
            }
            case RESTRICT -> {
                consume();
            }
            case CASCADE -> {
                consume();
            }
            case SET -> {
                consume();
                keyword = expectKeyword();
                switch (keyword.standard()) {
                    case NULL, DEFAULT -> {
                        consume();
                        if (next() == SpecialSymbol.OPEN_PAREN) {
                            columnNameList();
                        }
                    }
                    default -> syntaxError(keyword);
                }
            }
            default -> syntaxError(keyword);
        }
    }

    default List<String> columnNameList() {
        var columns = new ArrayList<String>();
        expect(SpecialSymbol.OPEN_PAREN);
        consume();
        columns.add(expectIdentifier(IdentifierType.OBJECT_NAME));
        consume();
        while (next() == SpecialSymbol.COMMA) {
            consume();
            columns.add(expectIdentifier(IdentifierType.OBJECT_NAME));
            consume();
        }
        expect(SpecialSymbol.CLOSE_PAREN);
        consume();
        return columns;
    }

    default void tableColumn() {
        String columnName = expectIdentifier(IdentifierType.OBJECT_NAME);
        consume();
        var dataType = dataType();
        handleColumn(columnName, dataType);
        columnConstraints(columnName);
    }

    default DataType dataType() {
        if (next().isKeyword()) {
            return keywordDataType();
        } else {
            return nonKeywordDataType();
        }
    }

    default void columnConstraints(String columnName) {
        var token = next();
        while (token != SpecialSymbol.COMMA && token != SpecialSymbol.CLOSE_PAREN) {
            columnConstraint(columnName);
            token = next();
        }
    }

    default void columnConstraint(String columnName) {
        Token token = expectKeyword();
        String constraintName = null;
        if (token.isSameAs(StandardKeyword.CONSTRAINT)) {
            consume();
            token = next();
            constraintName = token.toIdentifier();
            consume();
            token = expectKeyword();
        }

        Keyword keyword = expectKeyword();
        switch (keyword.standard()) {
            case CHECK -> {
                checkConstraint(constraintName);
            }
            case DEFAULT -> {
                consume();
                commonConstraintModifiers();
                handleColumnDefaultValue(defaultValue());
            }
            case NOT -> {
                consume();
                expect(StandardKeyword.NULL);
                consume();
                commonConstraintModifiers();
                handleColumnNullable(false);
            }
            case NULL -> {
                consume();
                commonConstraintModifiers();
                handleColumnNullable(true);
            }
            case PRIMARY -> {
                consume();
                expect(StandardKeyword.KEY);
                consume();
                commonConstraintModifiers();
                handleTablePrimaryKey(constraintName, List.of(columnName));
            }
            case UNIQUE -> {
                consume();
                if (next().isSameAs(StandardKeyword.NULLS)) {
                    nullsDistinct();
                }
                commonConstraintModifiers();
                handleTableUniqueConstraint(constraintName, List.of(columnName));
            }
            case REFERENCES -> references(constraintName, List.of(columnName));
            default -> {
                // keyword may be UNDEFINED
                syntaxError(token);
            }
        }
    }

    default Expression defaultValue() {
        return expression(OperatorGroup.LOWEST_PRECEDENCE,
            token -> token.isSameAs(StandardKeyword.NOT));
    }

    default void references(String constraintName, List<String> columns) {
        expect(StandardKeyword.REFERENCES);
        consume();
        TableRef tableRef = tableReference();
        List<String> refColumns = Collections.emptyList();
        if (next() == SpecialSymbol.OPEN_PAREN) {
            refColumns = columnNameList();
        }
        var token = next();
        while (token.isKeyword()) {
            if (token.isSameAs(StandardKeyword.MATCH)) {
                match();
            } else if (token.isSameAs(StandardKeyword.ON)) {
                foreignKeyEvent();
            } else {
                break;
            }
            token = next();
        }
        commonConstraintModifiers();
        handleTableForeignKey(constraintName, tableRef, columns, refColumns);
    }

    default TableRef tableReference() {
        String firstPart;
        String secondPart = expectIdentifier(IdentifierType.OBJECT_NAME);
        consume();
        if (next() == SpecialSymbol.PERIOD) {
            consume();
            firstPart = secondPart;
            secondPart = expectIdentifier(IdentifierType.OBJECT_NAME);
            consume();
        } else {
            firstPart = config().getDefaultSchema();
        }
        return TableRef.tableInSchema(secondPart, firstPart);
    }

    default boolean nullsDistinct() {
        boolean nullsDistinct = true;
        expect(StandardKeyword.NULLS);
        consume();
        if (next().isSameAs(StandardKeyword.NOT)) {
            consume();
            nullsDistinct = false;
        }
        expect(StandardKeyword.DISTINCT);
        consume();
        return nullsDistinct;
    }

    default DataType keywordDataType() {
        return switch (expectKeyword().standard()) {
            case BIGINT -> {
                consume();
                yield IntegerDataType.BIGINT;
            }
            case BOOLEAN -> {
                consume();
                yield StandardDataType.BOOLEAN;
            }
            case CHAR -> {
                consume();
                yield dataTypeWithOptionalLength(StandardDataType.CHAR);
            }
            case CHARACTER -> {
                consume();
                yield characterDataType(StandardDataType.CHARACTER);
            }
            case DATE -> {
                consume();
                yield StandardDataType.DATE;
            }
            case DECIMAL -> {
                consume();
                yield numericDataType(StandardDataType.DECIMAL);
            }
            case DOUBLE -> {
                consume();
                expect(StandardKeyword.PRECISION);
                consume();
                yield StandardDataType.DOUBLE_PRECISION;
            }
            case FLOAT -> {
                consume();
                yield dataTypeWithOptionalPrecision(StandardDataType.FLOAT);
            }
            case INTEGER -> {
                consume();
                yield IntegerDataType.INTEGER;
            }
            case INTERVAL -> {
                consume();
                yield intervalDataType();
            }
            case NUMERIC -> {
                consume();
                yield numericDataType(StandardDataType.NUMERIC);
            }
            case REAL -> {
                consume();
                yield StandardDataType.REAL;
            }
            case SMALLINT -> {
                consume();
                yield IntegerDataType.SMALLINT;
            }
            case TIME -> {
                consume();
                yield timeDataType(StandardDataType.TIME);
            }
            case TIMESTAMP -> {
                consume();
                yield timeDataType(StandardDataType.TIMESTAMP);
            }
            case VARCHAR -> {
                consume();
                yield dataTypeWithOptionalLength(StandardDataType.VARCHAR);
            }
            default -> nonStandardKeywordDataType();
        };
    }

    default DataType nonStandardKeywordDataType() {
        syntaxError(next());
        return null;
    }

    default DataType nonKeywordDataType() {
        syntaxError(next());
        return null;
    }

    default DataType characterDataType(StandardDataType baseType) {
        assert baseType == StandardDataType.BIT || baseType == StandardDataType.CHARACTER;
        var token = next();
        if (token == SpecialSymbol.OPEN_PAREN) {
            return dataTypeWithLength(baseType);
        }
        if (token.isSameAs(StandardKeyword.VARYING)) {
            consume();
            baseType = baseType.varying();
        }
        return dataTypeWithOptionalLength(baseType);
    }

    default DataType dataTypeWithOptionalPrecision(StandardDataType baseType) {
        if (next() == SpecialSymbol.OPEN_PAREN) {
            final int precision = dataTypeArgument();
            return baseType.withPrecision(precision);
        }
        return baseType;
    }

    default DataType numericDataType(StandardDataType baseType) {
        if (next() != SpecialSymbol.OPEN_PAREN) {
            return baseType;
        }
        consume();
        var firstValue = expectType(TokenType.INTEGER).value();
        consume();
        int precision = ((BigInteger) firstValue).intValue();
        var token = next();
        if (token == SpecialSymbol.CLOSE_PAREN) {
            consume();
            return baseType.withPrecision(precision);
        } else if (token != SpecialSymbol.COMMA) {
            syntaxError(token);
            return null;
        }
        consume();

        var secondValue = expectType(TokenType.INTEGER).value();
        consume();
        final int scale = ((BigInteger) secondValue).intValue();

        expect(SpecialSymbol.CLOSE_PAREN);
        consume();

        return baseType.withPrecision(precision, scale);
    }

    default DataType dataTypeWithLength(StandardDataType baseType) {
        final var length = dataTypeArgument();
        return baseType.withLength(length);
    }

    default DataType dataTypeWithOptionalLength(StandardDataType baseType) {
        if (next() == SpecialSymbol.OPEN_PAREN) {
            return dataTypeWithLength(baseType);
        }
        return baseType;
    }

    default DataType timeDataType(StandardDataType baseType) {
        assert baseType == StandardDataType.TIME || baseType == StandardDataType.TIMESTAMP;
        if (next() == SpecialSymbol.OPEN_PAREN) {
            final int precision = dataTypeArgument();
            if (withOrWithoutTimeZone()) {
                baseType = baseType.withTimeZone();
            }
            return baseType.withPrecision(precision);
        } else if (withOrWithoutTimeZone()) {
            return baseType.withTimeZone();
        }
        return baseType;
    }

    /**
     * Parses "WITH/WITHOUT TIME ZONE".
     * @return {@code true} if time zone was specified.
     */
    default boolean withOrWithoutTimeZone() {
        var token = next();
        if (!token.isSameAs(StandardKeyword.WITH) && !token.isSameAs(StandardKeyword.WITHOUT)) {
            return false;
        }
        final boolean exists = (token.isSameAs(StandardKeyword.WITH));
        consume();

        expect(StandardKeyword.TIME);
        consume();

        expect(StandardKeyword.ZONE);
        consume();

        return exists;
    }

    default DataType intervalDataType() {
        var dataType = intervalFields(IntervalDataType.INTERVAL);
        if (next() == SpecialSymbol.OPEN_PAREN) {
            return dataType.withPrecision(dataTypeArgument());
        }
        return dataType;
    }

    default IntervalDataType intervalFields(final IntervalDataType baseType) {
        Token token = next();
        if (!token.isKeyword()) {
            return baseType;
        }

        var keyword = (Keyword) token;
        var dataType = baseType.from(keyword);
        if (dataType == baseType) {
            return baseType;
        } else {
            consume();
        }

        token = next();
        if (token.isSameAs(StandardKeyword.TO)) {
            consume();
            keyword = expectKeyword();
            try {
                dataType = dataType.to(keyword);
                consume();
            } catch (IllegalArgumentException e) {
                syntaxError(token);
                return null;
            }
        }
        return dataType;
    }

    /**
     * Parses a data type argument such as precision or length.
     * @return parsed value
     */
    default int dataTypeArgument() {
        expect(SpecialSymbol.OPEN_PAREN);
        consume();
        int value = (int) integerLiteral();
        expect(SpecialSymbol.CLOSE_PAREN);
        consume();
        return value;
    }

    default long integerLiteral() {
        var token = expectType(TokenType.INTEGER);
        long value = ((BigInteger) token.value()).longValue();
        consume();
        return value;
    }

    /**
     * Parses check constraint.
     * @param constraintName the name of the constraint, can be {@code null}
     */
    default void checkConstraint(String constraintName) {
        expect(StandardKeyword.CHECK);
        consume();
        expect(SpecialSymbol.OPEN_PAREN);
        consume();

        Expression expression = expression();

        expect(SpecialSymbol.CLOSE_PAREN);
        consume();

        checkConstraintModifier();
        commonConstraintModifiers();

        handleCheckConstraint(constraintName, expression);
    }

    default void checkConstraintModifier() {
    }

    default Expression expression(int precedence) {
        return expression(precedence, token -> false);
    }

    default Expression expression(int precedence, Predicate<Token> stopper) {
        Expression expression = operand();
        Token token = next();
        while (token.isBinaryOperator() && !stopper.test(token)) {
            var op = token.binaryOperatorGroup();
            if (op.precedence() < precedence) {
                break;
            }
            expression = binaryOperator(expression, op.rightPrecedence());
            if (op.associativity() == OperatorGroup.Associativity.NONE) {
                break;
            }
            token = next();
        }
        return expression;
    }

    default Expression binaryOperator(Expression leftOperand, int rightPrecedence) {
        if (next().isKeyword()) {
            return keywordBinaryOperator(leftOperand, rightPrecedence);
        } else {
            return symbolBinaryOperator(leftOperand, rightPrecedence);
        }
    }

    default Expression keywordBinaryOperator(Expression leftOperand, int rightPrecedence) {
        return switch (expectKeyword().standard()) {
            case IS -> isPredicate(leftOperand);
            case IN -> inOperator(leftOperand, false);
            case NOT -> {
                consume();
                var keyword = expectKeyword();
                yield switch (keyword.standard()) {
                    case IN -> inOperator(leftOperand, true);
                    default -> {
                        syntaxError(keyword);
                        yield null;
                    }
                };
            }
            default -> genericKeywordBinaryOperator(leftOperand, rightPrecedence);
        };
    }

    default Expression symbolBinaryOperator(Expression leftOperand, int rightPrecedence) {
        Token token = next();
        var text = token.text();
        consume();
        var rightOperand = expression(rightPrecedence);
        return Expressions.createBinaryOperator(text, leftOperand, rightOperand);
    }

    default Expression isPredicate(Expression subject) {
        return isPredicate().toExpression(subject);
    }

    default IsPredicate isPredicate() {
        expect(StandardKeyword.IS);
        consume();
        Keyword keyword = expectKeyword();
        final boolean negated = keyword.isSameAs(StandardKeyword.NOT);
        if (negated) {
            consume();
            keyword = expectKeyword();
        }
        IsPredicate predicate = switch (keyword.standard()) {
            case NULL -> IsPredicate.IS_NULL;
            case TRUE -> IsPredicate.IS_TRUE;
            case FALSE -> IsPredicate.IS_FALSE;
            case UNKNOWN -> IsPredicate.IS_UNKNOWN;
            default -> {
                syntaxError(keyword);
                yield null;
            }
        };
        assert predicate != null;
        if (negated) {
            predicate = predicate.negated();
        }
        return predicate;
    }

    default Expression inOperator(Expression subject, boolean negated) {
        expect(StandardKeyword.IN);
        consume();
        return Expressions.createInPredicate(subject, listOfExpressions(), negated);
    }

    default Expression genericKeywordBinaryOperator(Expression leftOperand, int rightPrecedence) {
        var text = next().text();
        consume();
        var rightOperand = expression(rightPrecedence);
        return Expressions.createBinaryOperator(text, leftOperand, rightOperand);
    }

    default List<Expression> listOfExpressions() {
        expect(SpecialSymbol.OPEN_PAREN);
        consume();
        List<Expression> list = new ArrayList<>();
        list.add(expression());
        while (next() != SpecialSymbol.CLOSE_PAREN) {
            expect(SpecialSymbol.COMMA);
            consume();
            list.add(expression());
        }
        consume();
        return list;
    }

    default Expression operand() {
        var token = next();
        if (token.isUnaryOperator()) {
            var op = token.unaryOperatorGroup();
            var text = token.text();
            consume();
            var operand = expression(op.precedence());
            return Expressions.createUnaryOperator(text, operand);
        } else if (token == SpecialSymbol.OPEN_PAREN) {
            consume();
            var group = expression(OperatorGroup.LOWEST_PRECEDENCE);
            expect(SpecialSymbol.CLOSE_PAREN);
            consume();
            return group;
        } else if (token.isSameAs(StandardKeyword.TRUE)) {
            consume();
            return Expression.TRUE;
        } else if (token.isSameAs(StandardKeyword.FALSE)) {
            consume();
            return Expression.FALSE;
        } else if (token.isSameAs(StandardKeyword.NULL)) {
            consume();
            return Expression.NULL;
        } else if (token.isLiteral()) {
            var literal = token.toLiteral();
            consume();
            return literal;
        } else if (token.isFunction()) {
            return functionCall();
        } else if (token.isIdentifier(IdentifierType.OBJECT_NAME)
            || token.isIdentifier(IdentifierType.FUNCTION_NAME)) {
            return functionCallOrColumnReference();
        }
        syntaxError(token);
        return null;
    }

    default Expression functionCall() {
        Keyword keyword = expectKeyword();
        String functionName = keyword.canonicalName();
        consume();
        if (next() == SpecialSymbol.OPEN_PAREN) {
            return Expressions.createFunctionCall(functionName, functionArguments());
        } else {
            return Expressions.createFunctionCall(functionName);
        }
    }

    default Expression functionCallOrColumnReference() {
        var token = next();
        consume();
        if (next() == SpecialSymbol.OPEN_PAREN) {
            return Expressions.createFunctionCall(token.toIdentifier(), functionArguments());
        } else if (token.isIdentifier(IdentifierType.OBJECT_NAME)) {
            return Expressions.createColumnReference(token.toIdentifier());
        }
        syntaxError(token);
        return null;
    }

    default List<Expression> functionArguments() {
        var args = new ArrayList<Expression>();
        expect(SpecialSymbol.OPEN_PAREN);
        consume();
        while (next() != SpecialSymbol.CLOSE_PAREN) {
            if (args.size() > 1) {
                expect(SpecialSymbol.COMMA);
                consume();
            }
            args.add(expression(0));
        }
        consume();
        return args;
    }
}
