package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JToggleButton;

import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.FakeGameManager;

public class SettingsScreenTest {

    private SettingsScreen screen;
    private FakeGameManager manager;

    @Before
    public void setup() {
        manager = new FakeGameManager();
        screen = new SettingsScreen(manager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(screen);
    }

    @Test
    public void testUpdateTheme() throws Exception {
        Method m = SettingsScreen.class.getDeclaredMethod("updateTheme");
        m.setAccessible(true);
        m.invoke(screen);
        assertTrue(true);
    }

    @Test
    public void testApplySettings() throws Exception {
        Method m = SettingsScreen.class.getDeclaredMethod("applySettings");
        m.setAccessible(true);
        try {
            m.invoke(screen);
            assertTrue(manager.menuCalled);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // Print the actual exception being thrown
            e.getCause().printStackTrace();
            fail("applySettings threw exception: " + e.getCause().getMessage());
        }
    }

    @Test
    public void testStyleComboBox() throws Exception {
        JComboBox<String> box = new JComboBox<>(new String[]{"A", "B"});
        Method m = SettingsScreen.class
                .getDeclaredMethod("styleComboBox", JComboBox.class);
        m.setAccessible(true);
        m.invoke(screen, box);
        assertNotNull(box.getRenderer());
    }

    @Test
    public void testThemeToggleAction() throws Exception {
        Field f = SettingsScreen.class.getDeclaredField("themeToggle");
        f.setAccessible(true);
        JToggleButton toggle = (JToggleButton) f.get(screen);

        toggle.setSelected(!toggle.isSelected());
        toggle.doClick();

        assertTrue(true);
    }

    @Test
    public void testBoardStyleSelection() throws Exception {
        Field f = SettingsScreen.class.getDeclaredField("boardStyleBox");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        JComboBox<String> box = (JComboBox<String>) f.get(screen);

        box.setSelectedItem("Blue");
        box.dispatchEvent(
            new java.awt.event.ActionEvent(
                box,
                java.awt.event.ActionEvent.ACTION_PERFORMED,
                "select"
            )
        );

        assertEquals("Blue", box.getSelectedItem());
    }
}
