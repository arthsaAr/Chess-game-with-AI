package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

public class ChessGameTest {
    private ChessGame chessgame;
    private Player whitePlayer;
    private Player blackPlayer;

    @Before 
    public void setUp(){
        whitePlayer = new HumanPlayer("White", "White");
        blackPlayer = new HumanPlayer("Black", "Black");

        List<Player> players = new ArrayList<>();
        players.add(whitePlayer);
        players.add(blackPlayer);
        chessgame = new ChessGame(players);

    }

    @After 
    public void destroy(){
        chessgame = null;
        whitePlayer = null;
        blackPlayer = null;
    }

    @Test 
    public void testInitialization(){
        assertNotNull("Chessgame should be initialized", chessgame);
        assertNotNull("Board should be created as well", chessgame.getBoard());
        assertEquals("Current player should be white at first", whitePlayer, chessgame.getCurrentPlayer());
    }

    @Test 
    public void testChangeTurn(){
        assertEquals("White moves first", whitePlayer, chessgame.getCurrentPlayer());
        chessgame.changeTurn();
        assertEquals("Black should move next after white", blackPlayer, chessgame.getCurrentPlayer());
        chessgame.changeTurn();
        assertEquals("Back to white turn after change", whitePlayer, chessgame.getCurrentPlayer());
    }

    @Test 
    public void testSetBoard(){
        Board board = new Board(null);
        chessgame.setBoard(board);

        assertEquals("Board should be replaced when a different one is set", board, chessgame.getBoard());
    }

    @Test 
    public void testAddMove(){
        Board board = chessgame.getBoard();
        Piece pawn = board.getPieceAt(6, 0);
        Move move = new Move(new Coordinate(6, 0), new Coordinate(5, 0), pawn, null);

        chessgame.addMove(move);

        assertEquals("Move should be stored ", 1, chessgame.getMoveHistory().size());
        assertEquals(move, chessgame.getMoveHistory().get(0));
    }

    @Test
    public void testUndo() {
        Board board = chessgame.getBoard();

        Piece pawn = board.getPieceAt(6, 0);
        Move move = new Move(new Coordinate(6, 0), new Coordinate(5, 0), pawn, null);

        chessgame.makeMove(move);
        assertEquals(1, chessgame.getMoveHistory().size());

        chessgame.undoMove();
        assertEquals("Move should be removed from history when undoing it", 0, chessgame.getMoveHistory().size());
    }
}
