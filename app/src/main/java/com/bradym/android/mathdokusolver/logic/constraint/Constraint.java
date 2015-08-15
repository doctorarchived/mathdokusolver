package com.bradym.android.mathdokusolver.logic.constraint;

import com.bradym.android.mathdokusolver.logic.Variable;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;

/**
 * Created by Michael on 7/16/2015.
 */
public abstract class Constraint {

    public final HashSet<Variable> scope;
    private boolean enabled = false;
    public boolean bordered = true;

    public Constraint(Collection<Variable> scope) {
        this.scope = new HashSet<>(scope.size());
        this.scope.addAll(scope);
        enable();
    }

    public void reset() {
    }

    public void enable() {
        if (!enabled) {
            for (Variable var : scope) {
                var.constraints.add(this);
            }
            enabled = true;
        }
    }

    public void disable() {
        if (enabled) {
            for (Variable var : scope) {
                var.constraints.remove(this);
            }
            enabled = false;
        }
    }

    /*
    Validate the constraint against current assignments and whichever heuristics are being used
     */
    protected abstract boolean onValidate(Variable var, int value);
    /*
    Take care of bookkeeping when a variable has been assigned a value
     */
    protected abstract void onAssignment(Variable var, Variable.Domain oldDomain);
    /*
    Take care of bookkeeping when a variable has been unassigned
     */
    protected abstract void onPopAssignment(Variable var, Variable.Domain oldDomain);

    protected abstract void onDomainPruning(Variable var, int value);

    protected abstract void onRestoredDomain(Variable var, Variable.Domain oldDomain);

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (Variable v : scope) {
            sb.append(v.index + " ");
        }
        sb.append("]");

        return sb.toString().trim();
    }

    public abstract String cellString();

    public final boolean validateAssignment(Variable var, int value) {
        return onValidate(var, value);
    }

    public final void updateAssignment(Variable var, Variable.Domain oldDomain) {
        onAssignment(var, oldDomain);
    }

    public final void popAssignment(Variable var, Variable.Domain oldDomain) {
        onPopAssignment(var, oldDomain);
    }

    public final void updateRestoredDomain(Variable var, Variable.Domain oldDomain) {
        onRestoredDomain(var, oldDomain);
    }

    public void updatePrunedValue(Variable var, int value) {
        onDomainPruning(var, value);
    }
}
