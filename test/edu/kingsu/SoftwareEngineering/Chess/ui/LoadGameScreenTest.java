package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.FakeGameManager;

public class LoadGameScreenTest {

    private LoadGameScreen screen;
    private FakeGameManager manager;

    @Before
    public void setup() {
        manager = new FakeGameManager();
        screen = new LoadGameScreen(manager);
    }

    @Test
    public void testParsePGNHeaders() throws Exception {
        String pgn =
            "[Event \"Test Match\"]\n" +
            "[Site \"Edmonton\"]\n" +
            "[Date \"2025.01.01\"]\n\n1. e4 e5";

        Method m = LoadGameScreen.class
                .getDeclaredMethod("parsePGNHeaders", String.class);
        m.setAccessible(true);

        Map<String, String> headers =
            (Map<String, String>) m.invoke(screen, pgn);

        assertEquals("Test Match", headers.get("Event"));
        assertEquals("Edmonton", headers.get("Site"));
        assertEquals("2025.01.01", headers.get("Date"));
    }

    @Test
    public void testExtractMoves() throws Exception {
        String pgn =
            "1. e4 e5\n2. Nf3 Nc6\n3. Bb5 a6\n1-0";

        Method m = LoadGameScreen.class
                .getDeclaredMethod("extractMoves", String.class);
        m.setAccessible(true);

        List<String> moves =
            (List<String>) m.invoke(screen, pgn);

        assertEquals(6, moves.size());
        assertEquals("e4", moves.get(0));
        assertEquals("e5", moves.get(1));
        assertEquals("Nf3", moves.get(2));
    }

    @Test
    public void testPreviewGameWithEmptyPGN() throws Exception {
        Field field = LoadGameScreen.class.getDeclaredField("pgnTextArea");
        field.setAccessible(true);
        JTextArea area = (JTextArea) field.get(screen);
        area.setText("");

        Method preview = LoadGameScreen.class.getDeclaredMethod("previewGame");
        try {
            preview.invoke(screen);
            assertTrue(true); // Test passes if no exception
        } catch (java.lang.reflect.InvocationTargetException e) {
            fail("previewGame threw exception: " + e.getCause().getMessage());
        }
        assertTrue(true);
    }

    @Test
    public void testPreviewGameWithValidPGN() throws Exception {
        String pgn =
            "[Event \"Game\"]\n" +
            "[White \"Alice\"]\n" +
            "[Black \"Bob\"]\n\n" +
            "1. e4 e5 2. Nf3 Nc6";

        Field field = LoadGameScreen.class.getDeclaredField("pgnTextArea");
        field.setAccessible(true);
        JTextArea area = (JTextArea) field.get(screen);
        area.setText(pgn);

        Method preview = LoadGameScreen.class.getDeclaredMethod("previewGame");
        preview.setAccessible(true);
        preview.invoke(screen);

        Field showing = LoadGameScreen.class.getDeclaredField("showingPreview");
        showing.setAccessible(true);
        assertTrue(showing.getBoolean(screen));
    }

    @Test
    public void testLoadGameSuccess() throws Exception {
        Field field = LoadGameScreen.class.getDeclaredField("pgnTextArea");
        field.setAccessible(true);
        JTextArea area = (JTextArea) field.get(screen);
        area.setText("1. e4 e5");  // must be non-empty

        Field retVal = FakeGameManager.class.getDeclaredField("returnValue");
        retVal.setAccessible(true);
        retVal.setBoolean(manager, true);

        Method load = LoadGameScreen.class.getDeclaredMethod("loadGame");
        load.setAccessible(true);
        
         try {
            load.invoke(screen);
            assertTrue(manager.wasCalled);
            assertTrue(manager.loaded);
        } catch (java.lang.reflect.InvocationTargetException e) {
            //e.getCause().printStackTrace();
            fail("loadGame threw exception: " + e.getCause().getMessage());
        }
        assertTrue(true);
    }

    @Test
    public void testLoadGameFailure() throws Exception {
        Field field = LoadGameScreen.class.getDeclaredField("pgnTextArea");
        field.setAccessible(true);
        JTextArea area = (JTextArea) field.get(screen);
        area.setText("1. e4 e5");

        manager.returnValue = false;  // simulate failure

        Method load = LoadGameScreen.class.getDeclaredMethod("loadGame");
        load.setAccessible(true);
        
         try {
            load.invoke(screen);
            assertTrue(manager.wasCalled);
            assertFalse(manager.loaded);
        } catch (java.lang.reflect.InvocationTargetException e) {
            fail("loadGame threw exception: " + e.getCause().getMessage());
        }
        assertTrue(true);
    }

    @Test
    public void testAdjustLayoutForSize() throws Exception {
        screen.setSize(600, 400);

        Method m =
            LoadGameScreen.class
                .getDeclaredMethod("adjustLayoutForSize");
        m.setAccessible(true);
        m.invoke(screen);

        screen.setSize(1200, 800);
        m.invoke(screen);

        assertTrue(true);
    }

    @Test
    public void testAddInfoLine() throws Exception {
        JPanel panel = new JPanel();

        Method method =
            LoadGameScreen.class.getDeclaredMethod(
                "addInfoLine", JPanel.class, String.class);
        method.setAccessible(true);

        method.invoke(screen, panel, "Event: Test");

        assertEquals(1, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JLabel);
    }

    @Test
    public void testUpdateTheme() throws Exception {
        Method m =
            LoadGameScreen.class
                .getDeclaredMethod("updateTheme");
        m.setAccessible(true);
        m.invoke(screen);

        assertTrue(true);
    }
}
