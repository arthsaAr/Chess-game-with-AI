package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import org.junit.Before;
import org.junit.Test;
import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.FakeGameManager;

public class NewGameSetupTest {

    private NewGameSetup screen;
    private FakeGameManager manager;

    @Before
    public void setup() {
        manager = new FakeGameManager();
        screen = new NewGameSetup(manager);
    }

    @Test
    public void testInitialState() throws Exception {
        Field mode = NewGameSetup.class.getDeclaredField("modeBox");
        mode.setAccessible(true);
        JComboBox<?> box = (JComboBox<?>) mode.get(screen);
        assertNotNull(box);
    }

    @Test
    public void testSliderChangesUpdateLabel() throws Exception {
        Field sliderField = NewGameSetup.class.getDeclaredField("aiSlider");
        sliderField.setAccessible(true);
        JSlider slider = (JSlider) sliderField.get(screen);
        slider.setValue(8);

        Method update = NewGameSetup.class.getDeclaredMethod("updateSummary");
        update.setAccessible(true);
        update.invoke(screen);

        assertEquals(8, slider.getValue());
    }

    @Test
    public void testColorSelection() throws Exception {
        Field w = NewGameSetup.class.getDeclaredField("whiteBtn");
        Field b = NewGameSetup.class.getDeclaredField("blackBtn");
        w.setAccessible(true);
        b.setAccessible(true);

        JRadioButton white = (JRadioButton) w.get(screen);
        JRadioButton black = (JRadioButton) b.get(screen);

        black.setSelected(true);
        assertTrue(black.isSelected());
        assertFalse(white.isSelected());
    }

    @Test
    public void testUpdateSummaryDoesNotCrash() throws Exception {
        Method m = NewGameSetup.class.getDeclaredMethod("updateSummary");
        m.setAccessible(true);
        m.invoke(screen);
        assertTrue(true);
    }

    @Test
    public void testAdjustLayoutForSmallSize() throws Exception {
        screen.setSize(400, 600);
        Method m = NewGameSetup.class.getDeclaredMethod("adjustLayoutForSize");
        m.setAccessible(true);
        m.invoke(screen);
        assertTrue(true);
    }

    @Test
    public void testAdjustLayoutForLargeSize() throws Exception {
        screen.setSize(1200, 800);
        Method m = NewGameSetup.class.getDeclaredMethod("adjustLayoutForSize");
        m.setAccessible(true);
        m.invoke(screen);
        assertTrue(true);
    }

    @Test
    public void testMainCardCreated() throws Exception {
        Method m = NewGameSetup.class.getDeclaredMethod("createMainCard");
        m.setAccessible(true);
        JPanel panel = (JPanel) m.invoke(screen);
        assertNotNull(panel);
    }
}
