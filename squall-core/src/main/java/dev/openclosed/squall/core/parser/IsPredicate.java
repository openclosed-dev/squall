package dev.openclosed.squall.core.parser;

import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.core.spec.expression.Expressions;

public enum IsPredicate {
    IS_NULL,
    IS_NOT_NULL(IS_NULL),
    IS_TRUE,
    IS_NOT_TRUE(IS_TRUE),
    IS_FALSE,
    IS_NOT_FALSE(IS_FALSE),
    IS_UNKNOWN,
    IS_NOT_UNKNOWN(IS_UNKNOWN);

    private IsPredicate negated;

    IsPredicate() {
        this.negated = null;
    }

    IsPredicate(IsPredicate negated) {
        this.negated = negated;
        negated.negated = this;
    }

    IsPredicate negated() {
        return negated;
    }

    public Expression toExpression(Expression subject) {
        return Expressions.createIsPredicate(subject, name());
    }
}
