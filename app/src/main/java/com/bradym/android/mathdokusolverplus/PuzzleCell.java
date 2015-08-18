package com.bradym.android.mathdokusolverplus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bradym.android.mathdokusolverplus.logic.Variable;

/**
 * Created by Michael on 6/16/2015.
 *
 * Cell that contains two TextViews. One representing the constraint and another representing
 * the actual value
 */
public class PuzzleCell extends RelativeLayout {

    private final Paint m2BorderPaint = new Paint();
    private final Paint m1BorderPaint = new Paint();
    private final Paint m0BorderPaint = new Paint();
    private final Paint[] paints = new Paint[3];

    private Variable var;
    private TextView topLeftView;
    private TextView centreView;
    private int index;

    int right = 0;
    int up = 0;
    int left = 0;
    int down = 0;

    PuzzleType puzzleType = PuzzleType.KENKEN;

    public PuzzleCell(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PuzzleCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PuzzleCell(Context context) {
        this(context, null, 0);
    }

    public int getIndex() {
        return index;
    }

    private void init(Context context) {
        setWillNotDraw(false);

        topLeftView = new TextView(context);
        topLeftView.setTextSize(10);
        topLeftView.setPadding(10, 0, 0, 0);
        RelativeLayout.LayoutParams constraintLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        constraintLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        constraintLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(topLeftView, constraintLayout);

        centreView = new TextView(context);
        centreView.setTextSize(24);
        RelativeLayout.LayoutParams valueLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        valueLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(centreView, valueLayout);


        m2BorderPaint.setStyle(Paint.Style.STROKE);
        m2BorderPaint.setStrokeWidth(6);
        m2BorderPaint.setColor(Color.BLACK);
        paints[2] = m2BorderPaint;

        m1BorderPaint.setStyle(Paint.Style.STROKE);
        m1BorderPaint.setStrokeWidth(3);
        m1BorderPaint.setColor(Color.BLACK);
        paints[1] = m1BorderPaint;

        m0BorderPaint.setStyle(Paint.Style.STROKE);
        m0BorderPaint.setColor(Color.BLACK);
        m0BorderPaint.setStrokeWidth(1);
        paints[0] = m0BorderPaint;


    }

    public void setAttributes(Variable var, int index) {
        this.var = var;
        this.index = index;
    }

    public void setConstraintString(String s) {
        if (puzzleType == PuzzleType.KENKEN) {
            this.topLeftView.setText(s);
        } else if (puzzleType == PuzzleType.SUDOKU){
            this.centreView.setText(s);
        }
    }

    public Variable getVariable() {
        return var;
    }

    public void refreshValue() {
        if (var.value() == -1) {
            this.centreView.setText("");
        } else {
            this.centreView.setText(var.value() + "");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1, paints[right]);
        canvas.drawLine(0, 0, getWidth() - 1, 0, paints[up]);
        canvas.drawLine(0, 0, 0, getHeight() - 1, paints[left]);
        canvas.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, paints[down]);

    }

    public void resetBorders() {
        this.right = 0;
        this.up = 0;
        this.left = 0;
        this.down = 0;
    }


    public void select() {
        setBackgroundColor(Color.LTGRAY);
    }

    public void unSelect() {
        setBackgroundColor(Color.TRANSPARENT);
    }

}
