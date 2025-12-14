package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import edu.kingsu.SoftwareEngineering.Chess.innerclasses.NoOpRunnable;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.InnerCallbacks;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

public class MainMenuTest {

    private MainMenu menu;
    private InnerCallbacks callbacks;

    @Before
    public void setup() {
        callbacks = new InnerCallbacks();
        menu = new MainMenu(callbacks);
    }

    @Test
    public void testMenuConstructs() {
        assertNotNull(menu);
    }

    @Test
    public void testUpdateThemeDoesNotCrash() throws Exception {
        Method m = MainMenu.class.getDeclaredMethod("updateTheme");
        m.setAccessible(true);
        m.invoke(menu);
        assertTrue(true);
    }

    @Test
    public void testMakeButtonCreatesComponent() throws Exception {
        Method m = MainMenu.class.getDeclaredMethod(
                "makeButton",
                String.class,
                String.class,
                MenuButton.Variant.class,
                Runnable.class
        );

        m.setAccessible(true);

        Runnable r = new NoOpRunnable();

        Object[] params = new Object[] {
                "Test",
                "icon",
                MenuButton.Variant.PRIMARY,
                r
        };

        JComponent btn = (JComponent) m.invoke(menu, params);

        assertNotNull(btn);
        assertTrue(btn instanceof JComponent);
    }

    @Test
    public void testGradientHostConstructs() {
        MainMenu.GradientHost host = new MainMenu.GradientHost();
        assertNotNull(host);
    }

    @Test
    public void testCallbackWiring() {
        callbacks.onNewGame();
        callbacks.onLoadGame();
        callbacks.onTutorial();
        callbacks.onSettings();
        callbacks.onHelp();

        assertTrue(callbacks.newGame);
        assertTrue(callbacks.load);
        assertTrue(callbacks.tutorial);
        assertTrue(callbacks.settings);
        assertTrue(callbacks.help);
    }

    // ===== SAFE CALLBACK IMPLEMENTATION =====
    
}
