package com.gfranks.sudoku.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.gfranks.sudoku.R;
import com.gfranks.sudoku.widgets.SudokuBoard;

import java.util.ArrayList;

/**
 * Created by Garrett on 7/5/13.
 */
public class SudokuMoveDialog extends Dialog {

    public static enum SudokuMoveDialogType {
        SudokuMoveDialogTypePicker,
        SudokuMoveDialogTypeSelector;
    };
    private TextView tv;
    private ArrayList<SudokuBoard.SudokuMove> moves;
    private OnSudokuMoveSelectedListener listener;

    public SudokuMoveDialog(Context context, SudokuMoveDialogType type, TextView tv, ArrayList<SudokuBoard.SudokuMove> moves, OnSudokuMoveSelectedListener listener) {
        super(context, R.style.PopupDialog);
        this.tv = tv;
        this.moves = moves;
        this.listener = listener;
        setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        switch(type) {
            case SudokuMoveDialogTypePicker:
                setContentView(R.layout.popup_numberpicker);
                setupPicker();
                break;
            case SudokuMoveDialogTypeSelector:
                setContentView(R.layout.popup_numberselector);
                setupSelector();
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        tv.setBackgroundResource(R.drawable.tv_active_bg);
    }

    @Override
    public void dismiss() {
        tv.setBackgroundResource(R.drawable.tv_normal_bg);
        super.dismiss();
    }

    public void setTextView(TextView tv) {
        this.tv = tv;
    }

    public void setupPicker() {
        final NumberPicker np = (NumberPicker)findViewById(R.id.numberPicker);
        np.setMaxValue(9);
        np.setMinValue(1);
        np.setFocusable(true);
        np.setFocusableInTouchMode(true);
        ((Button)findViewById(R.id.numberPickerDone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                moves.add(new SudokuBoard.SudokuMove(tv, tv.getText().toString(), String.valueOf(np.getValue())));
                tv.setText(String.valueOf(np.getValue()));
                tv.setTextColor(getContext().getResources().getColor(R.color.green));
            }
        });
    }

    public void setupSelector() {
        final Button num1 = (Button)findViewById(R.id.num1);
        final Button num2 = (Button)findViewById(R.id.num2);
        final Button num3 = (Button)findViewById(R.id.num3);
        final Button num4 = (Button)findViewById(R.id.num4);
        final Button num5 = (Button)findViewById(R.id.num5);
        final Button num6 = (Button)findViewById(R.id.num6);
        final Button num7 = (Button)findViewById(R.id.num7);
        final Button num8 = (Button)findViewById(R.id.num8);
        final Button num9 = (Button)findViewById(R.id.num9);
        View.OnClickListener numberButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                moves.add(new SudokuBoard.SudokuMove(tv, tv.getText().toString(), ((Button)v).getText().toString()));
                tv.setText(((Button) v).getText().toString());
                tv.setTextColor(getContext().getResources().getColor(R.color.green));

                if (listener != null) {
                    listener.didSelectSudokuValue(tv, tv.getText().toString());
                }
            }
        };
        num1.setOnClickListener(numberButtonListener);
        num2.setOnClickListener(numberButtonListener);
        num3.setOnClickListener(numberButtonListener);
        num4.setOnClickListener(numberButtonListener);
        num5.setOnClickListener(numberButtonListener);
        num6.setOnClickListener(numberButtonListener);
        num7.setOnClickListener(numberButtonListener);
        num8.setOnClickListener(numberButtonListener);
        num9.setOnClickListener(numberButtonListener);
        ((Button)findViewById(R.id.numberSelectorClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public static interface OnSudokuMoveSelectedListener {
        public void didSelectSudokuValue(TextView tv, String newValue);
    }
}
