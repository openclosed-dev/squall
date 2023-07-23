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

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public abstract class SqlTokenizerTest {

    protected abstract SqlTokenizer createTokenizer(String text);

    public static Stream<SqlTestCase> generateTokens() {
        return loadTests("tokenize.md");
    }

    @ParameterizedTest
    @MethodSource
    public void generateTokens(SqlTestCase test) {
        var tokenizer = createTokenizer(test.firstSql());
        List<Token> tokens = new ArrayList<Token>();
        Token token;
        while ((token = tokenizer.next()) != Token.EOI) {
            tokens.add(token);
            tokenizer.consume();
        }

        Stream<String> actual = tokens.stream().map(t -> {
            var type = t.type();
            if (type == TokenType.SYMBOL) {
                return t.toString();
            } else {
                return type.toString();
            }
        });
        var expected = test.jsonAsStringList();
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    record StringTest(String input, String value) {
    }

    public static Stream<StringTest> stringTests() {
        return Stream.of(
                new StringTest("'This is a string'", "This is a string"),
                new StringTest("'Dianne''s horse'", "Dianne's horse")
        );
    }

    @ParameterizedTest
    @MethodSource("stringTests")
    public void testStringConstant(StringTest test) {
        var tokenizer = createTokenizer(test.input());
        var token = tokenizer.next();
        assertThat(token).isNotNull();
        assertThat(token.type()).isEqualTo(TokenType.STRING);
        assertThat(token.value()).isEqualTo(test.value());
    }

    record BitStringTest(String input, String text) {
    }

    public static Stream<BitStringTest> bitStringTests() {
        return Stream.of(
                new BitStringTest("b'1001'", "b'1001'"),
                new BitStringTest("B'1001'", "B'1001'")
        );
    }

    @ParameterizedTest
    @MethodSource("bitStringTests")
    public void testBitStringConstant(BitStringTest test) {
        var tokenizer = createTokenizer(test.input());
        var token = tokenizer.next();
        assertThat(token).isNotNull();
        assertThat(token.type()).isEqualTo(TokenType.BIT_STRING);
        assertThat(token.text()).isEqualTo(test.text());
    }

    record NumericTest(String input, TokenType type, Number value) {
    }

    public static Stream<NumericTest> numericConstantTests() {
        return Stream.of(
                new NumericTest("0", TokenType.INTEGER, BigInteger.ZERO),
                new NumericTest("1", TokenType.INTEGER, BigInteger.ONE),
                new NumericTest("42", TokenType.INTEGER, new BigInteger("42")),
                new NumericTest("3.5", TokenType.NUMBER, new BigDecimal("3.5")),
                new NumericTest("4.", TokenType.NUMBER, new BigDecimal("4.")),
                new NumericTest(".001", TokenType.NUMBER, new BigDecimal(".001")),
                new NumericTest("5e2", TokenType.NUMBER, new BigDecimal("5e2")),
                new NumericTest("1.925e-3", TokenType.NUMBER, new BigDecimal("1.925e-3"))
        );
    }

    @ParameterizedTest
    @MethodSource("numericConstantTests")
    public void testNumericConstant(NumericTest test) {
        var tokenizer = createTokenizer(test.input());
        var token = tokenizer.next();
        assertThat(token).isNotNull();
        assertThat(token.type()).isEqualTo(test.type);
        assertThat(token.value()).isEqualTo(test.value());
    }

    private static Stream<SqlTestCase> loadTests(String name) {
        return SqlTestCase.loadFrom(name, SqlTokenizerTest.class).stream();
    }
}
