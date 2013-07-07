package com.gfranks.sudoku.widget_helpers;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Garrett on 7/4/13.
 */
public class SudokuGame {
    private ArrayList<GridValueCoord> currentGame;
    private ArrayList<ArrayList<GridValueCoord>> allGames;
    private static SudokuGame instance;

    public static SudokuGame getInstance() {
        if (instance == null) {
            instance = new SudokuGame();
        }
        return instance;
    }

    private SudokuGame() {
        allGames = new ArrayList<ArrayList<GridValueCoord>>();
        makeMultiGames();
    }

    public ArrayList<GridValueCoord> getNewGame() {
        currentGame = allGames.get(new Random().nextInt(allGames.size()));

        return currentGame;
    }

    public ArrayList<GridValueCoord> getCurrentGame() {
        return currentGame;
    }

    private void makeMultiGames() {
        ArrayList<GridValueCoord> game1 = new ArrayList<GridValueCoord>();
        game1.add(GridValueCoord.make(0,0,0,0,1));
        game1.add(GridValueCoord.make(0,0,1,1,2));
        game1.add(GridValueCoord.make(0,0,2,0,3));
        game1.add(GridValueCoord.make(0,1,1,1,6));
        game1.add(GridValueCoord.make(0,1,1,2,7));
        game1.add(GridValueCoord.make(0,1,2,1,4));
        game1.add(GridValueCoord.make(0,2,1,0,8));
        game1.add(GridValueCoord.make(0,2,1,1,9));
        game1.add(GridValueCoord.make(1,0,0,1,4));
        game1.add(GridValueCoord.make(1,0,2,1,6));
        game1.add(GridValueCoord.make(1,1,0,1,3));
        game1.add(GridValueCoord.make(1,1,1,1,2));
        game1.add(GridValueCoord.make(1,1,1,2,1));
        game1.add(GridValueCoord.make(1,2,1,0,6));
        game1.add(GridValueCoord.make(1,2,1,1,7));
        game1.add(GridValueCoord.make(1,2,2,1,8));
        game1.add(GridValueCoord.make(2,0,0,2,7));
        game1.add(GridValueCoord.make(2,0,1,1,8));
        game1.add(GridValueCoord.make(2,0,2,2,9));
        game1.add(GridValueCoord.make(2,1,1,1,9));
        game1.add(GridValueCoord.make(2,1,1,2,3));
        game1.add(GridValueCoord.make(2,2,0,1,4));
        game1.add(GridValueCoord.make(2,2,1,0,7));
        game1.add(GridValueCoord.make(2,2,1,1,2));
        allGames.add(game1);

        ArrayList<GridValueCoord> game2 = new ArrayList<GridValueCoord>();
        game2.add(GridValueCoord.make(0,0,2,2,8));
        game2.add(GridValueCoord.make(0,1,0,1,2));
        game2.add(GridValueCoord.make(0,1,0,2,8));
        game2.add(GridValueCoord.make(0,1,1,0,3));
        game2.add(GridValueCoord.make(0,1,2,2,1));
        game2.add(GridValueCoord.make(0,2,0,1,7));
        game2.add(GridValueCoord.make(0,2,1,2,8));
        game2.add(GridValueCoord.make(0,2,2,2,4));
        game2.add(GridValueCoord.make(1,0,0,1,4));
        game2.add(GridValueCoord.make(1,0,1,1,8));
        game2.add(GridValueCoord.make(1,0,2,0,5));
        game2.add(GridValueCoord.make(1,0,2,2,7));
        game2.add(GridValueCoord.make(1,1,1,0,7));
        game2.add(GridValueCoord.make(1,1,1,1,5));
        game2.add(GridValueCoord.make(1,1,1,2,6));
        game2.add(GridValueCoord.make(1,2,0,0,7));
        game2.add(GridValueCoord.make(1,2,0,2,6));
        game2.add(GridValueCoord.make(1,2,1,1,4));
        game2.add(GridValueCoord.make(1,2,2,1,1));
        game2.add(GridValueCoord.make(2,0,0,0,9));
        game2.add(GridValueCoord.make(2,0,1,0,8));
        game2.add(GridValueCoord.make(2,0,2,1,2));
        game2.add(GridValueCoord.make(2,1,0,0,8));
        game2.add(GridValueCoord.make(2,1,1,2,9));
        game2.add(GridValueCoord.make(2,1,2,0,5));
        game2.add(GridValueCoord.make(2,1,2,1,4));
        game2.add(GridValueCoord.make(2,2,0,0,6));

        allGames.add(game2);

    }

    public static class GridValueCoord {
        public int row, column;
        public int x, y;
        public int value;

        public GridValueCoord(int row, int column, int x, int y, int value) {
            this.row = row;
            this.column = column;
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public static GridValueCoord make(int row, int column, int x, int y, int value) {
            return new GridValueCoord(row, column, x, y, value);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getGridValueCoordAsId() {
            return (x*10) + y;
        }

        public String getGridValueCoordAsTag() {
            return String.valueOf((x*10) + y);
        }
    }
}
