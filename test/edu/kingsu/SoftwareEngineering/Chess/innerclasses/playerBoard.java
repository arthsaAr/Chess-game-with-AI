package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.Move;
import edu.kingsu.SoftwareEngineering.Chess.Player;

public  class playerBoard extends Player {
    public playerBoard(String name, String color) {
        super(name, color);
    }

    @Override
    public Move makeMove(Board board) {
        return null; // Just return null for testing
    }
}
