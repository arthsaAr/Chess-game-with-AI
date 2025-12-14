package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;

import edu.kingsu.SoftwareEngineering.Chess.innerclasses.playerBoard;

import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test class for the {@link Player} class.
 * Uses a simple {@link TestPlayer} subclass to test basic functionality:
 * - Getting player name
 * - Getting player color
 * 
 * Author: Group3
 * Version: 1.0
 */
public class PlayerTest {

    private Player whitePlayer;
    private Player blackPlayer;

    @Before
    public void setup() {
        whitePlayer = new playerBoard("Stephen", "White");
        blackPlayer = new playerBoard("Chess320", "Black");
    }

    @After 
    public void destroy() {
        whitePlayer = null;
        blackPlayer = null;
    }

    @Test
    public void testNameGetter() {
        assertEquals("Stephen", whitePlayer.getName());
        assertEquals("Chess320", blackPlayer.getName());
    }

    @Test
    public void testGetColor() {
        assertEquals("White", whitePlayer.getColor());
        assertEquals("Black", blackPlayer.getColor());
    }
}
