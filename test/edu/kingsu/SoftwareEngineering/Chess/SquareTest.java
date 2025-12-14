package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.beans.Transient;
import edu.kingsu.SoftwareEngineering.Chess.Main;

/**
 * JUnit test class for the {@link Square} class.
 * Tests initialization, getter/setter for pieces, and piece removal behavior.
 * 
 * Author: Group3
 * Version: 1.0
 */
public class SquareTest {
  private Square square;
  private Piece piece;

  @Before
  public void createSquare() {
    square = new Square(4,5);
    piece = new Pawn("White", new Coordinate(4, 5));
  }

  @After
  public void tearDownChess() {
    square = null;
    piece = null;
  }

  @Test 
  public void testSquareInitialization() {
    assertNotNull("Square should be created properly", square);
    assertEquals("Row should be 4, as we set it", 4, square.getRow());
    assertEquals("Column should be 5", 5, square.getCol());
    //assertFalse("Column should not be other than 5", 6, square.getCol());
    assertNull("New square should not have any piece when we initialize", square.getPiece());
  }

  @Test 
  public void testSetter() {
    square.setPiece(piece);
    assertNotNull("Square should have a piece after setting a piece in that place/square", square.getPiece());
  }

  @Test 
  public void testGetter() {
    square.setPiece(piece);
    assertEquals("Piece on square should be to what we set", piece, square.getPiece());
  }

  @Test 
  public void testPieceRemoval() {
    square.setPiece(piece);
    square.setPiece(null);
    assertNull("Piece should be null after we remove piece in it", square.getPiece());
  }

  //tests for Board were successfull with total of 8 tests and no failure/errors

}
