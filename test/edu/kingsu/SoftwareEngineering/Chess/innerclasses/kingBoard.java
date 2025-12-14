package edu.kingsu.SoftwareEngineering.Chess;

import edu.kingsu.SoftwareEngineering.Chess.Piece;

public class kingBoard extends Board {
    private Piece[][] squares = new Piece[8][8];

    public kingBoard() {
        super(null);
    }

    @Override
    public Piece getPieceAt(int row, int col) {
        return squares[row][col];
    }

    @Override
    public void setPieceAt(int row, int col, Piece piece) {
        squares[row][col] = piece;
    }
}
