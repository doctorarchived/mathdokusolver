package com.bradym.android.mathdokusolver.logic.constraint;

import com.bradym.android.mathdokusolver.logic.Variable;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;


public final class DiffConstraint extends Constraint {

    //final BitSet bitSet;
    int placed;
    int[] remaining;


    /*
    We use the following assumptions:
    1) Every DiffConstraint will have a scope of N Variables given an NxN grid
    2) Every DiffConstraint will have the numbers from 1 to N inclusive assigned to its variables
    3) We don't track which variables have been assigned. This is left to the solver algorithm.
     */
    public DiffConstraint(Collection<Variable> scope) {
        super(scope);
        //bitSet = new BitSet(maxNum);
        //bitSet.set(0, maxNum);
        placed = 0;
        remaining = new int[scope.size()];
        for (Variable v : scope) {
            for (int i = v.nextValid(0); i != -1; i = v.nextValid(i+1)) {
                remaining[i]++;
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        placed = 0;
        remaining = new int[scope.size()];
        for (Variable v : scope) {
            for (int i = v.nextValid(0); i != -1; i = v.nextValid(i+1)) {
                remaining[i]++;
            }
        }
    }

    @Override
    protected boolean onValidate(Variable var, int value) {
        if (!((placed & (1 << (value - 1))) == 0)) {
            return false;
        } else {
            int toPlace = ~placed & var.domain.domain;
            int index = 0;
            while (toPlace != 0) {
                if (((toPlace & 1) == 1) && (remaining[index] == 1) && (index != value - 1)) {
                    return false;
                }
                toPlace >>= 1;
                index++;
            }
        }
        return true;
    }

    @Override
    protected void onAssignment(Variable var, Variable.Domain oldDomain) {
        placed |= var.domain.domain;
        for (int i = oldDomain.nextValid(0); i != -1; i = oldDomain.nextValid(i+1)) {
            remaining[i]--;
        }
    }

    @Override
    protected void onPopAssignment(Variable var, Variable.Domain oldDomain) {
        placed ^= oldDomain.domain;
        for (int i = var.nextValid(0); i != -1; i = var.nextValid(i+1)) {
            remaining[i]++;
        }
    }

    @Override
    protected void onDomainPruning(Variable var, int value) {
        remaining[value-1]--;
    }

    @Override
    protected void onRestoredDomain(Variable var, Variable.Domain oldDomain) {
        int domainChange = var.domain.domain & ~oldDomain.domain;
        int index = 0;

        while (domainChange != 0) {
            if ((domainChange & 1) == 1) {
                remaining[index]++;
            }
            domainChange >>= 1;
            index++;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " PLACED " + placed + " REMAINING " + Arrays.toString(remaining);
    }

    @Override
    public String cellString() {
        return "";
    }


}
