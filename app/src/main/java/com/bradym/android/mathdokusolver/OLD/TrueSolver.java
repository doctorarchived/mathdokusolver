//package com.bradym.android.mathdokusolver.OLD;
//
//import android.util.Log;
//
//import com.bradym.android.mathdokusolver.OLD.DiffConstraint;
//import com.bradym.android.mathdokusolver.OLD.DivConstraint;
//import com.bradym.android.mathdokusolver.OLD.HashQueue;
//import com.bradym.android.mathdokusolver.OLD.MinusConstraint;
//import com.bradym.android.mathdokusolver.OLD.PlusConstraint;
//import com.bradym.android.mathdokusolver.OLD.ProdConstraint;
//import com.bradym.android.mathdokusolver.OLD.TrueConstraint;
//import com.bradym.android.mathdokusolver.OLD.TrueVariable;
//import com.google.common.collect.ForwardingQueue;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Queue;
//import java.util.Set;
//
///**
// * Created by Michael on 6/7/2015.
// *
// * Solves the puzzle using a slightly modified GAC algorithm
// *
// */
//public class TrueSolver {
//
//    final List<TrueVariable> variables;
//    final HashQueue hashQueue;
//    final Queue<TrueConstraint> GACQueue = new SetQueue();
//    final LinkedHashSet<TrueConstraint> GACQueue2 = new LinkedHashSet<>();
//
//    long solveTime = 0L;
//
//    long assignVariableTime = 0L;
//    long updateVariableTime = 0L;
//    long validateTime = 0L;
//    long enforceGACTime = 0L;
//    long restoreTime = 0L;
//    long valueCleanUpTime = 0L;
//
//    long enforceGACPollTime = 0L;
//    long enforceGACAccessTime = 0L;
//    long enforceGACValidateTime = 0L;
//    long enforceGACPruneTime = 0L;
//    long enforceGACCleanUpTime = 0L;
//
//    public TrueSolver(TrueVariable[] variables) {
//        this.variables = new ArrayList<>(variables.length);
//        this.variables.addAll(Arrays.asList(variables));
//        this.hashQueue = new HashQueue(variables, variables[0].domain.size() + 1);
//    }
//
//    public long solveGAC() {
//        long s = System.currentTimeMillis();
//        boolean success = GAC();
//        solveTime = System.currentTimeMillis() - s;
//
//        Log.d("UPDATE VARIABLE TIME", updateVariableTime + "");
//        Log.d("VALIDATE TIME", validateTime + "");
//        Log.d("ENFORCE GAC TIME", enforceGACTime + "");
//        Log.d("RESTORE TIME", restoreTime + "");
//        Log.d("VALUE CLEANUP TIME", valueCleanUpTime + "");
//
//        Log.d("ENFORCE GAC POLL", enforceGACPollTime + "");
//        Log.d("ENFORCE GAC ACCESS", enforceGACAccessTime + "");
//        Log.d("ENFORCE GAC VALIDATE", enforceGACValidateTime + "");
//        Log.d("ENFORCE GAC PRUNE", enforceGACPruneTime + "");
//        Log.d("ENFORCE GAC CLEAN UP", enforceGACCleanUpTime + "");
//
//        Log.d("DIFF TIME", DiffConstraint.validateTime + "");
//        Log.d("DIV TIME", DivConstraint.validateTime + "");
//        Log.d("MINUS TIME", MinusConstraint.validateTime + "");
//        Log.d("PLUS TIME", PlusConstraint.validateTime + "");
//        Log.d("PROD TIME", ProdConstraint.validateTime + "");
//
//        Log.d("FULL SOLVE", solveTime + "");
//        GACQueue.clear();
//
//
//        if(success == false) {
//            return -1;
//        } else {
//            for (TrueVariable tv : variables) {
//                tv.completeRestore();
//                for (TrueConstraint tc : tv.constraints) {
//                    tc.restore();
//                }
//            }
//            return solveTime;
//        }
//    }
//
//
//    private boolean GAC() {
//
//        long s;
//        if (hashQueue.indices.isEmpty()) {
//            Log.d("GAC", "COMPLETE");
//            return true;
//        }
//
//        TrueVariable var = hashQueue.dequeue();
//
//        for (int i = var.nextValid(0); i != -1; i = var.nextValid(i + 1)) {
//            //Assigns the domain value at index i.
//            //Updates the domain, min, and max of the variable
//            //Updates the current state of the constraints attached to this variable
//            boolean valid = var.assign(i);
//
//            //If the assignment is valid (doesn't break any constraints), begin enforcing GAC
//            if (valid) {
//                GACQueue.addAll(var.constraints);
//                HashSet<TrueVariable> alteredVars = new HashSet<>();
//                if (enforce_GAC(alteredVars)) {
//                    if (GAC()) {
//                        return true;
//                    }
//                }
//
//                for (TrueVariable altered : alteredVars) {
//                    altered.restore();
//                    hashQueue.update(altered);
//                }
//                var.restore();
//                var.value = -1;
//            }
//        }
//        hashQueue.enqueue(var);
//        return false;
//    }
//
//    private boolean enforce_GAC(HashSet<TrueVariable> alteredVars) {
//        HashSet<TrueConstraint> constraints = new HashSet<>();
//        while (!GACQueue.isEmpty()) {
//            TrueConstraint constraint = GACQueue.poll();
//
//            for (TrueVariable var : constraint.scope) {
//
//                for (int j = var.nextValid(0); j != -1; j = var.nextValid(j + 1)) {
//                    boolean valid = constraint.validate(var, j+1);
//
//                    if (!valid) {
//                        var.prune(j);
//                        constraints.add(constraint);
//                        alteredVars.add(var);
//
//                        if (var.domain.size == 0) {
//                            GACQueue.clear();
//
//                            Iterator<TrueVariable> it = alteredVars.iterator();
//                            while (it.hasNext()) {
//                                it.next().restoreDomain();
//                                it.remove();
//                            }
//                        }
//                    }
//
//                }
//
//
//
//            }
//
//        }
//        for (TrueConstraint tc : constraints) {
//
//        }
//        return true;
//
//    }
//
//
//
//
//    private boolean old(int level) {
//        long s;
//        //boolean allAssigned = hashQueue.indices.size() == 0;
//        if (hashQueue.indices.size() == 0) {
//            Log.d("ALL ASSIGNED", "MUCH WOW");
//            return true;
//        }
//
//        TrueVariable v = hashQueue.dequeue();
//
//        //Log.d("LEVEL " + level, "VARIABLE " +  v.detail());
//
//        //i represents the variable value (i+1)
//        for (int i = v.nextValid(0); i != -1; i = v.nextValid(i + 1)) {
//
//            s = System.currentTimeMillis();
//            v.assign(i);
//            boolean valid = true;
//            List<TrueConstraint> altered = new ArrayList<>();
//            assignVariableTime += (System.currentTimeMillis() - s);
//            s = System.currentTimeMillis();
//
//            //Log.d("VARIABLE ASSIGNED", v.detail() + " LEVEL " + level);
//            for (TrueConstraint tc : v.constraints) {
//                valid = tc.updateVariableAssignment(v, i + 1);
//
//                if (!valid) {
//                    //Log.d("CONSTRAINT INVALIDATED", tc.toString());
//                    break;
//                } else {
//                    //Log.d("CONSTRAINT VALIDATED", tc.toString());
//                    altered.add(tc);
//                }
//                updateVariableTime += (System.currentTimeMillis() - s);
//            }
//
//            if (valid) {
//
//                for (TrueConstraint tc : v.constraints) {
//                    GACQueue.add(tc);
//                }
//
//                HashSet<TrueVariable> set = new HashSet<>();
//                s = System.currentTimeMillis();
//                if (enforce_GAC(set)) {
//                    //Log.d("ENFORCE GAC ", "COMPLETE");
//                    enforceGACTime += (System.currentTimeMillis() - s);
//                    if (GAC()) {
//                        return true;
//                    }
//                } else {
//                    enforceGACTime += (System.currentTimeMillis() - s);
//                }
//                s = System.currentTimeMillis();
//                for (TrueVariable tv : set) {
//                    tv.restore();
//                    hashQueue.update(tv);
//                }
//                restoreTime += (System.currentTimeMillis() - s);
//            }
//
//            s = System.currentTimeMillis();
//            v.restore();
//            v.value = -1;
//            //Log.d("RESTORED VARIABLE", v.detail());
//            for (TrueConstraint c : altered) {
//                c.pop();
//            }
//            valueCleanUpTime += (System.currentTimeMillis() - s);
//        }
//        hashQueue.enqueue(v);
//        return false;
//    }
//
//
//    private boolean enforce_GACOLD(Set<TrueVariable> alteredV) {
//        long s;
//        while (!GACQueue.isEmpty()) {
//            s = System.currentTimeMillis();
//            TrueConstraint constraint = GACQueue.poll();
//            //Log.d("ENFORCE_GAC", "CONSTRAINT " + constraint);
//            enforceGACPollTime += (System.currentTimeMillis() - s);
//            for (TrueVariable v : constraint.unassigned) {
//                s = System.currentTimeMillis();
//                if (v.value != -1) continue;
//                //Log.d("ENFORCE_GAC", "VARIABLE " + v.detail());
//                enforceGACAccessTime += (System.currentTimeMillis() - s);
//                for (int j = v.nextValid(0); j != -1; j = v.nextValid(j + 1)) {
//                    s = System.currentTimeMillis();
//                    boolean found = constraint.validate(v, j+1);
//                    //Log.d("VALID = " + found, constraint.toString());
//                    enforceGACValidateTime += (System.currentTimeMillis() - s);
//                    if (!found) {
//                        s = System.currentTimeMillis();
//                        v.prune(j);
//                        alteredV.add(v);
//
//                        if (v.aDomain == 0) {
//                            GACQueue.clear();
//                            for (TrueVariable tv : alteredV) {
//                                tv.restore();
//                                tv.lock();
//                            }
//                            alteredV.clear();
//                            enforceGACPruneTime += (System.currentTimeMillis() - s);
//                            return false;
//                        } else {
//                            for (TrueConstraint c : v.constraints) {
//                                if (!GACQueue.contains(c)) {
//                                    GACQueue.add(c);
//                                }
//                            }
//                            enforceGACPruneTime += (System.currentTimeMillis() - s);
//                        }
//                    }
//                }
//            }
//        }
//
//        s = System.currentTimeMillis();
//        for (TrueVariable tv : alteredV) {
//            tv.lock();
//            hashQueue.update(tv);
//            //Log.d("UPDATED VARIABLE " + tv + " DOMAIN", tv.aDomain.toString());
//        }
//        enforceGACCleanUpTime += (System.currentTimeMillis() - s);
//        return true;
//    }
//
//    private class SetQueue extends ForwardingQueue<TrueConstraint> {
//
//        private HashSet<TrueConstraint> membership = new HashSet<>();
//        private Queue<TrueConstraint> queue = new ArrayDeque<>();
//
//        @Override
//        protected Queue delegate() {
//            return queue;
//        }
//
//        @Override
//        public boolean offer(TrueConstraint o) {
//            if (!membership.contains(o))
//                return super.offer(o);
//            return false;
//        }
//
//        @Override
//        public boolean add(TrueConstraint o) {
//            if (!membership.contains(o))
//                return super.add(o);
//            return false;
//        }
//    }
//
//}
