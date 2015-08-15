package com.bradym.android.mathdokusolver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bradym.android.mathdokusolver.logic.Variable;
import com.bradym.android.mathdokusolver.logic.constraint.Constraint;
import com.bradym.android.mathdokusolver.logic.constraint.DivConstraint;
import com.bradym.android.mathdokusolver.logic.constraint.MinusConstraint;
import com.bradym.android.mathdokusolver.logic.constraint.PlusConstraint;
import com.bradym.android.mathdokusolver.logic.constraint.ProductConstraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Michael on 6/19/2015.
 *
 * Dialog that allows the user to specify the constraint corresponding to the selected cells
 */
public class ConstraintDialog extends DialogFragment implements View.OnClickListener {


    private String buffer = "";

    private TextView field;

    private TrueDialogListener trueListener;
    private Collection<Variable> scope = new ArrayList<>();
    private HashSet<PuzzleCell> cells;

    private PuzzleType mode = PuzzleType.KENKEN;

    Button plusButton;
    Button minusButton;
    Button timesButton;
    Button divButton;

    ImageButton trashButton;

    private int action;
    private Constraint prevConstraint;
    private State newState = null;

    public void addParameters(HashSet<PuzzleCell> scope, int action, PuzzleType puzzleType) {
        for (PuzzleCell tc : scope) {
            this.scope.add(tc.getVariable());
        }
        this.cells = scope;
        this.action = action;
        this.newState = new State(action);
        this.mode = puzzleType;
    }

    public void addParameters(Constraint constraint, int action, PuzzleType puzzleType) {
        this.prevConstraint = constraint;
        this.action = action;
        this.newState = new State(action, constraint);
        this.scope = constraint.scope;
        this.mode = puzzleType;
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select the constraint");

        View inflated = View.inflate(getActivity(), R.layout.true_dialog, null);

        field = (TextView) inflated.findViewById(R.id.dialogTextView);
        inflated.findViewById(R.id.dialogButton0).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton1).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton2).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton3).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton4).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton5).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton6).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton7).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton8).setOnClickListener(this);
        inflated.findViewById(R.id.dialogButton9).setOnClickListener(this);



        plusButton = (Button) inflated.findViewById(R.id.dialogButtonPlus);
        plusButton.setOnClickListener(this);


        timesButton = (Button) inflated.findViewById(R.id.dialogButtonTimes);
        timesButton.setOnClickListener(this);

        minusButton = (Button) inflated.findViewById(R.id.dialogButtonMinus);
        minusButton.setOnClickListener(this);

        divButton = (Button) inflated.findViewById(R.id.dialogButtonDiv);
        divButton.setOnClickListener(this);

        trashButton = (ImageButton) inflated.findViewById(R.id.deleteButton);
        trashButton.setOnClickListener(this);

        if (mode == PuzzleType.SUDOKU) {
            plusButton.setVisibility(View.GONE);
            timesButton.setVisibility(View.GONE);
            minusButton.setVisibility(View.GONE);
            divButton.setVisibility(View.GONE);
        } else {
            adjustButtons(false, plusButton, timesButton, divButton, minusButton);
        }

        if (action != State.EDIT_CONSTRAINT) {
            adjustButtons(false, trashButton);
        }

        inflated.findViewById(R.id.dialogButtonUndo).setOnClickListener(this);

        Dialog d = builder.setView(inflated).create();
        return d;
    }

    public void adjustButtons(boolean enable, Button ... buttons) {
        for (Button b : buttons) {
            if (enable) {
                b.setClickable(true);
                b.setTextColor(b.getTextColors().withAlpha(255));
            } else {
                b.setClickable(false);
                b.setTextColor(b.getTextColors().withAlpha(64));

            }
        }
    }

    public void adjustButtons(boolean enable, ImageButton ... buttons) {
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

    @Override
    public void onClick(View v) {
        Constraint constraint;

        v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
        switch (v.getId()) {
            case R.id.dialogButton0:
            case R.id.dialogButton1:
            case R.id.dialogButton2:
            case R.id.dialogButton3:
            case R.id.dialogButton4:
            case R.id.dialogButton5:
            case R.id.dialogButton6:
            case R.id.dialogButton7:
            case R.id.dialogButton8:
            case R.id.dialogButton9:

                if (buffer.length() < 9) {
                    buffer += ((TextView) v).getText();
                    field.setText(buffer);
                }

                if (buffer.length() == 1) {
                    if (scope.size() == 1) {
                        if (mode == PuzzleType.SUDOKU) {
                            constraint = new PlusConstraint(scope, Integer.parseInt(buffer));
                            constraint.bordered = false;
                            newState.addConstraints(constraint);
                            trueListener.onPositiveClick(newState);
                            dismiss();
                        }
                        adjustButtons(true, plusButton);
                    } else if (scope.size() > 1) {
                        adjustButtons(true, plusButton, timesButton, divButton, minusButton);
                    }
                }
                break;
            case R.id.dialogButtonUndo:
                buffer = buffer.length() > 0 ? buffer.substring(0, buffer.length() - 1) : buffer;
                field.setText(buffer);

                if (buffer.length() == 0) {
                    adjustButtons(false,plusButton, timesButton, divButton, minusButton );
                }

                break;
            case R.id.dialogButtonDiv:
                constraint = new DivConstraint(scope, Integer.parseInt(buffer));
                newState.addConstraints(constraint);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
            case R.id.dialogButtonPlus:
                constraint = new PlusConstraint( scope, Integer.parseInt(buffer));
                newState.addConstraints(constraint);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
            case R.id.dialogButtonMinus:
                constraint = new MinusConstraint(scope, Integer.parseInt(buffer));
                newState.addConstraints(constraint);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
            case R.id.dialogButtonTimes:
                constraint = new ProductConstraint(scope, Integer.parseInt(buffer));
                newState.addConstraints(constraint);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
            case R.id.deleteButton:
                newState.setAction(State.DELETE_CONSTRAINT);
                trueListener.onPositiveClick(newState);
                dismiss();
                break;
        }
    }

    interface TrueDialogListener {
        void onPositiveClick(State cs);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            trueListener = (TrueDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void dismiss() {
        switch (action) {
            case State.EDIT_CONSTRAINT:
                for (Variable var : prevConstraint.scope) {
                    var.cell.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
            case State.DELETE_CONSTRAINT:
                break;
            case State.ADD_CONSTRAINT:
                for (PuzzleCell tc : cells) {
                    tc.setBackgroundColor(Color.TRANSPARENT);
                }
                cells.clear();
                break;
        }
        super.dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

}
