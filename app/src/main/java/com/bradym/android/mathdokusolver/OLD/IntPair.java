//package com.bradym.android.mathdokusolver.OLD;
//
///**
// * Created by Michael on 7/10/2015.
// */
//public class IntPair {
//    final int top;
//    final int bottom;
//    final boolean topSet;
//
//    protected IntPair(int top, int bottom) {
//        this.top = top;
//        this.bottom = bottom;
//        this.topSet = false;
//    }
//
//    protected IntPair(int top, int bottom, boolean topMod) {
//        this.top = top;
//        this.bottom = bottom;
//        this.topSet = topMod;
//    }
//
//    protected IntPair multiplyTop(int mult) {
//        return new IntPair(top * mult, bottom, true);
//    }
//
//    protected IntPair multiplyBot(int mult) {
//        return new IntPair(top, bottom * mult, topSet);
//    }
//
//    protected IntPair addTop(int add) {
//        return new IntPair(top + add, bottom, true);
//    }
//
//    protected IntPair addBot(int add) {
//        return new IntPair(top, bottom + add, topSet);
//    }
//
//    @Override
//    public String toString() {
//        return "TOP " + top + " BOTTOM " + bottom;
//    }
//}
