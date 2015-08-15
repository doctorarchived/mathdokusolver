package com.bradym.android.mathdokusolver.logic;

import com.bradym.android.mathdokusolver.PuzzleType;
import com.bradym.android.mathdokusolver.logic.constraint.Constraint;
import com.google.common.collect.ForwardingQueue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Michael on 7/26/2015.
 */
public class GACSolver {

    private SetQueue GACQueue;
    private OrderedVarSet variables;

    public GACSolver(Collection<Variable> variables, PuzzleType puzzle) {
        this.variables = new OrderedVarSet();
        for (Variable v : variables) {
            int key = 0;
            if (puzzle == PuzzleType.KENKEN) {
                key = v.constraints.get(2).scope.size() == 1 ? 0 : v.domainSize() - 1;
            } else if (puzzle == PuzzleType.SUDOKU) {
                key = v.constraints.size() == 4 ? 0 : v.domainSize() - 1;
            }
            this.variables.add(v, key);
        }
        this.GACQueue = new SetQueue();
    }

    public long solve() {
        long start = System.currentTimeMillis();
        boolean success = GAC(0);
        long duration = System.currentTimeMillis() - start;
        return success ? duration : -1L;
    }

    public boolean GAC(int level) {
        if (variables.isEmpty()) {
            return true;
        }
        Variable var = variables.pollMin();

        for (int i = var.nextValid(0); i != -1; i = var.nextValid(i+1)) {

            if (var.assign(i+1)) {
                HashSet<Variable> alteredVars = new HashSet<>();
                GACQueue.addAll(var.constraints);
                boolean enforced = enforceGAC(alteredVars);
                if (enforced) {
                    boolean GACd = GAC(++level);
                    if (GACd) {
                        return true;
                    }
                }
                var.unAssign();
                for (Variable v : alteredVars) {
                    v.restoreDomain();
                    variables.update(v, v.domainSize()-1);
                }
            }
        }
        variables.add(var, var.domainSize()-1);
        return false;
    }

    public boolean enforceGAC(Set<Variable> alteredVars) {
        Constraint constraint;
        while ((constraint = GACQueue.poll()) != null) {

            for (Variable var : constraint.scope) {
                if (var.domain.value != -1) continue;
                boolean pruned = false;
                for (int i = var.nextValid(0); i != -1; i = var.nextValid(i+1)) {
                    boolean valid = constraint.validateAssignment(var, i+1);
                    if (!valid) {
                        alteredVars.add(var);
                        var.prune(i + 1);
                        if (var.domainSize() == 0) {
                            Iterator<Variable> iterator = alteredVars.iterator();
                            while (iterator.hasNext()) {
                                iterator.next().restoreDomain();
                                iterator.remove();
                            }
                            GACQueue.clear();
                            return false;
                        } else {
                            pruned = true;
                        }
                    }
                }
                if (pruned) {
                    for (Constraint c : var.constraints) {
                        GACQueue.add(c);
                    }
                }
            }
        }
        for (Variable v : alteredVars) {
            variables.update(v, v.domainSize()-1);
            v.lock();
        }
        return true;
    }

    private class SetQueue extends ForwardingQueue<Constraint> {

        private HashSet<Constraint> membership = new HashSet<>();
        private Queue<Constraint> queue = new ArrayDeque<>();

        @Override
        protected Queue delegate() {
            return queue;
        }

        @Override
        public boolean offer(Constraint o) {
            if (!membership.contains(o))
                return super.offer(o);
            return false;
        }

        @Override
        public boolean add(Constraint o) {
            if (!membership.contains(o))
                return super.add(o);
            return false;
        }

        @Override
        public Constraint poll() {
            Constraint polled = super.poll();
            membership.remove(polled);
            return polled;
        }

        @Override
        public Constraint remove() {
            Constraint removed = super.remove();
            membership.remove(removed);
            return removed;
        }

    }
}
