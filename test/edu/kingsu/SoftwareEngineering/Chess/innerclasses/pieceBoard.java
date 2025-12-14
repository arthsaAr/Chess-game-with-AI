package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import java.util.ArrayList;
import java.util.List;

import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.Coordinate;
import edu.kingsu.SoftwareEngineering.Chess.Piece;

//Since Piece is abstract we need to create a subclass for testing, to test specifically with subclasses like rook, pawn etc
public class pieceBoard extends Piece {
    public pieceBoard(String color, Coordinate position) {
        super(color, position);
    }

    @Override
    public List<Coordinate> getLegalMoves(Board board) {
        List<Coordinate> moves = new ArrayList<>();
        int rowIs = position.getRow();
        int colIs = position.getCol();
        if(rowIs+1 < Board.ROWS) {
            moves.add(new Coordinate(rowIs+1, colIs));
        }

        return moves;   //created a random valid coordinate and returned as a legal move for testing
    }

    @Override
    public String getSymbol() {
        return "T"; //a test piece similar to our subclasses
    }

    @Override
    public Piece copy() {
        return new pieceBoard(this.color, new Coordinate(this.position.getRow(), this.position.getCol()));
    }

    @Override
    public boolean isAttacking(Board board, Coordinate target) {
        return getLegalMoves(board).contains(target);
    }
}
