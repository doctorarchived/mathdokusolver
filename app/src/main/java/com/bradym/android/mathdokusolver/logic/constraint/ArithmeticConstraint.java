package com.bradym.android.mathdokusolver.logic.constraint;

import com.bradym.android.mathdokusolver.logic.Variable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Michael on 7/16/2015.
 */


public abstract class ArithmeticConstraint extends Constraint {

    final int constraint;
    final int identity;

    private int cumulativeMax;
    private int cumulativeMin;

    public ArithmeticConstraint(Collection<Variable> scope, int constraint, int identity) {
        super(scope);
        this.constraint = constraint;
        this.identity = identity;
        this.cumulativeMax = identity;
        this.cumulativeMin = identity;
        for (Variable v : scope) {
            cumulativeMax = op(cumulativeMax, identity, v.max());
            cumulativeMin = op(cumulativeMin, identity, v.min());
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.cumulativeMax = identity;
        this.cumulativeMin = identity;
        for (Variable v : scope) {
            cumulativeMax = op(cumulativeMax, identity, v.max());
            cumulativeMin = op(cumulativeMin, identity, v.min());
        }
    }


    @Override
    protected void onAssignment(Variable var, Variable.Domain oldDomain) {
        cumulativeMax = op(cumulativeMax, oldDomain.max, identity);
        cumulativeMin = op(cumulativeMin, oldDomain.min, identity);
    }

    @Override
    protected void onPopAssignment(Variable var, Variable.Domain oldDomain) {
        cumulativeMax = op(cumulativeMax, identity, var.max());
        cumulativeMin = op(cumulativeMin, identity, var.min());
    }

    @Override
    protected void onDomainPruning(Variable var, int value) {
        if (value > var.max() || var.domainSize() == 0) {
            cumulativeMax = op(cumulativeMax, value, var.domainSize() == 0 ? identity : var.max());
        }
        if (value < var.min() || var.domainSize() == 0) {
            cumulativeMin = op(cumulativeMin, value, var.domainSize() == 0 ? identity : var.min());
        }
    }

    @Override
    protected final void onRestoredDomain(Variable var, Variable.Domain oldDomain) {
        if (var.max() > oldDomain.max || oldDomain.size == 0) {
            cumulativeMax = op(cumulativeMax, oldDomain.domain == 0 ? identity : oldDomain.max, var.max());
        }
        if (var.min() < oldDomain.min || oldDomain.size == 0) {
            cumulativeMin = op(cumulativeMin, oldDomain.domain == 0 ? identity : oldDomain.min, var.min());
        }

    }

    @Override
    public String toString() {
        return super.toString() + " CUM MAX / MIN " + cumulativeMax + "/" + cumulativeMin;
    }

    @Override
    public String cellString() {
        if (scope.size() == 1) {
            return constraint + "";
        } else {
            return constraint + opString();
        }
    }


    protected int getCumulativeMax() {
        return cumulativeMax;
    }

    protected int getCumulativeMin() {
        return cumulativeMin;
    }

    protected abstract int op(int current, int remove, int add);

    protected abstract String opString();


}