package com.bradym.android.mathdokusolverplus.logic.constraint;

import com.bradym.android.mathdokusolverplus.logic.Variable;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;


/*
This covers constraints whose operation depends on which variable is the "dominant" one.
e.g. For 3 numbers in a division constraint, a / b / c may be different than b / a / c so we keep
track of all current possible values the constraint can hold.
*/


public abstract class InverseConstraint extends ArithmeticConstraint {

    private Map<Variable, IntPair> currentValues;
    private final Deque<Map<Variable, IntPair>> valuesHistory;

    InverseConstraint(Collection<Variable> scope, int constraint, int identity) {
        super(scope, constraint, identity);

        currentValues = new HashMap<>();
        valuesHistory = new ArrayDeque<>();

        for (Variable variable : scope) {
            currentValues.put(variable, new IntPair());
        }
    }

    @Override
    public void reset() {
        super.reset();
        currentValues.clear();
        valuesHistory.clear();

        for (Variable variable : scope) {
            currentValues.put(variable, new IntPair());
        }
    }

    @Override
    protected final boolean onValidate(Variable var, int value) {
        boolean valid = false;
        for (Map.Entry<Variable, IntPair> entry : currentValues.entrySet()) {
           if (onValidateEntry(var, value, entry, false) != null) {
               valid = true;
               break;
           }
        }
        return valid;
    }


    protected abstract IntPair onValidateEntry(Variable var, int value, Map.Entry<Variable, IntPair> entry, boolean adjusted);

    @Override
    public void onAssignment(Variable var, int value) {
        super.onAssignment(var, value);
        valuesHistory.addLast(currentValues);
        currentValues = updateCurrentValue(var, value);
    }

    @Override
    public void onPopAssignment(Variable var, int value) {
        super.onPopAssignment(var, value);
        this.currentValues = valuesHistory.pollLast();
    }

    @Override
    public String toString() {
        return super.toString() + " CURRENT VALUES " + currentValues.toString();
    }

    private Map<Variable, IntPair> updateCurrentValue(Variable var, int value) {
        Map<Variable, IntPair> newMap = new HashMap<>();
        for (Map.Entry<Variable, IntPair> entry : currentValues.entrySet()) {
            IntPair pair;
            if ((pair=onValidateEntry(var, value, entry, true)) != null) {
                newMap.put(entry.getKey(), pair);
            }
        }
        return newMap;
    }

    protected final class IntPair {

        final int first;
        final int second;

        public IntPair() {
            this.first = -1;
            this.second = identity;
        }

        public IntPair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return first + ", " + second;
        }
    }
}
