package com.bradym.android.mathdokusolverplus;

import com.bradym.android.mathdokusolverplus.logic.constraint.Constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class State {

    public static final int ADD_CONSTRAINT = 0x0;
    public static final int EDIT_CONSTRAINT = 0x1;
    public static final int DELETE_CONSTRAINT = 0x2;

    private int action;
    private final List<Constraint> constraints = new ArrayList<>();

    /*
    If action is ADD_CONSTRAINT, constraints is an array of all constraints that have been added
    If action is EDIT_CONSTRAINT, constraints[0] is the previous constraint and constraints[1] is the new one
    If action is DELETE_CONSTRAINT, constraints is an array of all deleted constraints
     */
    public State(int action, Constraint... constraints) {
        if (action < 3 && action > -1) {
            this.action = action;
            this.constraints.addAll(Arrays.asList(constraints));
        }
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void addConstraints(Constraint ... constraints) {
        this.constraints.addAll(Arrays.asList(constraints));
    }
}
