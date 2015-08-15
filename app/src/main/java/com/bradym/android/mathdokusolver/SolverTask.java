package com.bradym.android.mathdokusolver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.bradym.android.mathdokusolver.logic.GACSolver;

/**
 * Created by Michael on 5/14/2015.
 *
 * Async Task to initiate GAC solve.
 */
public class SolverTask extends AsyncTask<GACSolver, Void, Long> implements DialogInterface.OnCancelListener {

    ProgressDialog pg;
    PuzzleActivity activity;

    public SolverTask(PuzzleActivity activity) {
        this.pg = new ProgressDialog(activity);
        pg.setOnCancelListener(this);
        this.activity = activity;
    }

    GACSolver solver;

    @Override
    protected void onPreExecute() {
        pg.setMessage("Solving...");
        pg.show();
    }


    @Override
    protected Long doInBackground(GACSolver... params) {
        solver = params[0];
        return solver.solve();
    }

    @Override
    protected void onPostExecute(Long result) {
        if (pg.isShowing()) {
            pg.dismiss();
        }
        activity.onSolve(result);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        cancel(true);
    }

    @Override
    public void onCancelled() {
        activity.onSolve(-1L);
    }
}
