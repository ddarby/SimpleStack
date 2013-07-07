package com.gfranks.sudoku.widget_helpers;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Garrett on 6/30/13.
 */
public class SudokuGrid {

    private GridLayout grid;
    private Activity mActivity;
    private int base_width = 25;
    private ArrayList<Integer[][]> randomizedNumbers;

    public SudokuGrid(GridLayout grid, Activity activity) {
        this.grid = grid;
        this.mActivity = activity;
        generateNumberArray();
    }

    public void loadNewSudkokuGrid() {
        for (int i=0; i<81; i++) {
            int row = i/9;
            int array_column = i%9;
            int array_row = 0;
            if (array_column <= 2) {
                if (row <= 2) {
                    array_row = 0;
                } else if (row > 2 && row < 6) {
                    array_row = 3;
                } else {
                    array_row = 6;
                }
            } else if (array_column > 2 && array_column < 6) {
                array_column -= 3;
                if (row <= 2) {
                    array_row = 1;
                } else if (row > 2 && row < 6) {
                    array_row = 4;
                } else {
                    array_row = 7;
                }
            } else {
                array_column -= 6;
                if (row <= 2) {
                    array_row = 2;
                } else if (row > 2 && row < 6) {
                    array_row = 5;
                } else {
                    array_row = 8;
                }
            }

            Log.e("CURRENTLY ADDING", "GROUP: " + array_row + ", ROW: " + row%3 + ", COLUMN: " + array_column%3);

            TextView tv = new TextView(mActivity.getBaseContext());
            tv.setText(String.valueOf(randomizedNumbers.get(array_row)[row%3][array_column%3]));
            tv.setWidth(getSubviewWidth());
            tv.setHeight(getSubviewWidth());
            tv.setGravity(Gravity.CENTER);
            grid.addView(tv);

            if (i%9 != 8) {
                if (i%3 == 2) {
                    grid.addView(getNewVerticalBar(true));
                } else {
                    grid.addView(getNewVerticalBar(false));
                }
            }

            if (i > 0 && i < 78) {
                if (i==26 || i==53) {
                    for (int j=0; j<17; j++) {
                        if (j%2 == 1) {
                            grid.addView(getNewSkinnyHorizontalBar(true));
                        } else {
                            grid.addView(getNewHorizontalBar(true));
                        }
                    }
                } else {
                    if (i%9 == 8) {
                        for (int j=0; j<17; j++) {
                            if (j%2 == 1) {
                                grid.addView(getNewSkinnyHorizontalBar(false));
                            } else {
                                grid.addView(getNewHorizontalBar(false));
                            }
                        }
                    }
                }
            }
        }
    }

    public View getNewHorizontalBar(boolean isThick) {
        TextView horizontal = new TextView(mActivity.getBaseContext());
        horizontal.setWidth(getSubviewWidth());
        horizontal.setHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, isThick ? 5 : 1,
                mActivity.getResources().getDisplayMetrics()));
        horizontal.setBackgroundColor(mActivity.getResources().getColor(android.R.color.black));
        return horizontal;
    }

    public View getNewSkinnyHorizontalBar(boolean isThick) {
        TextView horizontal = new TextView(mActivity.getBaseContext());
        horizontal.setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, isThick ? 5 : 1,
                mActivity.getResources().getDisplayMetrics()));
        horizontal.setHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, isThick ? 5 : 1,
                mActivity.getResources().getDisplayMetrics()));
        horizontal.setBackgroundColor(mActivity.getResources().getColor(android.R.color.white));
        return horizontal;
    }

    public View getNewVerticalBar(boolean isThick) {
        TextView vertical = new TextView(mActivity.getBaseContext());
        vertical.setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, isThick ? 5 : 1,
                mActivity.getResources().getDisplayMetrics()));
        vertical.setHeight(getSubviewWidth());
        vertical.setBackgroundColor(mActivity.getResources().getColor(android.R.color.black));
        return vertical;
    }

    public void reloadSudokuGrid() {
        grid.removeAllViews();

    }

    public int getSubviewWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

//        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width,
//                mActivity.getResources().getDisplayMetrics())-42)/15;
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, base_width,
                mActivity.getResources().getDisplayMetrics()));
    }

    public void orientationChanged() {
//        for (int i=0; i<grid.getChildCount(); i++) {
//            View v = grid.getChildAt(i);
//            if (v instanceof TextView) {
//                int width_height = getScreenWidth()/14;
//                ((TextView)v).setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width_height,
//                        mActivity.getResources().getDisplayMetrics()));
//                ((TextView)v).setHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width_height,
//                        mActivity.getResources().getDisplayMetrics()));
//            }
//        }
    }

    public void generateNumberArray() {
        randomizedNumbers = new ArrayList<Integer[][]>();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (int i=0;i<9;i++) {
            temp.add(i+1);
        }

        for (int i=0; i<9; i++) {
            Integer[][] nums = new Integer[3][3];
            int count = 0;
            for (int j=0;j<3;j++) {
                for (int k=0;k<3;k++) {
                    Collections.shuffle(temp);
                    nums[j][k] = temp.get(count);
                    ++count;
                }
            }
            randomizedNumbers.add(nums);
        }
    }
}
