package com.gfranks.sudoku.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Garrett on 7/4/13.
 */
public class SudokuGames {
    private SudokuGame currentGame;
    private ArrayList<SudokuGame> allGames;
    private static SudokuGames instance;

    public static SudokuGames getInstance() {
        if (instance == null) {
            instance = new SudokuGames();
        }
        return instance;
    }

    private SudokuGames() {
        allGames = new ArrayList<SudokuGame>();
        makeMultiGames();
    }

    public SudokuGame getNewGame() {
        currentGame = allGames.get(new Random().nextInt(allGames.size()));
        currentGame.setIsCurrentGame(true);

        for (SudokuGame game : allGames) {
            if (!currentGame.getID().equals(game.getID())) {
                game.setIsCurrentGame(false);
            }
        }

        return currentGame;
    }

    public void setCurrentGame(SudokuGame game) {
        boolean hasGameInstance = false;

        for (SudokuGame g : allGames) {
            if (g.getID().equals(game.getID())) {
                hasGameInstance = true;
            } else {
                g.setIsCurrentGame(false);
            }
        }

        if (!hasGameInstance) {
            allGames.add(game);
        }

        currentGame = game;
        currentGame.setIsCurrentGame(true);
    }

    public SudokuGame getCurrentGame() {
        return currentGame;
    }

    public SudokuGame restartCurrentGame() {
        ArrayList<SudokuGame.GridValueCoord> coords = currentGame.getGridValueCoords();
        for (SudokuGame.GridValueCoord coord : currentGame.getGridValueCoords()) {
            if (coord.hasListener) {
                coords.remove(coord);
            }
        }

        currentGame.setGridValueCoords(coords);
        return currentGame;
    }

    private void makeMultiGames() {
        ArrayList<SudokuGame.GridValueCoord> game1 = new ArrayList<SudokuGame.GridValueCoord>();
        game1.add(SudokuGame.GridValueCoord.make(0,0,0,0,1,false));
        game1.add(SudokuGame.GridValueCoord.make(0,0,1,1,2,false));
        game1.add(SudokuGame.GridValueCoord.make(0,0,2,0,3,false));
        game1.add(SudokuGame.GridValueCoord.make(0,1,1,1,6,false));
        game1.add(SudokuGame.GridValueCoord.make(0,1,1,2,7,false));
        game1.add(SudokuGame.GridValueCoord.make(0,1,2,1,4,false));
        game1.add(SudokuGame.GridValueCoord.make(0,2,1,0,8,false));
        game1.add(SudokuGame.GridValueCoord.make(0,2,1,1,9,false));
        game1.add(SudokuGame.GridValueCoord.make(1,0,0,1,4,false));
        game1.add(SudokuGame.GridValueCoord.make(1,0,2,1,6,false));
        game1.add(SudokuGame.GridValueCoord.make(1,1,0,1,3,false));
        game1.add(SudokuGame.GridValueCoord.make(1,1,1,1,2,false));
        game1.add(SudokuGame.GridValueCoord.make(1,1,1,2,1,false));
        game1.add(SudokuGame.GridValueCoord.make(1,2,1,0,6,false));
        game1.add(SudokuGame.GridValueCoord.make(1,2,1,1,7,false));
        game1.add(SudokuGame.GridValueCoord.make(1,2,2,1,8,false));
        game1.add(SudokuGame.GridValueCoord.make(2,0,0,2,7,false));
        game1.add(SudokuGame.GridValueCoord.make(2,0,1,1,8,false));
        game1.add(SudokuGame.GridValueCoord.make(2,0,2,2,9,false));
        game1.add(SudokuGame.GridValueCoord.make(2,1,1,1,9,false));
        game1.add(SudokuGame.GridValueCoord.make(2,1,1,2,3,false));
        game1.add(SudokuGame.GridValueCoord.make(2,2,0,1,4,false));
        game1.add(SudokuGame.GridValueCoord.make(2,2,1,0,7,false));
        game1.add(SudokuGame.GridValueCoord.make(2,2,1,1,2,false));
        allGames.add(new SudokuGame(game1));

        ArrayList<SudokuGame.GridValueCoord> game2 = new ArrayList<SudokuGame.GridValueCoord>();
        game2.add(SudokuGame.GridValueCoord.make(0,0,2,2,8,false));
        game2.add(SudokuGame.GridValueCoord.make(0,1,0,1,2,false));
        game2.add(SudokuGame.GridValueCoord.make(0,1,0,2,8,false));
        game2.add(SudokuGame.GridValueCoord.make(0,1,1,0,3,false));
        game2.add(SudokuGame.GridValueCoord.make(0,1,2,2,1,false));
        game2.add(SudokuGame.GridValueCoord.make(0,2,0,1,7,false));
        game2.add(SudokuGame.GridValueCoord.make(0,2,1,2,8,false));
        game2.add(SudokuGame.GridValueCoord.make(0,2,2,2,4,false));
        game2.add(SudokuGame.GridValueCoord.make(1,0,0,1,4,false));
        game2.add(SudokuGame.GridValueCoord.make(1,0,1,1,8,false));
        game2.add(SudokuGame.GridValueCoord.make(1,0,2,0,5,false));
        game2.add(SudokuGame.GridValueCoord.make(1,0,2,2,7,false));
        game2.add(SudokuGame.GridValueCoord.make(1,1,1,0,7,false));
        game2.add(SudokuGame.GridValueCoord.make(1,1,1,1,5,false));
        game2.add(SudokuGame.GridValueCoord.make(1,1,1,2,6,false));
        game2.add(SudokuGame.GridValueCoord.make(1,2,0,0,7,false));
        game2.add(SudokuGame.GridValueCoord.make(1,2,0,2,6,false));
        game2.add(SudokuGame.GridValueCoord.make(1,2,1,1,4,false));
        game2.add(SudokuGame.GridValueCoord.make(1,2,2,1,1,false));
        game2.add(SudokuGame.GridValueCoord.make(2,0,0,0,9,false));
        game2.add(SudokuGame.GridValueCoord.make(2,0,1,0,8,false));
        game2.add(SudokuGame.GridValueCoord.make(2,0,2,1,2,false));
        game2.add(SudokuGame.GridValueCoord.make(2,1,0,0,8,false));
        game2.add(SudokuGame.GridValueCoord.make(2,1,1,2,9,false));
        game2.add(SudokuGame.GridValueCoord.make(2,1,2,0,5,false));
        game2.add(SudokuGame.GridValueCoord.make(2,1,2,1,4,false));
        game2.add(SudokuGame.GridValueCoord.make(2,2,0,0,6,false));

        allGames.add(new SudokuGame(game2));
    }
}
