package dev.openclosed.squall.core.spec.expression;

import dev.openclosed.squall.api.spec.Expression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Case(
    Type type,
    Optional<Expression> expression,
    List<When> when,
    Optional<Expression> elseResult) implements MapSourceExpression {

    public record When(Expression condition, Expression result) {
        public When {
            Objects.requireNonNull(condition);
            Objects.requireNonNull(result);
        }
    };

    public Case(Expression expression, List<When> when, Expression elseResult) {
        this(Type.CASE, Optional.ofNullable(expression), when, Optional.ofNullable(elseResult));
    }

    public Case {
        when = List.copyOf(when);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("CASE ");
        expression().ifPresent(e -> sb.append(e).append(' '));

        for (var entry : when()) {
            sb.append("WHEN ")
                .append(entry.condition())
                .append(" THEN ")
                .append(entry.result())
                .append(' ');
        }

        elseResult().ifPresent(result -> {
            sb.append("ELSE ").append(result).append(' ');
        });
        return sb.append("END").toString();
    }
}
