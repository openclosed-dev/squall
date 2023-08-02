package dev.openclosed.squall.core.spec.expression;

import dev.openclosed.squall.api.spec.Expression;

record Is(Type type, Expression subject, String predicate) implements RecordExpression {

    @Override
    public String toString() {
        return new StringBuilder()
            .append(subject().toString())
            .append(' ')
            .append(predicate().replaceAll("_", " ").toUpperCase())
            .toString();
    }
}
