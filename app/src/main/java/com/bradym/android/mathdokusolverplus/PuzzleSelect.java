package com.bradym.android.mathdokusolverplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Activity that allows the user to select the puzzle
 *
 */
public class PuzzleSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_select);
    }

    public void sudokuSelect(View v) {
        Intent intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra("puzzle", "sudoku");
        startActivity(intent);
    }

    public void kenkenSizeSelect(View v) {

        String[] sizes = {"3x3", "4x4", "5x5", "6x6", "7x7", "8x8", "9x9"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.GRID_SELECT)
                .setItems(sizes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PuzzleSelect.this, PuzzleActivity.class);
                                intent.putExtra("puzzle", "kenken");
                                intent.putExtra("size", which + 3);
                                startActivity(intent);
                            }
                        }
                );
        builder.create().show();
    }

    public void debug(View v) {
        Intent intent = new Intent(PuzzleSelect.this, PuzzleActivity.class);
        intent.putExtra("puzzle", "kenken");
        intent.putExtra("size", 9);

        switch (v.getId()) {
            case R.id.buttonSecret1:
                intent.putExtra("secret", 0);
                break;
            case R.id.buttonSecret2:
                intent.putExtra("secret", 1);
                break;
            case R.id.buttonSecret3:
                intent.putExtra("secret", 2);
                break;
            case R.id.buttonSecret4:
                intent.putExtra("secret", 3);
                break;
            case R.id.buttonSecret5:
                intent.putExtra("secret", 4);
                break;
        }
        startActivity(intent);
    }
}
