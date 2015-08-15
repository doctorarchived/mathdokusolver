//package com.bradym.android.mathdokusolver.OLD;
//
//import com.bradym.android.mathdokusolver.logic.GACState;
//
//import java.util.List;
//
///**
// * Created by Michael on 6/3/2015.
// *
// * Constraint representing Product.
// */
//public class ProdConstraint extends TrueConstraint {
//
//    public ProdConstraint(int value, List<TrueVariable> scope, int num_col) {
//        super(value, scope, num_col, 1);
//        active = new GACState(1);
//    }
//
//    @Override
//    protected int op(int first, int second) {
//        return first * second;
//    }
//
//    public boolean updateVariableAssignment(TrueVariable var, int val) {
//        if ((value % val == 0) && (active.currentValue * val * active.remMin <= value) && (active.currentValue * val * active.remMax >= value)) {
//            temp = new GACState(active.currentValue * val);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean validate(TrueVariable var, int val) {
//        long s = System.currentTimeMillis();
//        boolean b = ((value % val == 0) && (active.currentValue * val * active.remMin <= value) && (active.currentValue * val * active.remMax >= value));
//        validateTime += (System.currentTimeMillis() - s);
//        return b;
//    }
//
//    @Override
//    public void enable() {
//        super.enable();
//        for (TrueVariable tv : scope) {
//            tv.weightedIndex = (tv.weightedIndex + 1);
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
//
//        @Override
//    public String toString() {
//        return "PROD CONSTRAINT | SCOPE = " + scope.toString() + " | " + active;
//    }
//
//    public String simpleString() {
//        return value + "\u00D7";
//    }
//
//}
