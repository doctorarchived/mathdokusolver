package com.bradym.android.mathdokusolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bradym.android.mathdokusolver.logic.GACSolver;
import com.bradym.android.mathdokusolver.logic.Variable;
import com.bradym.android.mathdokusolver.logic.constraint.Constraint;
import com.bradym.android.mathdokusolver.logic.constraint.DiffConstraint;
import com.bradym.android.mathdokusolver.logic.constraint.DivConstraint;
import com.bradym.android.mathdokusolver.logic.constraint.MinusConstraint;
import com.bradym.android.mathdokusolver.logic.constraint.PlusConstraint;
import com.bradym.android.mathdokusolver.logic.constraint.ProductConstraint;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 6/19/2015.
 *
 * Activity that contains grid
 */
public class PuzzleActivity extends AppCompatActivity implements ConstraintDialog.TrueDialogListener {

    private PuzzleType mode = PuzzleType.KENKEN;

    private int num_col = 6;
    private PuzzleGrid puzzleGrid;
    private List<PuzzleCell> puzzleCells;
    private List<Variable> variables;

    private Deque<State> history;
    private Deque<State> undoHistory;

    private ImageButton solverButton;
    private ImageButton undoButton;
    private ImageButton redoButton;
    private ImageButton clearButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductConstraint.op  = getString(R.string.times);
        MinusConstraint.op = getString(R.string.minus);
        PlusConstraint.op = getString(R.string.plus);
        DivConstraint.op = getString(R.string.div);

        setContentView(R.layout.true_activity);
        puzzleGrid = (PuzzleGrid) findViewById(R.id.trueGrid);

        Intent intent = getIntent();
        num_col = intent.getIntExtra("size", num_col);

        int secret = -1;
        if (num_col >= 420) {
            secret = num_col - 420;
            if (num_col == 425) {
                num_col = 4;
            } else {
                num_col = 9;
            }
        } else if (num_col == 0) {
            num_col = 9;
            mode = PuzzleType.SUDOKU;
            puzzleGrid.setPuzzle(PuzzleType.SUDOKU);
        }


        solverButton = (ImageButton) findViewById(R.id.solveButton);
        undoButton = (ImageButton) findViewById(R.id.undoButton);
        redoButton = (ImageButton) findViewById(R.id.redoButton);
        clearButton = (ImageButton) findViewById(R.id.clearButton);


        puzzleCells = new ArrayList<>();
        puzzleGrid.setColumnCount(num_col);
        puzzleGrid.setRowCount(num_col);


        variables = new ArrayList<>(num_col*num_col);
        history = new ArrayDeque<>();
        undoHistory = new ArrayDeque<>();

        //Initialize variables and cells
        for (int i = 0; i < num_col*num_col; i++) {
            PuzzleCell tc = new PuzzleCell(this);
            tc.puzzleType = mode;
            variables.add(i, new Variable(i, tc, num_col));
            tc.setAttributes(variables.get(i), i);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i / num_col,1f), GridLayout.spec(i % num_col,1f));
            puzzleGrid.addView(tc, params);
            params.width = 0;
            params.height = 0;
            puzzleCells.add(tc);
        }

        //Initialize Row/Column constraints
        for(int i = 0; i < num_col; i++) {
            List<Variable> rowVars = new ArrayList<>();
            List<Variable> colVars = new ArrayList<>();
            for(int j = 0; j < num_col; j++) {
                rowVars.add(variables.get(num_col*i + j));
                colVars.add(variables.get(i + num_col*j));
            }
            new DiffConstraint(rowVars);
            new DiffConstraint(colVars);
        }

        if (mode == PuzzleType.SUDOKU) {
            for (int i = 0; i < 9; i++) {
                List<Variable> tVars = new ArrayList<>();
                int start = (3 * (i % 3)) + (27 * (i / 3));
                for (int j = 0; j < 9; j++) {
                    int add = ((j % 3) + (9 * (j / 3)));
                    tVars.add(variables.get(start + add));
                }
                puzzleGrid.addConstraint(new DiffConstraint(tVars), false);
            }
        }



        adjustButtons(false, undoButton, redoButton, solverButton, clearButton);
        checkSolverButton();

        if (secret != -1) {
            if (secret == 0) secret();
            else if (secret == 1) secret2();
            else if (secret == 2) secret3();
            else if (secret == 3) secret4();
            else if (secret == 4) secret5();
            else if (secret == 5) secret6();
            checkSolverButton();
        }

    }

    private void newConstraint(int type, int val, int... vars) {
        Constraint constraint = null;
        List<Variable> tVars = new ArrayList<>();
        for (int v : vars) {
            tVars.add(variables.get(v));
        }

        switch (type) {
            case 0:
                constraint = new PlusConstraint(tVars, val);
                break;
            case 1:
                constraint = new ProductConstraint(tVars, val);
                break;
            case 2:
                constraint = new MinusConstraint(tVars, val);
                break;
            case 3:
                constraint = new DivConstraint(tVars, val);
                break;
            default:
                break;
        }
        if (constraint != null) {
            puzzleGrid.addConstraint(constraint);
        }
    }

    private void secret() {
        //74226

        newConstraint(2, 1, 0, 1);
        newConstraint(2, 1, 2, 3);
        newConstraint(1, 3024, 4, 13, 21, 22);
        newConstraint(1, 16, 5, 6, 14);
        newConstraint(3, 2, 7, 16);
        newConstraint(1, 12, 8, 17);
        newConstraint(0, 16, 9, 10);
        newConstraint(2, 4, 11, 12);
        newConstraint(2, 1, 15, 24);
        newConstraint(0, 12, 18, 19, 20);
        newConstraint(2, 1, 23, 32);
        newConstraint(0, 15, 25, 34);
        newConstraint(2, 7, 26, 35);
        newConstraint(2, 5, 27, 28);
        newConstraint(1, 126, 29, 30, 38);
        newConstraint(3, 3, 31, 40);
        newConstraint(0, 5, 33);
        newConstraint(2, 3, 36, 45);
        newConstraint(3, 2, 37, 46);
        newConstraint(0, 4, 39);
        newConstraint(1, 144, 41, 42, 51, 52);
        newConstraint(1, 280, 43, 44, 53);
        newConstraint(1, 28, 47, 48);
        newConstraint(1, 40, 49, 50, 58);
        newConstraint(2, 1, 54, 55);
        newConstraint(0, 17, 56, 57);
        newConstraint(0, 15, 59, 68);
        newConstraint(0, 10, 60, 61, 69);
        newConstraint(2, 1, 62, 71);
        newConstraint(0, 15, 63, 72, 73);
        newConstraint(2, 1, 64, 65);
        newConstraint(2, 7, 66, 67);
        newConstraint(0, 7, 70);
        newConstraint(0, 5, 74, 75);
        newConstraint(0, 5, 76);
        newConstraint(2, 2, 77, 78);
        newConstraint(2, 7, 79, 80);
    }

    private void secret2() {
        //37387
        newConstraint(0, 8, 0, 9);
        newConstraint(1, 192, 1, 2, 3, 4);
        newConstraint(1, 1440, 5, 6, 15, 16);
        newConstraint(0, 19, 7, 8, 17);
        newConstraint(1, 135, 10, 11, 20);
        newConstraint(2, 6, 12, 21);
        newConstraint(0, 14, 13, 14, 22);
        newConstraint(2, 2, 18, 19);
        newConstraint(1, 540, 23, 32, 41, 50);
        newConstraint(0, 8, 24, 25, 26);
        newConstraint(1, 192, 27, 28, 36);
        newConstraint(0, 22, 29, 38, 47);
        newConstraint(3, 2, 30, 31);
        newConstraint(1, 270, 33, 34, 35);
        newConstraint(0, 11, 37, 46);
        newConstraint(2, 5, 39, 48);
        newConstraint(2, 2, 40, 49);
        newConstraint(1, 224, 42, 43, 52, 61);
        newConstraint(1, 32, 44, 53, 62);
        newConstraint(2, 3, 45, 54);
        newConstraint(0, 5, 51, 59, 60);
        newConstraint(0, 7, 55);
        newConstraint(3, 2, 56, 65);
        newConstraint(2, 3, 57, 66);
        newConstraint(0, 19, 58, 67, 68);
        newConstraint(2, 8, 63, 64);
        newConstraint(0, 18, 69, 78, 70);
        newConstraint(1, 108, 71, 79, 80);
        newConstraint(0, 7, 72, 73, 74);
        newConstraint(0, 15, 75, 76, 77);
    }

    private void secret3() {
        //74049
        newConstraint(1, 336, 0, 1, 2);
        newConstraint(0, 15, 3, 4 ,5 ,6 ,7);
        newConstraint(1, 45, 8, 17);
        newConstraint(1, 360, 9, 18, 19, 20);
        newConstraint(0, 9, 10);
        newConstraint(2, 5, 11, 12);
        newConstraint(2, 3, 13, 14);
        newConstraint(1, 8, 15, 16, 25);
        newConstraint(2, 1, 21, 22);
        newConstraint(3, 2, 23, 24);
        newConstraint(2, 3, 26, 35);
        newConstraint(2, 8, 27, 28);
        newConstraint(0, 10, 29, 30);
        newConstraint(0, 15, 31, 32, 33);
        newConstraint(0, 13, 34, 42, 43);
        newConstraint(3, 2, 36, 37);
        newConstraint(2, 8, 38,39);
        newConstraint(0, 19, 40, 41, 49, 50);
        newConstraint(1, 6, 44, 53);
        newConstraint(3, 2, 45, 46);
        newConstraint(1, 15, 47, 56);
        newConstraint(0, 6, 48);
        newConstraint(2, 1, 51, 52);
        newConstraint(0, 12, 54, 55);
        newConstraint(3, 4, 57, 58);
        newConstraint(2, 5, 59, 68);
        newConstraint(0, 15, 60, 61);
        newConstraint(0, 15, 62, 70, 71);
        newConstraint(0, 15, 63, 64, 65, 66, 67);
        newConstraint(2, 2, 69, 78);
        newConstraint(3, 2, 72, 73);
        newConstraint(3, 2, 74, 75);
        newConstraint(1, 63, 76, 77);
        newConstraint(2, 4, 79, 80);
    }

    private void secret4() {
        //74063
        newConstraint(1, 1008, 0, 1, 10, 19);
        newConstraint(0, 6, 2, 3, 4);
        newConstraint(2, 5, 5, 6);
        newConstraint(1, 120, 7, 8, 16);
        newConstraint(2, 5, 9, 18);
        newConstraint(1, 48, 11, 12, 20);
        newConstraint(1, 90, 13, 14, 15);
        newConstraint(0, 1, 17);
        newConstraint(2, 1, 21, 22);
        newConstraint(1, 35, 23, 31, 32, 40);
        newConstraint(2, 1, 24, 33);
        newConstraint(2, 3, 25, 34);
        newConstraint(0 , 7, 26, 35);
        newConstraint(0, 13, 27, 36);
        newConstraint(0, 24, 28, 29, 37, 38);
        newConstraint(0, 17, 30, 39);
        newConstraint(0, 7, 41, 42);
        newConstraint(1, 28, 43, 44, 53);
        newConstraint(0, 5, 45);
        newConstraint(3, 3, 46, 47);
        newConstraint(1, 24, 48, 49);
        newConstraint(1, 56, 50, 51, 52);
        newConstraint(2, 5, 54, 63);
        newConstraint(2, 8, 55, 56);
        newConstraint(2, 1, 57, 58);
        newConstraint(2, 5, 59, 60);
        newConstraint(1, 48, 61, 62);
        newConstraint(2, 4, 64, 65);
        newConstraint(0, 2, 66);
        newConstraint(2, 3, 67, 68);
        newConstraint(0, 15, 69, 77, 78);
        newConstraint(2, 1, 70, 71);
        newConstraint(0, 15, 72, 73, 74, 75, 76);
        newConstraint(1, 63, 79, 80);
    }

    private void secret5() {
        //73916
        newConstraint(0, 6, 0, 1);
        newConstraint(1, 168, 2, 3, 4);
        newConstraint(0, 3, 5);
        newConstraint(1, 144, 6, 7, 8);
        newConstraint(0, 14, 9, 18);
        newConstraint(2, 2, 10, 19);
        newConstraint(0, 16, 11, 12, 13);
        newConstraint(2, 3, 14, 15);
        newConstraint(1, 32, 16, 17, 26);
        newConstraint(2, 8, 20, 29);
        newConstraint(0, 12, 21, 30, 31);
        newConstraint(1, 10, 22, 23);
        newConstraint(1, 35, 24, 33);
        newConstraint(0, 17, 25, 34, 35);
        newConstraint(1, 42, 27, 36, 28);
        newConstraint(2, 1, 32, 41);
        newConstraint(1, 45, 37, 38);
        newConstraint(2, 6, 39, 48);
        newConstraint(1, 24, 40, 49, 50);
        newConstraint(3, 2, 42, 51);
        newConstraint(3, 4, 43, 44);
        newConstraint(2, 3, 45 ,46);
        newConstraint(0, 8, 47);
        newConstraint(1, 315, 52, 53, 62);
        newConstraint(1, 112, 54, 55, 63);
        newConstraint(2, 5, 56, 57);
        newConstraint(0, 12, 58, 59, 67);
        newConstraint(3, 2, 60, 61);
        newConstraint(0, 24, 64, 65, 66);
        newConstraint(3, 3, 68, 69);
        newConstraint(0, 20, 70, 78, 79);
        newConstraint(2, 4, 71, 80);
        newConstraint(0, 7, 72, 73);
        newConstraint(0, 7, 74, 75);
        newConstraint(2, 7, 76, 77);
    }

    private void secret6() {
        newConstraint(2, 2, 0, 4);
        newConstraint(1, 6, 1, 2);
        newConstraint(0, 8, 3, 6, 7);
        newConstraint(2, 3, 5, 9);
        newConstraint(0, 4, 8, 12);
        newConstraint(3, 2, 10, 11);
        newConstraint(3, 2, 13, 14);
        newConstraint(0, 3, 15);
    }

    private void adjustButtons(boolean enable, ImageButton... buttons) {
        for (ImageButton b : buttons) {
            if (enable) {
                b.setClickable(true);
                b.setAlpha(1.0f);
            } else {
                b.setClickable(false);
                b.setAlpha(0.25f);

            }
        }
    }

    public void onUndo(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        State state;
        if ((state = history.pollLast()) != null) {
            switch (state.getAction()) {
                case State.ADD_CONSTRAINT:
                    for (Constraint constraint : state.getConstraints()) {
                        puzzleGrid.deleteConstraint(constraint);
                    }
                    undoHistory.offerLast(state);
                    adjustButtons(true, redoButton);
                    break;

                case State.DELETE_CONSTRAINT:
                    for (Constraint constraint: state.getConstraints()) {
                        puzzleGrid.addConstraint(constraint);
                    }
                    undoHistory.offerLast(state);
                    adjustButtons(true, redoButton);
                    break;

                case State.EDIT_CONSTRAINT:
                    puzzleGrid.deleteConstraint(state.getConstraints().get(1));
                    puzzleGrid.addConstraint(state.getConstraints().get(0));
                    undoHistory.offerLast(state);
                    adjustButtons(true, redoButton);
                    break;
            }
        }

        if (history.isEmpty()) {
            adjustButtons(false, undoButton);
        }

        checkSolverButton();
        checkClearButton();
    }
    public void onRedo(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        State cs;
        if ((cs = undoHistory.pollLast()) != null) {
            switch (cs.getAction()) {
                case State.ADD_CONSTRAINT:
                    for (Constraint constraint : cs.getConstraints()) {
                        puzzleGrid.addConstraint(constraint);
                    }
                    history.offerLast(cs);
                    adjustButtons(true, undoButton);
                    break;

                case State.DELETE_CONSTRAINT:
                    for (Constraint constraint: cs.getConstraints()) {
                        puzzleGrid.deleteConstraint(constraint);
                    }
                    history.offerLast(cs);
                    adjustButtons(true, undoButton);
                    break;

                case State.EDIT_CONSTRAINT:
                    puzzleGrid.deleteConstraint(cs.getConstraints().get(0));
                    puzzleGrid.addConstraint(cs.getConstraints().get(1));
                    history.offerLast(cs);
                    adjustButtons(true, undoButton);
                    break;
            }
        }
        if (undoHistory.isEmpty()) {
            adjustButtons(false, redoButton);
        }

        checkSolverButton();
        checkClearButton();
    }


    public void onSolveClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        GACSolver solver = new GACSolver(variables, mode);
        SolverTask st = new SolverTask(this);
        st.execute(solver);
    }

    public void onClearClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        State state = new State(State.DELETE_CONSTRAINT);

        fullReset();
        //Gets around concurrent modification exception
        for (Constraint tc : new ArrayList<>(puzzleGrid.getActiveConstraints())) {
            state.addConstraints(tc);
            for (Variable tv : tc.scope) {
                tv.cell.refreshValue();
            }
            puzzleGrid.deleteConstraint(tc);
        }

        history.offerLast(state);
        undoHistory.clear();
        puzzleGrid.editable = true;
        adjustButtons(false, redoButton);
        adjustButtons(true, undoButton);
        checkSolverButton();
        checkClearButton();
    }

    public void onPositiveClick(State cs) {
        switch (cs.getAction()) {
            case State.ADD_CONSTRAINT:
                for (Constraint tc : cs.getConstraints()) {
                    puzzleGrid.addConstraint(tc);
                }
                history.offerLast(cs);
                break;

            case State.DELETE_CONSTRAINT:
                for (Constraint tc: cs.getConstraints()) {
                    puzzleGrid.deleteConstraint(tc);
                }
                history.offerLast(cs);
                break;

            case State.EDIT_CONSTRAINT:
                puzzleGrid.deleteConstraint(cs.getConstraints().get(0));
                puzzleGrid.addConstraint(cs.getConstraints().get(1));
                history.offerLast(cs);
                break;
        }
        adjustButtons(false, redoButton);
        adjustButtons(true, undoButton);
        checkSolverButton();
        checkClearButton();
        undoHistory.clear();
    }

    private void checkSolverButton() {
        boolean allConstrained = true;
        for (Variable tv : variables) {
            if (tv.constraints.size() != 3) {
                allConstrained = false;
                break;
            }
        }
        if (allConstrained || mode == PuzzleType.SUDOKU) {
            adjustButtons(true, solverButton);
        } else {
            adjustButtons(false, solverButton);
        }
    }

    private void checkClearButton() {
        if (puzzleGrid.getActiveConstraints().isEmpty()) {
            adjustButtons(false, clearButton);
        } else {
            adjustButtons(true, clearButton);
        }
    }

    private void fullReset() {
        Set<Constraint> reset = new HashSet<>();
        for (Variable variable : variables) {
            variable.reset();
            variable.cell.refreshValue();
            for (Constraint constraint : variable.constraints) {
                reset.add(constraint);
            }
        }
        for (Constraint constraint : reset) {
            constraint.reset();
        }
    }

    public void onSolve(Long result) {

        if (result != -1L) {
            Toast.makeText(this, "Solved in " + result + " ms", Toast.LENGTH_SHORT).show();
            for (PuzzleCell tc : puzzleCells) {
                tc.refreshValue();
            }
            history.clear();
            undoHistory.clear();
            adjustButtons(false, redoButton, undoButton, solverButton);
            puzzleGrid.editable = false;
            adjustButtons(true, clearButton);
        } else {
            Toast.makeText(this, "Unable to solve", Toast.LENGTH_SHORT).show();
            fullReset();
            checkSolverButton();
            checkClearButton();
        }
    }
}
