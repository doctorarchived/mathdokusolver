package com.bradym.android.mathdokusolverplus.logic.constraint;

import com.bradym.android.mathdokusolverplus.logic.Variable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * Constraint that can be any of the 4 arithmetic constraints
 */
public class WildcardConstraint extends Constraint {

    private final int constraint;
    private final static String op = "?";

    private List<Constraint> possibleConstraints = new ArrayList<>();
    private final Deque<List<Constraint>> constraintsHistory = new ArrayDeque<>();

    public WildcardConstraint(Collection<Variable> scope, int constraint) {
        super(scope);
        this.constraint = constraint;
        possibleConstraints.add(new DivConstraint(scope, constraint));
        possibleConstraints.add(new MinusConstraint(scope, constraint));
        possibleConstraints.add(new ProductConstraint(scope, constraint));
        possibleConstraints.add(new PlusConstraint(scope, constraint));
        for (Constraint c : possibleConstraints) {
            c.disable();
        }
    }

    @Override
    protected boolean onValidate(Variable var, int value) {
        for (Constraint c : possibleConstraints) {
            if (c.onValidate(var, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onAssignment(Variable var, int value) {
        List<Constraint> pConstraints = new ArrayList<>();
        for (Constraint c : possibleConstraints) {
            if (c.onValidate(var, value)) {
                c.updateAssignment(var, value);
                pConstraints.add(c);
            }
        }
        constraintsHistory.addLast(possibleConstraints);
        possibleConstraints = pConstraints;
    }

    @Override
    protected void onPopAssignment(Variable var, int value) {
        for (Constraint c : possibleConstraints) {
            c.popAssignment(var, value);
        }
        possibleConstraints = constraintsHistory.pollLast();

    }

    @Override
    protected void onDomainPruning(Variable var, int value) {
        for (Constraint c : possibleConstraints) {
            c.updatePrunedValue(var, value);
        }
    }

    @Override
    protected void onRestoredDomain(Variable var, Variable.Domain oldDomain) {
        for (Constraint c : possibleConstraints) {
            c.updateRestoredDomain(var, oldDomain);
        }
    }

    @Override
    public String cellString() {
        return constraint + op;
    }
}
