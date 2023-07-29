package dev.openclosed.squall.core.parser;

public record MetacommandToken(CharSequence wholeText, int start, int end) implements Token {

    @Override
    public TokenType type() {
        return TokenType.METACOMMAND;
    }

    @Override
    public String text() {
        return wholeText.subSequence(start, end).toString();
    }
}
