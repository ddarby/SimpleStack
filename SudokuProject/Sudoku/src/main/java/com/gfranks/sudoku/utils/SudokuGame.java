package com.gfranks.sudoku.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by gfranks on 7/27/13.
 */
public class SudokuGame {

    private ArrayList<GridValueCoord> coords;
    private boolean isCurrentGame;
    private String id;

    private static final String KEY_ID = "id";
    private static final String KEY_ISCURRENTGAME = "isCurrentGame";
    private static final String KEY_COORDS = "coords";
    private static final String KEY_ROW = "row";
    private static final String KEY_COLUMN = "column";
    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_VALUE = "value";
    private static final String KEY_HASLISTENER = "hasListener";

    public SudokuGame(ArrayList<GridValueCoord> coords) {
        this.coords = coords;
        this.id = new BigInteger(130, new SecureRandom()).toString(32);
    }

    public SudokuGame(String json) {
        createGameFromJSON(json);
    }

    public ArrayList<GridValueCoord> getGridValueCoords() {
        return coords;
    }

    public void setGridValueCoords(ArrayList<GridValueCoord> coords) {
        this.coords = coords;
    }

    public boolean isCurrentGame() {
        return isCurrentGame;
    }

    public void setIsCurrentGame(boolean isCurrentGame) {
        this.isCurrentGame = isCurrentGame;
    }

    public String getID() {
        return id;
    }

    public void addOrUpdateGridValueCoord(GridValueCoord coord) {
        boolean valueExists = false;
        for (GridValueCoord c : coords) {
            if (coord.row == c.row && coord.column == c.column && coord.x == c.x && coord.y == c.y) {
                valueExists = true;
                c.value = coord.value;
            }
        }

        if (!valueExists) {
            coords.add(coord);
        }
    }

    public void removeGridValueCoord(int row, int column, int x, int y) {
        for (GridValueCoord c : coords) {
            if (row == c.row && column == c.column && x == c.x && y == c.y) {
                coords.remove(c);
                return;
            }
        }
    }

    private void createGameFromJSON(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            this.id = obj.getString(KEY_ID);
            this.isCurrentGame = obj.getBoolean(KEY_ISCURRENTGAME);
            ArrayList<GridValueCoord> coords = new ArrayList<GridValueCoord>();
            JSONArray array = obj.getJSONArray(KEY_COORDS);
            for (int i=0; i<array.length(); i++) {
                JSONObject coord = array.getJSONObject(i);
                coords.add(GridValueCoord.make(coord.getInt(KEY_ROW),
                                               coord.getInt(KEY_COLUMN),
                                               coord.getInt(KEY_X),
                                               coord.getInt(KEY_Y),
                                               coord.getInt(KEY_VALUE),
                                               coord.getBoolean(KEY_HASLISTENER)));
            }
            this.coords = coords;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getGameAsJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(KEY_ID, id);
            obj.put(KEY_ISCURRENTGAME, isCurrentGame());

            JSONArray array = new JSONArray();
            for (int i=0; i<coords.size(); i++) {
                GridValueCoord coord = coords.get(i);
                JSONObject gameObj = new JSONObject();
                gameObj.put(KEY_ROW, coord.row);
                gameObj.put(KEY_COLUMN, coord.column);
                gameObj.put(KEY_X, coord.x);
                gameObj.put(KEY_Y, coord.y);
                gameObj.put(KEY_VALUE, coord.value);
                gameObj.put(KEY_HASLISTENER, coord.hasListener);

                array.put(gameObj);
            }
            obj.put(KEY_COORDS, array);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return obj.toString();
    }

    public static class GridValueCoord {
        public int row, column;
        public int x, y;
        public int value;
        public boolean hasListener;

        public GridValueCoord(int row, int column, int x, int y, int value, boolean hasListener) {
            this.row = row;
            this.column = column;
            this.x = x;
            this.y = y;
            this.value = value;
            this.hasListener = hasListener;
        }

        public static GridValueCoord make(int row, int column, int x, int y, int value, boolean hasListener) {
            return new GridValueCoord(row, column, x, y, value, hasListener);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getGridValueCoordAsTag() {
            return String.valueOf((row*10) + column) + ":" + String.valueOf((x*10) + y);
        }
    }
}
