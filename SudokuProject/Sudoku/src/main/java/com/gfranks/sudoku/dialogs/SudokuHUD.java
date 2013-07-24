package com.gfranks.sudoku.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gfranks.sudoku.R;
import com.gfranks.sudoku.widgets.SudokuBoard;

import java.util.ArrayList;

/**
 * Created by Garrett on 7/24/13.
 */
public class SudokuHUD  extends Dialog {

    private TextView tv;
    private ProgressBar progressBar;

    public SudokuHUD(Context context) {
        super(context, R.style.PopupDialog);
        setCancelable(true);
//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.popup_hud);

        this.tv = (TextView) findViewById(R.id.hud_textview);
        this.progressBar = (ProgressBar) findViewById(R.id.hud_progress_bar);
    }

    public SudokuHUD(Context context, String progress_text) {
        this(context);
        setProgressText(progress_text);
    }

    public void setProgressText(String progress_text) {
        this.tv.setText(progress_text);
    }
}
