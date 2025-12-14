package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;

import edu.kingsu.SoftwareEngineering.Chess.innerclasses.pieceRuleEngine;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.ruleEngineBoard;

import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test class for the {@link RuleEngine} class.
 * Uses test subclasses of {@link Piece} and {@link Board} to verify
 * - King check detection
 * - Checkmate detection
 * - Stalemate detection
 * - Move legality
 * 
 * Author: Group3
 * Version: 1.0
 */
public class RuleEngineTest {
    private ruleEngineBoard board;
    private pieceRuleEngine piece;
    private RuleEngine engine;

    @Before 
    public void initialize() {
        board = new ruleEngineBoard(null);
        piece = new pieceRuleEngine("White", new Coordinate(0, 0));
        board.addPiece(piece);

        King whiteKing = new King("White", new Coordinate(7, 7));
        board.addPiece(whiteKing);
        
        King blackKing = new King("Black", new Coordinate(7, 0));
        board.addPiece(blackKing);

        engine = new RuleEngine(board);
    }

    @After 
    public void destroy() {
        board = null;
        piece = null;
        engine = null;
    }

    @Test
    public void testKingCheck() {
        assertFalse(engine.isKingInCheck("White"));
    }

    @Test
    public void testCheckmate() {
        Player whitePlayer = new HumanPlayer("White", "White");
        assertFalse(engine.isCheckmate(whitePlayer));
    }

    @Test
    public void testStalemate() {
        Player whitePlayer = new HumanPlayer("White", "White");
        assertFalse(engine.isStalemate(whitePlayer));
    }

    @Test
    public void testMoveLegal() {
        piece.setPosition(new Coordinate(1, 1));
        Move move = new Move(new Coordinate(1,1), new Coordinate(2,1), piece, null);
        assertTrue(engine.isMoveLegal(move));
    }
}
