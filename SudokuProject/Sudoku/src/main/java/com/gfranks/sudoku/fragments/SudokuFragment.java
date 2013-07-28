package com.gfranks.sudoku.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;

import com.gfranks.sudoku.R;
import com.gfranks.sudoku.activities.MainActivity;
import com.gfranks.sudoku.utils.SudokuGame;
import com.gfranks.sudoku.utils.SudokuGames;
import com.gfranks.sudoku.widgets.SudokuBoard;

public class SudokuFragment extends Fragment {

    public static final String TAG       = "SudokuFragment";
    public static final String ARGUMENT  = "argument";
    public static final int NEW_GAME     = 0;
    public static final int RESTART_GAME = 1;
    public static final int SHOW_TIMER   = 2;
    public static final int CHECK_WIN    = 3;

    private View container;
    private SudokuBoard sudokuBoard;
    private RelativeLayout timerLayout;
    private Button startStopTimer, checkForWin;
    private Chronometer timer;
    long timeWhenStopped = 0;

    private boolean is_timer_shown = false;
    private boolean is_timer_started = false;

    private static final String KEY_CURRENT_GAME = "current_sudoku_game";

    public SudokuFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = inflater.inflate(R.layout.fragment_sudoku, container, false);

        sudokuBoard = (SudokuBoard)this.container.findViewById(R.id.sudoku_grid);
        sudokuBoard.setupGrids();
        timerLayout = (RelativeLayout)this.container.findViewById(R.id.sudoku_timer_layout);
        startStopTimer = (Button)this.container.findViewById(R.id.sudoku_timer_button);
        startStopTimer.setOnClickListener(StartStopTimerListener);
        timer = (Chronometer)this.container.findViewById(R.id.sudoku_timer);

        loadNewGame();

        return this.container;
    }

    public void loadArgument(int arg) {
        switch (arg) {
            case NEW_GAME:
                loadNewGame();
                break;
            case RESTART_GAME:
                restartCurrentGame();
                break;
            case SHOW_TIMER:
                if (is_timer_shown) {
                    timerLayout.setVisibility(View.GONE);
                    ((MainActivity)getActivity()).timerHidden();
                } else {
                    timerLayout.setVisibility(View.VISIBLE);
                    ((MainActivity)getActivity()).timerShown();
                }

                is_timer_shown = !is_timer_shown;

                sudokuBoard.is_timer_shown = is_timer_shown;
                break;
            case CHECK_WIN:
                boolean isAWin = sudokuBoard.isAWin();
                String message = "Sorry, but you messed up somewhere along the way!!";
                if (isAWin) {
                    SharedPreferences prefs = getActivity().getSharedPreferences(KEY_CURRENT_GAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = prefs.edit();
                    ed.remove(KEY_CURRENT_GAME);
                    ed.commit();
                    message = "Congratulations!! You have successfully completed the board! Start a new game?";
                }

                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle("Game Status");
                dialog.setMessage(message);
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, isAWin ? "NO" : "Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                if (isAWin) {
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            loadNewGame();
                        }
                    });
                }
                Log.e(TAG + ".CheckForWin", "Is a win: " + isAWin);
                break;
            default:
                break;
        }
    }

    public void loadNewGame() {
        SharedPreferences prefs = getActivity().getSharedPreferences(KEY_CURRENT_GAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_CURRENT_GAME, null);
        if (json != null) {
            Log.e(TAG + ".loadNewGame", "Loading Current Game");
            SudokuGame game = new SudokuGame(json);
            SudokuGames.getInstance().setCurrentGame(game);
            sudokuBoard.setCurrentGame(game);
        } else {
            Log.e(TAG + ".loadNewGame", "Loading New Game");
            sudokuBoard.setCurrentGame(SudokuGames.getInstance().getNewGame());
        }
    }

    public void restartCurrentGame() {
        sudokuBoard.reloadCurrentGame();
    }

    public void orienationChanged() {

    }

    public void undoLastMove() {
        sudokuBoard.undoLastMove();
    }

    public void storeCurrentGame() {
        Log.e(TAG + ".storeCurrentGame", "Storing Current Game");
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences(KEY_CURRENT_GAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putString(KEY_CURRENT_GAME, SudokuGames.getInstance().getCurrentGame().getGameAsJSON());
            ed.commit();
        } catch (NullPointerException e) {
            // this throws a NPE even though it is successfull, fix is still in progress
            e.printStackTrace();
        }
    }

    private View.OnClickListener StartStopTimerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!is_timer_started) {
                timer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                timer.start();
                startStopTimer.setText("Pause");
            } else {
                timeWhenStopped = timer.getBase() - SystemClock.elapsedRealtime();
                timer.stop();
                startStopTimer.setText("Start");
            }

            is_timer_started = !is_timer_started;
        }
    };
}
