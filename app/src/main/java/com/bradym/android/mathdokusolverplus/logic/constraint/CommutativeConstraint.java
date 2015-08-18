package com.bradym.android.mathdokusolverplus.logic.constraint;

import com.bradym.android.mathdokusolverplus.logic.Variable;

import java.util.Collection;

/**
 * Given the assumption that the constraint is based on a commutative operation, the order of the
 * variable assignments is irrelevant so there is only one possible current value.
 */
public abstract class CommutativeConstraint extends ArithmeticConstraint {

    private int currentValue;

    CommutativeConstraint(Collection<Variable> scope, int constraint, int identity) {
        super(scope, constraint, identity);
        currentValue = identity;
    }

    @Override
    public void reset() {
        super.reset();
        currentValue = identity;
    }

    @Override
    protected void onPopAssignment(Variable var, int value) {
        super.onPopAssignment(var, value);
        currentValue = op(currentValue, value, identity);
    }

    @Override
    protected void onAssignment(Variable var, int value) {
        super.onAssignment(var, value);
        currentValue = op(currentValue, identity, value);
    }

    protected int getCurrentValue() {
        return currentValue;
    }

    @Override
    public String toString() {
        return super.toString() + " CURRENT VALUE " + currentValue;
    }

}
