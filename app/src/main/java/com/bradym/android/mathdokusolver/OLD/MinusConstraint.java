//package com.bradym.android.mathdokusolver.OLD;
//
//import java.util.ArrayDeque;
//import java.util.Deque;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Michael on 6/3/2015.
// * Represents the Minus Constraint.
// * Given n variables, any of the n can be the minuend of the difference yielding n possible values
// *
// */
//public class MinusConstraint extends TrueConstraint {
//
////    private final Deque<Map<TrueVariable, Difference>> partials;
//    private int rem;
//
//    private Deque<Map<TrueVariable, IntPair>> history;
//    private Map<TrueVariable, IntPair> active;
//    static long validateTime = 0L;
//
//    private HashMap<TrueVariable, Integer> maxHeuristics = new HashMap<>();
//    private HashMap<TrueVariable, Integer> minHeuristics = new HashMap<>();
//
//
//    public MinusConstraint(int value, List<TrueVariable> scope, int max) {
//        super(value, scope, max);
//        rem = scope.size();
//
//        enable();
//        active = new HashMap<>();
//        history = new ArrayDeque<>();
//        for (TrueVariable tv : scope) {
//            active.put(tv, new IntPair(0,0));
//        }
//        adjustMaxBounds();
//        adjustMinBounds();
//    }
//
//    @Override
//    public void adjustMaxBounds() {
//        int[] max = new int[scope.size()];
//        for (int i = 0; i < scope.size(); i++) {
//            for (int j = 0; j < scope.size(); j++) {
//                if (scope.get(j).value == -1 && j != i) {
//                    max[i] += scope.get(j).max;
//                }
//            }
//        }
//        for (int m = 0; m < max.length; m++) {
//            maxHeuristics.put(scope.get(m), max[m]);
//        }
//    }
//
//    @Override
//    public void adjustMinBounds() {
//        int[] min = new int[scope.size()];
//        for (int i = 0; i < scope.size(); i++) {
//            for (int j = 0; j < scope.size(); j++) {
//                if (scope.get(j).value == -1 && j != i) {
//                    min[i] += scope.get(j).min;
//                }
//            }
//        }
//        for (int m = 0; m < min.length; m++) {
//            minHeuristics.put(scope.get(m), min[m]);
//        }
//    }
//
//    public boolean updateVariableAssignment(TrueVariable var, int val) {
//        int maxHeuristic = MAX_NUM * (rem-1);
//        Map<TrueVariable, IntPair> newActive = new HashMap<>();
//        for (Map.Entry<TrueVariable, IntPair> entry : active.entrySet()) {
//
//            TrueVariable v = entry.getKey();
//            IntPair pair = entry.getValue();
//            int num = pair.top;
//            int den = pair.bottom;
//            boolean numAssigned = pair.topSet;
//
//            if (v == var) {
//                // Assign numerator
//                if (!numAssigned && (val <= den + maxHeuristic + value) && (val >= value + den)) {
//                    newActive.put(v, pair.addTop(val));
//                }
//            } else {
//                if (numAssigned) {
//                    if ((num <= den + val + maxHeuristic + value) && (num >= value + den + val)) {
//                        newActive.put(v, pair.addBot(val));
//                    }
//                } else if (rem > 1) {
//                    int maxT = MAX_NUM * (rem-2);
//
//                    if ((MIN_NUM <= den + val + maxT + value) && (MAX_NUM >= value + den + val)) {
//                        newActive.put(v, pair.addBot(val));
//                    }
//                }
//            }
//        }
//        if (!newActive.isEmpty()) {
//            rem--;
//            history.addLast(active);
//            active = newActive;
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean validate(TrueVariable var, int val) {
//        long s = System.currentTimeMillis();
//        int maxHeuristic = MAX_NUM * (rem-1);
//        for (Map.Entry<TrueVariable, IntPair> entry : active.entrySet()) {
//
//            TrueVariable v = entry.getKey();
//            IntPair pair = entry.getValue();
//            int num = pair.top;
//            int den = pair.bottom;
//            boolean numAssigned = pair.topSet;
//
//            if (v == var) {
//                // Assign numerator
//                if (!numAssigned && (val <= den + maxHeuristic + value) && (val >= value + den)) {
//                    validateTime += (System.currentTimeMillis() - s);
//                    return true;
//                }
//            } else {
//                if (numAssigned) {
//                    if ((num <= den + val + maxHeuristic + value) && (num >= value + den + val)) {
//                        validateTime += (System.currentTimeMillis() - s);
//                        return true;
//                    }
//                } else if (rem > 1) {
//                    int maxT = MAX_NUM * (rem-2);
//
//                    if ((MIN_NUM <= den + val + maxT + value) && (MAX_NUM >= value + den + val)) {
//                        validateTime += (System.currentTimeMillis() - s);
//                        return true;
//                    }
//                }
//            }
//        }
//        validateTime += (System.currentTimeMillis() - s);
//        return false;
//    }
//
//    public void restore() {
//        history.clear();
//        rem = scope.size();
//        for (TrueVariable tv : scope) {
//            active.put(tv, new IntPair(0,0));
//        }
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
//        this.active = history.pollLast();
//        rem++;
//    }
//
//    @Override
//    public String toString() {
//        return "MINUS CONSTRAINT | SCOPE = " + scope.toString() + " | " + active.toString();
//    }
//
//    public String simpleString() {
//        return value + "\u2212";
//    }
//
//}
