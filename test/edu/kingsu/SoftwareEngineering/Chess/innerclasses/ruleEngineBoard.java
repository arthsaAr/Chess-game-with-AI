package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import java.util.ArrayList;
import java.util.List;

import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.Coordinate;
import edu.kingsu.SoftwareEngineering.Chess.GameScreen;
import edu.kingsu.SoftwareEngineering.Chess.Move;
import edu.kingsu.SoftwareEngineering.Chess.Piece;

public class ruleEngineBoard extends Board {
    public ruleEngineBoard(GameScreen gameScreen) {
        super(gameScreen);
    }

    private List<Piece> pieces = new ArrayList<>();

    @Override
    public List<Piece> getAllPieces(String color) { 
        return pieces; 
    }

    @Override
    public Coordinate findKing(String color) { 
        if(color.equals("White")) {
            return new Coordinate(7, 7);
        } else {
            return new Coordinate(7, 0);    //black king position
        }
    }

    public void addPiece(Piece p) { 
        pieces.add(p); 
    }

    @Override
    public void simulateMove(Move move) {
    }

    @Override
    public void undoMove(Move move) {
    }
}
