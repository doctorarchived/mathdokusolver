//package com.bradym.android.mathdokusolver.OLD;
//
//import java.util.ArrayDeque;
//import java.util.BitSet;
//import java.util.List;
//
///**
// * Created by Michael on 6/5/2015.
// *
// * Represents the All-Different Constraint
// *
// */
//class DiffConstraint extends TrueConstraint {
//
//    private BitSet active;
//    private ArrayDeque<Integer> history;
//    static long validateTime = 0L;
//
//    public DiffConstraint(List<TrueVariable> scope) {
//        super(-1, scope, scope.size());
//        active = new BitSet();
//        history = new ArrayDeque<>();
//        enable();
//    }
//
//
//    @Override
//    public void pop() {
//        active.set(history.pollLast(), false);
//    }
//
//    @Override
//    public void restore() {
//        active.clear();
//        history.clear();
//    }
//
//    @Override
//    public boolean updateVariableAssignment(TrueVariable var, int val) {
//        if (active.get(val-1)) {
//            return false;
//        } else {
//            active.set(val-1);
//            history.addLast(val - 1);
//            return true;
//        }
//    }
//
//    @Override
//    public boolean validate(TrueVariable var, int val) {
//        long s = System.currentTimeMillis();
//        boolean b = !active.get(val-1);
//        validateTime += (System.currentTimeMillis() - s);
//        return b;
//    }
//
//    @Override
//    public String toString() {
//        return "DIFFERENT CONSTRAINT | SCOPE = " + scope.toString() + " | " + active;
//    }
//
//    public String simpleString() {
//        return "";
//    }
//
//}
