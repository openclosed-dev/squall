package dev.openclosed.squall.core.spec.expression;

import dev.openclosed.squall.api.spec.Expression;

import java.util.Objects;

public record Is(Type type, Expression subject, String predicate) implements MapSourceExpression {

    public Is(Expression subject, String predicate) {
        this(Type.IS, subject, predicate);
    }

    public Is {
        Objects.requireNonNull(subject);
        Objects.requireNonNull(predicate);
        predicate = predicate.toLowerCase();
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(subject().toString())
            .append(' ')
            .append(predicate().replaceAll("_", " ").toUpperCase())
            .toString();
    }
}
