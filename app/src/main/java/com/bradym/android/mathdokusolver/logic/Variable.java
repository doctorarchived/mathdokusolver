package com.bradym.android.mathdokusolver.logic;

import com.bradym.android.mathdokusolver.PuzzleCell;
import com.bradym.android.mathdokusolver.logic.constraint.Constraint;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by Michael on 7/16/2015.
 */
public final class Variable {

    public final List<Constraint> constraints;
    public final int index;
    public final PuzzleCell cell;
    public Domain domain;

    private final int max;
    private final Deque<Domain> domainHistory;
    private boolean domainSaved = false;

    public Variable(int index, PuzzleCell cell, int max) {
        this.index = index;
        this.cell = cell;
        this.constraints = new ArrayList<>();
        this.domainHistory = new ArrayDeque<>();
        this.domain = new Domain(~(~0 << max));
        this.max = max;
    }

    public void reset() {
        this.domainHistory.clear();
        this.domain = new Domain(~(~0 << max));
        domainSaved = false;
    }

    public int nextValid(int i) {
        return domain.nextValid(i);
    }

    public void lock() {
        domainSaved = false;
    }

    public void prune(int val) {
        if (!domainSaved) {
            domainHistory.addLast(new Domain(domain));
            domainSaved = true;
        }
        domain.prune(val);
        for (Constraint constraint : constraints) {
            constraint.updatePrunedValue(this, val);
        }
    }

    public void restoreDomain() {
        domainSaved = false;
        Domain oldDomain = domain;
        domain = domainHistory.pollLast();
        for (Constraint constraint : constraints) {
            constraint.updateRestoredDomain(this, oldDomain);
        }
    }

    public boolean assign(int val) {
        boolean valid = true;

        for (Constraint constraint : constraints) {
            valid &= constraint.validateAssignment(this, val);
            if (!valid) break;
        }

        if (valid) {
            domainHistory.addLast(domain);
            domain = new Domain(1 << (val - 1));
            domain.value = val;
            for (Constraint constraint : constraints) {
                constraint.updateAssignment(this, domainHistory.peekLast());
            }
        }
        return valid;
    }

    public void unAssign() {
        Domain oldDomain = domain;
        domain = domainHistory.pollLast();
        for (Constraint constraint : constraints) {
            constraint.popAssignment(this, oldDomain);
        }
    }

    public int value() {
        return domain.value;
    }

    public void setValue(int value) {
        domain.value = value;
    }

    @Override
    public boolean equals(Object o) {
        return ((Variable) o).index == index;
    }

    public int max() {
        return domain.max;
    }

    public int min() {
        return domain.min;
    }

    public int domainSize() {
        return domain.size;
    }

    @Override
    public String toString() {
        return "VARIABLE " + index + " DOMAIN " + domain.toString() + " VALUE " + domain.value;
    }

    public final class Domain {

        public int domain;
        public int max;
        public int min;
        public int size;
        public int value = -1;


        public Domain(int domain) {
            this.domain = domain;
            this.max = 32 - Integer.numberOfLeadingZeros(domain);
            this.min = 1 + Integer.numberOfTrailingZeros(domain);
            this.size = Integer.bitCount(domain);
        }

        public Domain(Domain domain) {
            this.domain = domain.domain;
            this.max = domain.max;
            this.min = domain.min;
            this.size = domain.size;
        }

        public void prune(int val) {
            domain = domain & ~(1 << (val - 1));
            if (val == max) {
                max = domain == 0 ? 0 : 32 - Integer.numberOfLeadingZeros(domain);
            }
            if (val == min) {
                min = domain == 0 ? 0 : 1 + Integer.numberOfTrailingZeros(domain);
            }
            size--;
        }

        public int nextValid(int i) {
            int shifted = domain >> i;
            return shifted == 0 ? -1 : i + Integer.numberOfTrailingZeros(shifted);
        }

        @Override
        public String toString() {
            return Integer.toBinaryString(domain);
        }


    }
}


