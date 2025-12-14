package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.Piece;

public class helperBoard extends Board {
    public helperBoard() {
        super(null);
    }

    @Override
    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol, String choosePromotionPiece, boolean isRealMove) {
        // Copy logic if needed, but skip gameScreen interaction
        // You can call super.movePiece partially or just skip the captured piece part
        Piece target = getPieceAt(toRow, toCol);

        // Move the piece normally
        Piece moving = getPieceAt(fromRow, fromCol);
        setPieceAt(toRow, toCol, moving);
        setPieceAt(fromRow, fromCol, null);
        return isRealMove;
        // Skip gameScreen.addCapturedPiece entirely
    }
}

