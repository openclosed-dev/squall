package dev.openclosed.squall.api.spec;

import java.util.Optional;

/**
 * A constraint.
 */
public interface Constraint {

    /**
     * Returns the name of this constraint.
     * @return the name of this constraint, may be empty.
     */
    Optional<String> constraintName();
}
