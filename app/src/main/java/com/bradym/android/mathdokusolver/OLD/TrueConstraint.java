//package com.bradym.android.mathdokusolver.OLD;
//
//import com.bradym.android.mathdokusolver.logic.GACState;
//
//import java.util.Deque;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Michael on 6/3/2015.
// *
// * Abstract class that all Constraints inherit.
// *
// */
//public abstract class TrueConstraint {
//
//    static long validateTime = 0L;
//
//    public final int constraint;
//    final int size;
//    final int num_col;
//    boolean disabled = true;
//    public Set<TrueVariable> scope;
//    protected Set<TrueVariable> unassigned;
//
//    protected GACState active;
//    protected GACState temp;
//    protected Deque<GACState> history;
//
//    protected final int identity;
//
//
//
//
//    //public HashSet<TrueVariable> scope;
//
//
//    public TrueConstraint(int value, List<TrueVariable> scope, int num_col, int identity) {
//        this.constraint = value;
//        this.size = scope.size();
//        this.num_col = num_col;
//        this.scope = new HashSet<>(scope);
//        this.unassigned = new HashSet<>(scope);
//        this.identity = identity;
//
//        enable();
//        adjustMaxBounds();
//        adjustMinBounds();
//    }
//
//    protected abstract int op(int first, int second);
//
//    protected abstract int opInv(int first, int second);
//
//
//    private void adjustMaxBounds() {
//        int newRemMax = identity;
//        int newMax = 1;
//        for (TrueVariable var : unassigned) {
//            newRemMax = op(newRemMax, var.domain.max);
//            if (newMax < var.domain.max) newMax = var.domain.max;
//        }
//        active.max = newMax;
//        active.remMax = newRemMax;
//    }
//    private void adjustMinBounds() {
//        int newRemMin = identity;
//        int newMin = num_col;
//        for (TrueVariable var : unassigned) {
//            newRemMin = op(newRemMin, var.domain.min);
//            if (newMin > var.domain.min) newMin = var.domain.min;
//        }
//        active.min = newMin;
//        active.remMin = newRemMin;
//    }
//
//
//    public void disable() {
//        if (!disabled) {
//            for (TrueVariable tv : scope) {
//                tv.constraints.remove(this);
//                tv.weightedIndex = (tv.index + 1);
//            }
//            disabled = true;
//        }
//    }
//
//
//    public void enable() {
//        if (disabled) {
//            for (TrueVariable tv : scope) {
//                tv.constraints.add(this);
//                tv.weightedIndex = (tv.weightedIndex + 1);
//            }
//            disabled = false;
//        }
//    }
//
//    public void pop() {
//        if (active.assigned != null) unassigned.add(active.assigned);
//        active = history.pollLast();
//        temp = null;
//    }
//
//    public void restore() {
//        active = history.pollFirst();
//        history.clear();
//        temp = null;
//    }
//
//
//    public abstract boolean updateVariableAssignment(TrueVariable var, int val);
//
//    public void lockAssignment(TrueVariable var) {
//        history.addLast(active);
//        unassigned.remove(var);
//        active = temp;
//        adjustMaxBounds();
//        adjustMinBounds();
//    }
//
//
//
//    public abstract boolean validate(TrueVariable var, int val);
//
//    public void lockPruning() {
//        history.addLast(active);
//        active = temp;
//        temp = null;
//        adjustMinBounds();
//        adjustMaxBounds();
//    }
//
//    public abstract String simpleString();
//
//
//    private final class State<T> {
//
//        final int currentValue;
//        final Map<TrueVariable, T> currentValues;
//
//        public State(int value) {
//            this.currentValue = value;
//            this.currentValues = null;
//        }
//
//        public State(Map<TrueVariable, T> values) {
//            this.currentValue = -1;
//            this.currentValues = values;
//        }
//    }
//
//}
