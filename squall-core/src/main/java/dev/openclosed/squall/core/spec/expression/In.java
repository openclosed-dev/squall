package dev.openclosed.squall.core.spec.expression;

import dev.openclosed.squall.api.spec.Expression;

import java.util.List;

record In(
    Type type,
    Expression left,
    List<Expression> right
    ) implements RecordExpression {

    public In {
        if (type != Type.IN && type != Type.NOT_IN) {
            throw new IllegalArgumentException();
        }
        right = List.copyOf(right);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder()
            .append(left().toString())
            .append(' ')
            .append(type() == Type.IN ? "IN" : "NOT IN")
            .append(" (");

        int i = 0;
        for (var e : right()) {
            if (i++ > 0) {
                sb.append(", ");
            }
            sb.append(e.toString());
        }
        sb.append(')');
        return sb.toString();
    }
}
