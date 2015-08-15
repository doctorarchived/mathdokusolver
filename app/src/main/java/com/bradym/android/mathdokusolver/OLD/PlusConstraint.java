//package com.bradym.android.mathdokusolver.OLD;
//
//import java.util.ArrayDeque;
//import java.util.List;
//
///**
// * Created by Michael on 6/3/2015.
// *
// * Constraint representing Plus.
// *
// *
// */
//public class PlusConstraint extends TrueConstraint {
//
//    private final ArrayDeque<Integer> history;
//    private int active;
//    private int rem;
//    static long validateTime = 0L;
//
//    int maxHeuristic;
//    int minHeuristic;
//
//    public PlusConstraint(int value, List<TrueVariable> scope, int max) {
//        super(value, scope, max, 0);
//        enable();
//        history = new ArrayDeque<>();
//        active = 0;
//        rem = scope.size();
//        adjustMaxBounds();
//        adjustMinBounds();
//    }
//
//    @Override
//    public void adjustMaxBounds() {
//        int newMax = 0;
//        for (TrueVariable tv : scope) {
//            if (tv.value == -1) {
//                newMax += tv.min;
//            }
//        }
//        maxHeuristic = newMax;
//    }
//
//    @Override
//    public void adjustMinBounds() {
//        int newMin = 0;
//        for (TrueVariable tv : scope) {
//            if (tv.value == -1) {
//                newMin += tv.min;
//            }
//        }
//        minHeuristic = newMin;
//
//    }
//
//    public boolean updateVariableAssignment(TrueVariable var, int val) {
//        if ((active + val + MIN_NUM * (rem - 1) <= value) && (active + val + MAX_NUM * (rem - 1) >= value)) {
//            history.addLast(active);
//            active += val;
//            rem--;
//            return true;
//        }
//        return false;
//    }
//
//    public boolean validate(TrueVariable var, int val) {
//        long s = System.currentTimeMillis();
//        boolean b = ((active + val + MIN_NUM * (rem - 1) <= value) && (active + val + MAX_NUM * (rem - 1) >= value));
//        validateTime += (System.currentTimeMillis() - s);
//        return b;
//    }
//
//    public void restore() {
//        history.clear();
//        active = 0;
//        rem = scope.size();
//    }
//
//    @Override
//    public void enable() {
//        super.enable();
//        for (TrueVariable tv : scope) {
//            tv.weightedIndex = (tv.weightedIndex + 1) * (num_col * num_col);
//        }
//    }
//    @Override
//    public void disable() {
//        super.disable();
//        for (TrueVariable tv : scope) {
//            tv.weightedIndex = tv.index;
//        }
//    }
//
//    public void pop() {
//        active = history.pollLast();
//        rem++;
//    }
//
//    @Override
//    public String toString() {
//        return "PLUS CONSTRAINT | SCOPE = " + scope.toString() + " | " + active;
//    }
//
//
//    public String simpleString() {
//        if (scope.size() == 1) {
//            return value + "";
//        } else {
//            return value + "\u002B";
//        }
//    }
//
//}
