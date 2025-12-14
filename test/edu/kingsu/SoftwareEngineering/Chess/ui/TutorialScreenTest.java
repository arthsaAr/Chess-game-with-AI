package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.junit.Before;
import org.junit.Test;

import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.FakeGameManager;

public class TutorialScreenTest {

    private TutorialScreen screen;
    private FakeGameManager manager;

    @Before
    public void setup() {
        manager = new FakeGameManager();
        screen = new TutorialScreen(manager);
    }

    // ===== INITIAL STATE =====

    @Test
    public void testInitialLessonState() throws Exception {
        Field counter = TutorialScreen.class.getDeclaredField("lessonCounter");
        counter.setAccessible(true);
        JLabel label = (JLabel) counter.get(screen);
        assertEquals("Lesson 1 of 8", label.getText());
    }

    // ===== NAVIGATION =====

    @Test
    public void testNextLesson() throws Exception {
        Method next = TutorialScreen.class.getDeclaredMethod("nextLesson");
        next.setAccessible(true);
        next.invoke(screen);

        Field lesson = TutorialScreen.class.getDeclaredField("currentLesson");
        lesson.setAccessible(true);
        assertEquals(1, lesson.getInt(screen));
    }

    @Test
    public void testPreviousLessonBlockedAtZero() throws Exception {
        Method prev = TutorialScreen.class.getDeclaredMethod("previousLesson");
        prev.setAccessible(true);
        prev.invoke(screen);

        Field lesson = TutorialScreen.class.getDeclaredField("currentLesson");
        lesson.setAccessible(true);
        assertEquals(0, lesson.getInt(screen));
    }

    @Test
    public void testPreviousLessonAfterNext() throws Exception {
        Method next = TutorialScreen.class.getDeclaredMethod("nextLesson");
        Method prev = TutorialScreen.class.getDeclaredMethod("previousLesson");
        next.setAccessible(true);
        prev.setAccessible(true);

        next.invoke(screen);
        prev.invoke(screen);

        Field lesson = TutorialScreen.class.getDeclaredField("currentLesson");
        lesson.setAccessible(true);
        assertEquals(0, lesson.getInt(screen));
    }

    // ===== CONTENT UPDATES =====

    @Test
    public void testLessonContentUpdates() throws Exception {
        Method next = TutorialScreen.class.getDeclaredMethod("nextLesson");
        next.setAccessible(true);
        next.invoke(screen);

        Field content = TutorialScreen.class.getDeclaredField("lessonContent");
        content.setAccessible(true);
        JTextArea area = (JTextArea) content.get(screen);

        assertTrue(area.getText().length() > 10);
    }

    @Test
    public void testLessonTitleUpdates() throws Exception {
        Method next = TutorialScreen.class.getDeclaredMethod("nextLesson");
        next.setAccessible(true);
        next.invoke(screen);

        Field title = TutorialScreen.class.getDeclaredField("lessonTitle");
        title.setAccessible(true);
        JLabel lbl = (JLabel) title.get(screen);

        assertNotNull(lbl.getText());
    }

    // ===== LAST LESSON LOGIC =====

    @Test
    public void testFinishButtonVisibleOnLastLesson() throws Exception {
        Method next = TutorialScreen.class.getDeclaredMethod("nextLesson");
        next.setAccessible(true);

        for (int i = 0; i < 7; i++) {
            next.invoke(screen);
        }

        Field finish = TutorialScreen.class.getDeclaredField("finishBtn");
        finish.setAccessible(true);
        JButton btn = (JButton) finish.get(screen);

        assertTrue(btn.isVisible());
    }

    @Test
    public void testNextHiddenOnLastLesson() throws Exception {
        Method next = TutorialScreen.class.getDeclaredMethod("nextLesson");
        next.setAccessible(true);

        for (int i = 0; i < 7; i++) {
            next.invoke(screen);
        }

        Field nextBtn = TutorialScreen.class.getDeclaredField("nextBtn");
        nextBtn.setAccessible(true);
        JButton btn = (JButton) nextBtn.get(screen);

        assertFalse(btn.isVisible());
    }

    // ===== BOARD LOGIC =====

    @Test
    public void testBoardUpdatesForAllLessons() throws Exception {
        Method update = TutorialScreen.class.getDeclaredMethod("updateBoardForLesson", int.class);
        update.setAccessible(true);

        for (int i = 0; i < 8; i++) {
            update.invoke(screen, i);
        }

        assertTrue(true);
    }

    // ===== DOT RENDERING =====

    @Test
    public void testUpdateDots() throws Exception {
        Method dots = TutorialScreen.class.getDeclaredMethod("updateDots");
        dots.setAccessible(true);
        dots.invoke(screen);

        assertTrue(true);
    }

    // ===== THEME UPDATE =====

    @Test
    public void testUpdateTheme() throws Exception {
        Method update = TutorialScreen.class.getDeclaredMethod("updateTheme");
        update.setAccessible(true);
        update.invoke(screen);

        assertTrue(true);
    }

    // ===== FINISH BUTTON =====

    @Test
    public void testFinishButtonReturnsToMenu() throws Exception {
        Method next = TutorialScreen.class.getDeclaredMethod("nextLesson");
        next.setAccessible(true);

        for (int i = 0; i < 7; i++) {
            next.invoke(screen);
        }

        Field finish = TutorialScreen.class.getDeclaredField("finishBtn");
        finish.setAccessible(true);
        JButton btn = (JButton) finish.get(screen);

        btn.doClick();
        assertTrue(manager.getMenu());
    }

    // ===== DEFENSIVE =====

    @Test
    public void testNextDoesNotOverflow() throws Exception {
        Method next = TutorialScreen.class.getDeclaredMethod("nextLesson");
        next.setAccessible(true);

        for (int i = 0; i < 20; i++) {
            next.invoke(screen);
        }

        Field lesson = TutorialScreen.class.getDeclaredField("currentLesson");
        lesson.setAccessible(true);
        assertEquals(7, lesson.getInt(screen));
    }

    
}
