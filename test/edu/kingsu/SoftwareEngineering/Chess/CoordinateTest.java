package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.beans.Transient;
import edu.kingsu.SoftwareEngineering.Chess.Main;

/**
 * JUnit test class for the {@link Coordinate} class.
 * Tests getter methods, equality, and string representation.
 * 
 * Author: Group3
 * Version: 1.0
 */
public class CoordinateTest {
    public Coordinate cord;

    @Before
    public void initialize() {
        cord = new Coordinate(3, 5);
    }

    @After 
    public void destroy() {
        cord = null;
    }

    @Test 
    public void testGetter() {
        assertEquals("Row should be 3 as our initialization",3, cord.getRow());
        assertEquals("Column should be 5 as our initialization",5, cord.getCol());
    }

    /**
     * Tests if coordinates are equal.( Checks same coordinates, different row, different column, and null comparison.)
     */
    @Test 
    public void testIfEqual () {
        Coordinate sameIs = new Coordinate(3,5);
        Coordinate diffRow = new Coordinate(4, 5);
        Coordinate diffCol = new Coordinate(3, 4);
        assertTrue("Coordinates with same row and column should be equal", cord.equals(sameIs));
        assertFalse("Coordinates with different rows should not be equal", cord.equals(diffRow));   
        assertFalse("Coordinates with different columns should not be equal", cord.equals(diffCol));
        assertFalse("Coordinate should not be equal to null, as we actually set it up", cord.equals(null));
    }

    /**
     * Tests the string representation of a Coordinate.     
     */
    @Test 
    public void testToString() {
        assertEquals("toString should return (row,column) format, so in our case if we test with 3,5 structure it should be true", "(3,5)", cord.toString());
    }
    
}
