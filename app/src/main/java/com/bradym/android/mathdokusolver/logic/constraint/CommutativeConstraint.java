package com.bradym.android.mathdokusolver.logic.constraint;

import com.bradym.android.mathdokusolver.logic.Variable;

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
    protected void onPopAssignment(Variable var, Variable.Domain oldDomain) {
        super.onPopAssignment(var, oldDomain);
        currentValue = op(currentValue, oldDomain.value, identity);
    }

    @Override
    protected void onAssignment(Variable var, Variable.Domain oldDomain) {
        super.onAssignment(var, oldDomain);
        currentValue = op(currentValue, identity, var.value());
    }

    int getCurrentValue() {
        return currentValue;
    }

    @Override
    public String toString() {
        return super.toString() + " CURRENT VALUE " + currentValue;
    }

}
