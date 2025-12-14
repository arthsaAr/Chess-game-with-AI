package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.FakeGameManager;

public class GameScreenTest {

    @Test
    public void testConstructorCreatesPanel() {
        GameManager manager = new FakeGameManager();

        GameScreen screen = new GameScreen(manager);

        assertNotNull(screen);
        assertTrue(screen.getLayout() instanceof BorderLayout);
    }

    @Test
    public void testBoardComponentAdded() {
        GameManager manager = new FakeGameManager();

        GameScreen screen = new GameScreen(manager);

        assertEquals(1, screen.getComponentCount());
        assertTrue(screen.getComponent(0) instanceof JPanel);
    }
}
