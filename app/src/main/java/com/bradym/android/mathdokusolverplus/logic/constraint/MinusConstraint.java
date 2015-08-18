package com.bradym.android.mathdokusolverplus.logic.constraint;

import com.bradym.android.mathdokusolverplus.logic.Variable;

import java.util.Collection;
import java.util.Map;

/**
 * Being under the InverseConstraint, it requires that op be implemented to be the inverse of
 * subtraction => addition.
 */
public class MinusConstraint extends InverseConstraint {

    public static String op;

    public MinusConstraint(Collection<Variable> scope, int constraint) {
        super(scope, constraint, 0);
    }

    @Override
    protected IntPair onValidateEntry(Variable var, int value, Map.Entry<Variable, IntPair> entry, boolean adjusted) {
        IntPair currentValue = entry.getValue();
        Variable top = entry.getKey();
        int num = currentValue.first;
        int den = currentValue.second;
        int adjustedCumMin = getCumulativeMin();
        int adjustedCumMax = getCumulativeMax();

        if (!adjusted) {
            adjustedCumMin -= var.min();
            adjustedCumMax -= var.max();
        }

        if (var == top) {
            if ((value <= constraint + den + adjustedCumMax)
                    && (value >= constraint + den + adjustedCumMin)) {
                return new IntPair(value, den);
            }
        } else {
            den += value;
            if (num == -1) {
                int absMax = top.max();
                int absMin = top.min();
                adjustedCumMin -= absMin;
                adjustedCumMax -= absMax;
                if ((absMin <= constraint + den + adjustedCumMax) &&
                        (absMax >= constraint + den + adjustedCumMin)) {
                    return new IntPair(num, den);
                }
            } else {
                if ((num <= constraint + den + adjustedCumMax) &&
                        (num >= constraint + den + adjustedCumMin)) {
                    return new IntPair(num, den);
                }
            }
        }
        return null;
    }

    @Override
    protected int op(int current, int oldVal, int newVal) {
        return current - oldVal + newVal;
    }

    @Override
    protected String opString() {
        return op;
    }
}


