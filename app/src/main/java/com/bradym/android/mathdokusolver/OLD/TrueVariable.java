//package com.bradym.android.mathdokusolver.OLD;
//
//import com.bradym.android.mathdokusolver.TrueCell;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Deque;
//import java.util.List;
//
///**
// * Created by Michael on 6/3/2015.
// *
// * Represents the variable used in the solving process
// * Attached to a cell on the TrueGrid
// *
// */
//private class TrueVariable {
//
//    public final String name;
//    public final int index;
//    public int weightedIndex;
//    public final List<TrueConstraint> constraints = new ArrayList<>();
//    //public final List<Integer> domain = new ArrayList<>();
//
//    boolean pruning = false;
//
//
//    public final TrueCell cell;
//
//    public int value = -1;
//
//
//    private final Deque<Domain> history;
//    Domain domain;
//    Domain temp;
//    //BitSet aDomain;
//    //int aDomain;
//    //int domainSize;
//    //int max;
//    //int min;
//
//
//    public TrueVariable(String name, List<Integer> domain, TrueCell cell) {
//        this.name = name;
//        this.index = Integer.parseInt(name);
//        this.weightedIndex = index;
//        this.cell = cell;
//        this.history = new ArrayDeque<>(domain.size());
//        this.domain = new Domain(domain.size(), true);
//        this.temp = new Domain(this.domain);
//
//    }
//
//    public int nextValid(int i) {
//        for (int j = i; j < domain.size; j++) {
//            boolean isSet = (domain.domain & (1 << j)) != 0;
//            if (isSet) {
//                return j;
//            }
//        }
//        return -1;
//    }
//
//    public boolean assign(int val) {
//        boolean valid = true;
//        for (TrueConstraint tc : constraints) {
//            valid &= tc.updateVariableAssignment(this, val);
//            if (!valid) {
//                break;
//            }
//        }
//        if (valid) {
//            history.addLast(domain);
//            for (TrueConstraint tc : constraints) {
//                tc.lockAssignment(this);
//            }
//            domain = new Domain(val, false);
//        } else {
//            for (TrueConstraint tc : constraints) {
//                tc.temp = null;
//            }
//        }
//        return valid;
//    }
//
//    public int prune(int index) {
//        if (!pruning) {
//            history.addLast(new Domain(domain));
//            pruning = true;
//        }
//        return domain.removeIndex(index);
//    }
//
//    public void completeRestore() {
//        while (!history.isEmpty()) {
//            restoreDomain();
//        }
//    }
//
//    public void restoreDomain() {
//        domain = history.pollLast();
//        pruning = false;
//    }
//
//    public void lockPruned() {
//        pruning = false;
//        for (TrueConstraint tc : constraints) {
//            tc.lockPruning();
//        }
//
//    }
//
//    @Override
//    public String toString() {
//        return name;
//    }
//
//    public String detail() {
//        return "NAME: " + name + " DOMAIN: " + domain.domain + " VALUE " + value;
//    }
//}
