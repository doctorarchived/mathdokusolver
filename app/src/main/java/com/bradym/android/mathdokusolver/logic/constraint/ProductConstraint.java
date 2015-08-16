package com.bradym.android.mathdokusolver.logic.constraint;

import com.bradym.android.mathdokusolver.logic.Variable;

import java.util.Collection;

/**
 * Product constraint.
 */
public class ProductConstraint extends CommutativeConstraint {

    public static String op;

    public ProductConstraint(Collection<Variable> scope, int constraint) {
        super(scope, constraint, 1);
    }

    @Override
    protected boolean onValidate(Variable var, int value) {
        int newCurrent = getCurrentValue() * value;
        int cumMax = getCumulativeMax() / var.max();
        int cumMin = getCumulativeMin() / var.min();
        return (constraint % value == 0) && (newCurrent * cumMax >= constraint) &&
                (newCurrent * cumMin <= constraint);
    }

    @Override
    protected int op(int current, int remove, int add) {
        return current / remove * add;
    }

    @Override
    protected String opString() {
        return op;
    }
}
