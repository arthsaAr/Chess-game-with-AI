package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.FakeGameManager;

public class HelpScreenTest {

    private HelpScreen screen;
    private FakeGameManager manager;

    @Before
    public void setup() {
        manager = new FakeGameManager();
        screen = new HelpScreen(manager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(screen);
    }

    @Test
    public void testSwitchTabs() throws Exception {
        Method switchTab = HelpScreen.class
                .getDeclaredMethod("switchTab", String.class);
        switchTab.setAccessible(true);

        switchTab.invoke(screen, "Controls");
        switchTab.invoke(screen, "Notation");
        switchTab.invoke(screen, "About");

        assertTrue(true);
    }

    @Test
    public void testUpdateTheme() throws Exception {
        Method m = HelpScreen.class.getDeclaredMethod("updateTheme");
        m.setAccessible(true);
        m.invoke(screen);
        assertTrue(true);
    }

    @Test
    public void testContentCardCreation() throws Exception {
        Method m = HelpScreen.class.getDeclaredMethod("createContentCard");
        m.setAccessible(true);

        JPanel panel = (JPanel) m.invoke(screen);
        assertNotNull(panel);
    }

    @Test
    public void testRulesContent() throws Exception {
        Method m = HelpScreen.class
                .getDeclaredMethod("addRulesContent", JPanel.class);
        m.setAccessible(true);

        JPanel panel = new JPanel();
        m.invoke(screen, panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    public void testControlsContent() throws Exception {
        Method m = HelpScreen.class
                .getDeclaredMethod("addControlsContent", JPanel.class);
        m.setAccessible(true);

        JPanel panel = new JPanel();
        m.invoke(screen, panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    public void testNotationContent() throws Exception {
        Method m = HelpScreen.class
                .getDeclaredMethod("addNotationContent", JPanel.class);
        m.setAccessible(true);

        JPanel panel = new JPanel();
        m.invoke(screen, panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    public void testAboutContent() throws Exception {
        Method m = HelpScreen.class
                .getDeclaredMethod("addAboutContent", JPanel.class);
        m.setAccessible(true);

        JPanel panel = new JPanel();
        m.invoke(screen, panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    public void testTabButtonsExist() throws Exception {
        Field rules = HelpScreen.class.getDeclaredField("rulesBtn");
        Field controls = HelpScreen.class.getDeclaredField("controlsBtn");
        Field notation = HelpScreen.class.getDeclaredField("notationBtn");
        Field about = HelpScreen.class.getDeclaredField("aboutBtn");

        rules.setAccessible(true);
        controls.setAccessible(true);
        notation.setAccessible(true);
        about.setAccessible(true);

        assertNotNull((JButton) rules.get(screen));
        assertNotNull((JButton) controls.get(screen));
        assertNotNull((JButton) notation.get(screen));
        assertNotNull((JButton) about.get(screen));
    }
}
