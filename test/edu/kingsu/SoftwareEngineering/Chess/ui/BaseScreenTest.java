package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JPanel;

import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.FakeGameManager;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.InnerScreen;

public class BaseScreenTest {

    private InnerScreen screen;
    private FakeGameManager manager;

    @Before
    public void setUp() {
        System.setProperty("java.awt.headless", "true");
        manager = new FakeGameManager();
        screen = new InnerScreen();
    }

    @Test
    public void testConstructorInitializesLayoutAndTheme() {
        assertNotNull(screen);
        assertTrue(screen.getLayout() instanceof BorderLayout);
        assertNotNull(screen.theme);
        assertNotNull(screen.manager);
    }

    @Test
    public void testCreateContentWrapper() {
        JPanel wrapper = screen.createContentWrapper();

        assertNotNull(wrapper);
        assertFalse(wrapper.isOpaque());
        assertTrue(wrapper.getLayout() instanceof BorderLayout);
    }

    @Test
    public void testCreateVerticalPanel() {
        JPanel panel = screen.createVerticalPanel();

        assertNotNull(panel);
        assertFalse(panel.isOpaque());
        assertNotNull(panel.getLayout());
    }

    @Test
    public void testCreateScrollableContent() {
        JPanel content = new JPanel();

        JScrollPane pane = screen.createScrollableContent(content);

        assertNotNull(pane);
        assertEquals(content, pane.getViewport().getView());
    }

    @Test
    public void testCreateVerticalStrut() {
        Component c = screen.createVerticalStrut(20);

        assertNotNull(c);
    }

    @Test
    public void testCreateHorizontalStrut() {
        Component c = screen.createHorizontalStrut(20);

        assertNotNull(c);
    }

    @Test
    public void testCreateBottomButtons() {
        JPanel panel = screen.createBottomButtons("Cancel", "Save", () -> {});

        assertNotNull(panel);
        assertEquals(2, panel.getComponentCount());
    }

    @Test
    public void testOnThemeChangedExecutes() {
        screen.onThemeChanged();

        assertNotNull(screen.getBackground());
    }

    // ===== HELPER CLASSES (must be public static for JUnit) =====

    
}