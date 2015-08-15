package com.bradym.android.mathdokusolver.logic.constraint;

import com.bradym.android.mathdokusolver.logic.Variable;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

/**
 * Created by Michael on 7/16/2015.
 */

/*
Given a commutative operation f and a list of numbers {A,B,C,D,...,} we define f applied to the list as being:
A f B f C f D ... = Z

In this case, op should be implemented to represent f.
 */
public abstract class CommutativeConstraint extends ArithmeticConstraint {

    private int currentValue;

    public CommutativeConstraint(Collection<Variable> scope, int constraint, int identity) {
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

    public int getCurrentValue() {
        return currentValue;
    }

    @Override
    public String toString() {
        return super.toString() + " CURRENT VALUE " + currentValue;
    }

}
