package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import java.util.ArrayList;
import java.util.List;

import edu.kingsu.SoftwareEngineering.Chess.Bishop;
import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.Coordinate;
import edu.kingsu.SoftwareEngineering.Chess.GameScreen;
import edu.kingsu.SoftwareEngineering.Chess.King;
import edu.kingsu.SoftwareEngineering.Chess.Knight;
import edu.kingsu.SoftwareEngineering.Chess.Move;
import edu.kingsu.SoftwareEngineering.Chess.Piece;
import edu.kingsu.SoftwareEngineering.Chess.Rook;


public class InnerBoardFactory {
        public static Board board;

    public InnerBoardFactory(){
        board = new Board(null);
    }
    public static Board kingInCheckScenario() {
        board = new Board(null);
        board.clear();
        // White king
        board.setPieceAt(7, 4, new King("White", new Coordinate(7, 4)));
        // Black rook giving check
        board.setPieceAt(7, 0, new Rook("Black", new Coordinate(7, 0)));
        // White bishop can block/capture
        board.setPieceAt(5, 2, new Bishop("White", new Coordinate(5, 2)));
         return board;
    }

    public static Board simpleBoardWithCapture() {
        return new Board(null);
    }

    public static Board blockableCheckScenario() {
        board = new Board(null);
        board.clear();
        // White king at (7, 4)
        board.setPieceAt(7, 4, new King("White", new Coordinate(7, 4)));
        // Black bishop giving diagonal check
        board.setPieceAt(5, 2, new Bishop("Black", new Coordinate(5, 2)));
        // Black knight giving check
        board.setPieceAt(6, 6, new Knight("Black", new Coordinate(6, 6)));
        // No white pieces blocking
         return board;
    }

    public static Board doubleCheckScenario() {
        board = new Board(null);
        board.clear();
        // White king
        board.setPieceAt(0, 4, new King("White", new Coordinate(0, 4)));
        // Two attackers giving double-check
        board.setPieceAt(2, 4, new Rook("Black", new Coordinate(2, 4)));
        board.setPieceAt(1, 6, new Bishop("Black", new Coordinate(1, 6)));
        // Remove ALL white pieces except king
        // so AI cannot block or capture
         return board;
    }

    public static Board materialComparisonBoard() {
        return new Board(null);
    }

    public static Board tradeLossScenario() {
        return new Board(null);
    }

    public static Board deepTacticScenario() {
        return new Board(null);
    }
}