package com.bradym.android.mathdokusolverplus;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bradym.android.mathdokusolverplus.logic.Variable;
import com.bradym.android.mathdokusolverplus.logic.constraint.Constraint;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Michael on 6/16/2015.
 *
 * Grid that represents the puzzle stage with touch interactions allowing the manipulation of constraints
 *
 */
@SuppressWarnings("SuspiciousNameCombination")
public class PuzzleGrid extends GridLayout {

    private enum SELECTION {
        CELL,
        CONSTRAINT
    }

    private final HashSet<PuzzleCell> selected = new HashSet<>();
    private Constraint selectedConstraint = null;
    boolean editable = true;

    private Activity context;
    private FragmentManager fm;

    private SELECTION selectionMode = SELECTION.CELL;
    private PuzzleType puzzle = PuzzleType.KENKEN;
    private final HashSet<Constraint> activeConstraints = new HashSet<>();

    public PuzzleGrid(Context context) {
        this(context, null, 0);
    }

    public PuzzleGrid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof Activity) {
            this.context = (Activity) context;
            fm = this.context.getFragmentManager();
        }
    }

    public void setPuzzle(@SuppressWarnings("SameParameterValue") PuzzleType type) {
        this.puzzle = type;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!editable) {
            return false;
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_UP:
                if (context != null) {
                    switch (selectionMode) {
                        case CELL:
                            if (!selected.isEmpty()) {
                                ConstraintDialog sd = new ConstraintDialog();
                                sd.addConstraint(selected, puzzle);
                                sd.show(fm, "Begin add");
                            }
                            break;
                        case CONSTRAINT:
                            if (selectedConstraint != null) {
                                ConstraintDialog sd = new ConstraintDialog();
                                sd.editConstraint(selectedConstraint, puzzle);
                                sd.show(fm, "Begin edit");
                            }
                            break;
                    }
                }

                return true;
            case MotionEvent.ACTION_CANCEL:
                for (PuzzleCell tc : selected) {
                    tc.unSelect();
                }
                selected.clear();
                if (selectedConstraint != null) {
                    for (Variable var : selectedConstraint.scope) {
                        var.cell.unSelect();
                    }
                }
                return true;

            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < getChildCount(); i++) {
                    PuzzleCell cell = (PuzzleCell) getChildAt(i);
                    Rect rect = new Rect();
                    cell.getHitRect(rect);
                    if (rect.contains((int) event.getX(), (int) event.getY())) {
                        Variable var = cell.getVariable();
                        if ((puzzle == PuzzleType.KENKEN && var.constraints.size() == 2) ||
                                (puzzle == PuzzleType.SUDOKU && var.constraints.size() == 3)) {
                            selectionMode = SELECTION.CELL;
                            selected.add(cell);
                            cell.select();
                        } else if ((puzzle == PuzzleType.KENKEN) ||
                                (puzzle == PuzzleType.SUDOKU && var.constraints.size() == 4)) {
                            selectionMode = SELECTION.CONSTRAINT;
                            selectedConstraint = var.constraints.get(2);
                            for (Variable tv : selectedConstraint.scope) {
                                tv.cell.select();
                            }
                        }

                    return true;
                    }
                }
                return false;

            case MotionEvent.ACTION_MOVE:
                if (selectionMode == SELECTION.CELL && puzzle == PuzzleType.KENKEN) {
                    for (int i = 0; i < getChildCount(); i++) {
                        PuzzleCell v = (PuzzleCell) getChildAt(i);
                        Rect rect = new Rect();
                        v.getHitRect(rect);
                        if (rect.contains((int) event.getX(), (int) event.getY()) && v.getVariable().constraints.size() == 2) {
                            for (PuzzleCell tc : selected) {
                                int dim = getColumnCount();
                                int colDiff = Math.abs(tc.getIndex() % dim - v.getIndex() % dim);
                                int rowDiff = Math.abs(tc.getIndex() / dim - v.getIndex() / dim);

                                if (colDiff * rowDiff == 0 && colDiff + rowDiff == 1) {
                                    selected.add(v);
                                    v.select();
                                    return true;
                                }
                            }
                        }
                    }
                }
                return true;
        }
        return false;

    }

    public HashSet<Constraint> getActiveConstraints() {
        return activeConstraints;
    }

    private PuzzleCell getTopLeftCell(Constraint constraint, int num_col) {

        ArrayList<PuzzleCell> nVars = new ArrayList<>(constraint.scope.size());
        PuzzleCell min = null;
        for (Variable var : constraint.scope) {
            PuzzleCell cell = var.cell;
            int i = cell.getIndex();
            if (min == null || i < min.getIndex()) {
                min = cell;
            }
            if (constraint.bordered) {
                cell.up = i / num_col == 0 ? 0 : 2;
                cell.left = i % num_col == 0 ? 0 : 2;
                cell.right = i % num_col == num_col - 1 ? 0 : 2;
                cell.down = i / num_col == num_col - 1 ? 0 : 2;

                for (PuzzleCell tv : nVars) {
                    int diff = i - tv.getIndex();
                    if (diff == 1) {
                        cell.left = 0;
                        tv.right = 0;
                    } else if (diff == -1) {
                        cell.right = 0;
                        tv.left = 0;
                    } else if (diff == -num_col) {
                        cell.down = 0;
                        tv.up = 0;
                    } else if (diff == num_col) {
                        cell.up = 0;
                        tv.down = 0;
                    }
                }
                nVars.add(cell);
                cell.invalidate();
            }
        }
        return min;
    }

    public void addConstraint(Constraint constraint, boolean addToActive) {
        if (addToActive) {
            activeConstraints.add(constraint);
        }
        constraint.enable();

        PuzzleCell min = getTopLeftCell(constraint, getColumnCount());
        if (min != null) {
            min.setConstraintString(constraint.cellString());
        }

    }

    public void addConstraint(Constraint constraint) {
        addConstraint(constraint, true);
    }

    public void deleteConstraint(Constraint constraint) {
        constraint.disable();
        activeConstraints.remove(constraint);

        for (Variable tv : constraint.scope) {
            PuzzleCell cell = tv.cell;
            if (constraint.bordered) cell.resetBorders();
            cell.setConstraintString("");
            cell.invalidate();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }

}
