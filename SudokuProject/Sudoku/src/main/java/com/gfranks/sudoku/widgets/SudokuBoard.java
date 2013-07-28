package com.gfranks.sudoku.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gfranks.sudoku.R;
import com.gfranks.sudoku.dialogs.SudokuMoveDialog;
import com.gfranks.sudoku.utils.SudokuGame;
import com.gfranks.sudoku.utils.SudokuGames;

import java.util.ArrayList;

/**
 * Created by Garrett on 7/3/13.
 */
public class SudokuBoard extends LinearLayout implements View.OnClickListener, SudokuMoveDialog.OnSudokuMoveSelectedListener {

    private GridLayout[][] grids;
    private String[][] gridTags = {{"0","1","2"}, {"10","11","12"}, {"20","21","22"}};
    private SudokuMoveDialog numberDialog;
    public boolean is_timer_shown;
    private int base_width = 27;
    private ArrayList<SudokuMove> moves;

    public SudokuBoard(Context context) {
        super(context);
    }

    public SudokuBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SudokuBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setupGrids() {
        moves = new ArrayList<SudokuMove>();
        grids = new GridLayout[3][3];
        setOrientation(LinearLayout.VERTICAL);

        GridLayout layout;
        for (int i=0; i<3; i++) {
            LinearLayout container = new LinearLayout(getContext());
            container.setOrientation(LinearLayout.HORIZONTAL);
            for (int j=0; j<3; j++) {
                layout = new GridLayout(getContext());
                layout.setColumnCount(3);
                layout.setRowCount(3);
                layout.setOrientation(GridLayout.HORIZONTAL);
                layout.setId(i*10+j);
                layout.setTag(gridTags[i][j]);
                layout.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));

                LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(3,3,3,3);
                layout.setLayoutParams(lp);

                setupSubviews(layout);
                grids[i][j] = layout;
                container.addView(layout);
            }
            addView(container);
        }
    }

    private void setupSubviews(GridLayout grid) {
        for (int i=0; i<9; i++) {

            TextView tv = new TextView(getContext());
            tv.setWidth(getSubviewWidth());
            tv.setHeight(getSubviewWidth());
            tv.setOnClickListener(this);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getContext().getResources().getColor(R.color.dark_gray));
            if (i < 3) {
                tv.setTag(String.valueOf(grid.getTag()) + ":" + String.valueOf(i%3));
            } else {
                tv.setTag(String.valueOf(grid.getTag()) + ":" + String.valueOf(((i/3)*10)+i%3));
            }
            tv.setBackgroundResource(R.drawable.tv_normal_bg);
            grid.addView(tv);
        }
    }

    private int getSubviewWidth() {
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, base_width,
                getContext().getResources().getDisplayMetrics()));
    }

    public GridLayout getGrid(SudokuGame.GridValueCoord gridCoord) {
        return grids[gridCoord.row][gridCoord.column];
    }

    public void addValueToGrid(SudokuGame.GridValueCoord valueCoord) {
        GridLayout grid = getGrid(valueCoord);

        for (int i=0; i<grid.getChildCount(); i++) {
            View v = grid.getChildAt(i);
            if (v instanceof TextView) {
                if (v.getTag() != null && v.getTag().equals(valueCoord.getGridValueCoordAsTag())) {
                    TextView tv = (TextView)v;
                    ((TextView)v).setText(String.valueOf(valueCoord.value));

                    if (valueCoord.hasListener) {
                        tv.setOnClickListener(this);
                        tv.setTextColor(getContext().getResources().getColor(R.color.green));
                    } else {
                        tv.setOnClickListener(null);
                        tv.setTextColor(getContext().getResources().getColor(R.color.dark_gray));
                    }
                }
            }
        }
    }

    public void setCurrentGame(SudokuGame game) {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                for (int k=0; k<grids[i][j].getChildCount(); k++) {
                    View v = grids[i][j].getChildAt(k);
                    if (v instanceof TextView) {
                        ((TextView)v).setText("");
                        v.setOnClickListener(this);
                    }
                }
            }
        }

        for (SudokuGame.GridValueCoord coord : game.getGridValueCoords()) {
            addValueToGrid(coord);
        }
    }

    public void reloadCurrentGame() {
        setCurrentGame(SudokuGames.getInstance().restartCurrentGame());
    }

    public void undoLastMove() {
        if (moves.size() <=0) {
            return;
        }

        SudokuMove move = moves.get(moves.size()-1);
        move.tv.setText(move.valueBefore);

        String[] coords = move.tv.getTag().toString().split(":");
        int row = 0, column = 0, x = 0, y = 0;
        if (coords[0].length() > 1) {
            row = Integer.parseInt(""+coords[0].charAt(0));
            column = Integer.parseInt(""+coords[0].charAt(1));
        } else {
            column = Integer.parseInt(coords[0]);
        }
        if (coords[1].length() > 1) {
            y = Integer.parseInt(""+coords[1].charAt(0));
            x = Integer.parseInt(""+coords[1].charAt(1));
        } else {
            y = Integer.parseInt(coords[1]);
        }
        if (move.valueBefore.length() == 0) {
            SudokuGames.getInstance().getCurrentGame().removeGridValueCoord(row, column, x, y);
        } else {
            SudokuGames.getInstance().getCurrentGame().addOrUpdateGridValueCoord(SudokuGame.GridValueCoord.make(row, column, x, y, Integer.parseInt(move.valueBefore), true));
        }

        moves.remove(moves.size()-1);
    }

    public boolean isAWin() {
        int row = 0;
        while (row < 3) {
            if (checkHorizontalValues(row)) {
                ++row;
            } else {
                return false;
            }
        }

        int column = 0;
        while (column < 3) {
            if (checkVerticalValues(column)) {
                ++column;
            } else {
                return false;
            }
        }

        int grid = 0;
        while (grid < 9) {
            if (checkGridValues(SudokuGame.GridValueCoord.make(grid / 3, grid % 3, 0, 0, 0,false))) {
                ++grid;
            } else {
                return false;
            }
        }

        return true;
    }

    public boolean checkHorizontalValues(int row) {
        ArrayList<GridLayout> grids = new ArrayList<GridLayout>();
        grids.add(getGrid(SudokuGame.GridValueCoord.make(row, 0, 0, 0, 0,false)));
        grids.add(getGrid(SudokuGame.GridValueCoord.make(row, 1, 0, 0, 0,false)));
        grids.add(getGrid(SudokuGame.GridValueCoord.make(row, 2, 0, 0, 0,false)));

        ArrayList<Integer> row1Values = new ArrayList<Integer>();
        ArrayList<Integer> row2Values = new ArrayList<Integer>();
        ArrayList<Integer> row3Values = new ArrayList<Integer>();
        for (int grid = 0; grid<grids.size(); grid++) {
            for (int i=0; i<grids.get(grid).getChildCount(); i++) {
                View v = grids.get(grid).getChildAt(i);
                if (v instanceof TextView) {
                    TextView tv = (TextView)v;
                    if (tv.getText().toString().length() == 0) {
                        return false;
                    }

                    int coord = 0;
                    int value = 0;
                    try {
                        coord = Integer.parseInt(tv.getTag().toString().split(":")[1]);
                        value = Integer.parseInt(tv.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return false;
                    }
                    if (coord >= 10) {
                        if (coord >= 10 && coord < 20) {
                            if (!row2Values.contains(value)) {
                                row2Values.add(value);
                            } else {
                                return false;
                            }
                        } else {
                            if (!row3Values.contains(value)) {
                                row3Values.add(value);
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if (!row1Values.contains(value)) {
                            row1Values.add(value);
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean checkVerticalValues(int column) {
        ArrayList<GridLayout> grids = new ArrayList<GridLayout>();
        grids.add(getGrid(SudokuGame.GridValueCoord.make(0, column, 0, 0, 0,false)));
        grids.add(getGrid(SudokuGame.GridValueCoord.make(1, column, 0, 0, 0,false)));
        grids.add(getGrid(SudokuGame.GridValueCoord.make(2, column, 0, 0, 0,false)));

        ArrayList<Integer> column1Values = new ArrayList<Integer>();
        ArrayList<Integer> column2Values = new ArrayList<Integer>();
        ArrayList<Integer> column3Values = new ArrayList<Integer>();
        for (int grid = 0; grid<grids.size(); grid++) {
            for (int i=0; i<grids.get(grid).getChildCount(); i++) {
                View v = grids.get(grid).getChildAt(i);
                if (v instanceof TextView) {
                    TextView tv = (TextView)v;
                    if (tv.getText().toString().length() == 0) {
                        return false;
                    }

                    int coord = 0;
                    int value = 0;
                    try {
                        coord = Integer.parseInt(tv.getTag().toString().split(":")[1]);
                        value = Integer.parseInt(tv.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return false;
                    }
                    if (coord%10 == 0) {
                        if (!column1Values.contains(value)) {
                            column1Values.add(value);
                        } else {
                            return false;
                        }
                    } else if (coord%10 == 1) {
                        if (!column2Values.contains(value)) {
                            column2Values.add(value);
                        } else {
                            return false;
                        }
                    } else if (coord%10 == 2) {
                        if (!column3Values.contains(value)) {
                            column3Values.add(value);
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean checkGridValues(SudokuGame.GridValueCoord valueCoord) {
        GridLayout grid = getGrid(valueCoord);

        ArrayList<Integer> values = new ArrayList<Integer>();
        for (int i=0; i<grid.getChildCount(); i++) {
            View v = grid.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView)v;
                if (tv.getText().toString().length() == 0) {
                    return false;
                }

                int coord = 0;
                int value = 0;
                try {
                    coord = Integer.parseInt(tv.getTag().toString().split(":")[1]);
                    value = Integer.parseInt(tv.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
                if (!values.contains(value)) {
                    values.add(value);
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof TextView) {
            if (numberDialog != null) {
                numberDialog.dismiss();
                numberDialog.setTextView((TextView)view);
            } else {
                numberDialog = new SudokuMoveDialog(getContext(),
                        SudokuMoveDialog.SudokuMoveDialogType.SudokuMoveDialogTypeSelector, (TextView)view, moves, this);
            }
            numberDialog.show();
        }
    }

    @Override
    public void didSelectSudokuValue(TextView tv, String newValue) {
        String[] coords = tv.getTag().toString().split(":");
        int row = 0, column = 0, x = 0, y = 0;
        if (coords[0].length() > 1) {
            row = Integer.parseInt(""+coords[0].charAt(0));
            column = Integer.parseInt(""+coords[0].charAt(1));
        } else {
            column = Integer.parseInt(coords[0]);
        }
        if (coords[1].length() > 1) {
            y = Integer.parseInt(""+coords[1].charAt(0));
            x = Integer.parseInt(""+coords[1].charAt(1));
        } else {
            y = Integer.parseInt(coords[1]);
        }
        SudokuGames.getInstance().getCurrentGame().addOrUpdateGridValueCoord(SudokuGame.GridValueCoord.make(row, column, x, y, Integer.parseInt(newValue), true));
    }

    public static class SudokuMove {
        private TextView tv;
        private String valueBefore;
        private String valueAfter;

        public SudokuMove(TextView tv, String valueBefore, String valueAfter) {
            this.tv = tv;
            this.valueBefore = valueBefore;
            this.valueAfter = valueAfter;
        }
    }
}
