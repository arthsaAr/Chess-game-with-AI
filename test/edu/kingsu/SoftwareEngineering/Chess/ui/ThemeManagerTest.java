package edu.kingsu.SoftwareEngineering.Chess.ui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

public class ThemeManagerTest {

    private ThemeManager theme;

    @Before
    public void setup() {
        theme = ThemeManager.getInstance();
        theme.setDarkMode(true); // reset baseline
        theme.setBoardStyle("Wooden");
    }

    // ========== Singleton ==========

    @Test
    public void testSingletonInstance() {
        ThemeManager t1 = ThemeManager.getInstance();
        ThemeManager t2 = ThemeManager.getInstance();
        assertSame(t1, t2);
    }

    // ========== Theme Switching ==========

    @Test
    public void testSetDarkModeTrue() {
        theme.setDarkMode(true);
        assertTrue(theme.isDarkMode());
    }

    @Test
    public void testSetDarkModeFalse() {
        theme.setDarkMode(false);
        assertFalse(theme.isDarkMode());
    }

    @Test
    public void testSetThemeDirectly() {
        ThemeManager.Theme light = new ThemeManager.LightTheme();
        theme.setTheme(light);
        assertFalse(theme.isDarkMode());
    }

    // ========== Listener System ==========

    @Test
    public void testListenerIsNotified() {
        AtomicBoolean called = new AtomicBoolean(false);

        ThemeManager.ThemeListener listener = () -> called.set(true);
        theme.addListener(listener);

        theme.setDarkMode(false);

        assertTrue(called.get());

        theme.removeListener(listener);
    }

    @Test
    public void testRemoveListenerStopsNotifications() {
        AtomicBoolean called = new AtomicBoolean(false);

        ThemeManager.ThemeListener listener = () -> called.set(true);
        theme.addListener(listener);
        theme.removeListener(listener);

        theme.setDarkMode(false);

        assertFalse(called.get());
    }

    // ========== Board Style ==========

    @Test
    public void testBoardStyleSetAndGet() {
        theme.setBoardStyle("Blue");
        assertEquals("Blue", theme.getBoardStyle());
    }

    @Test
    public void testBoardColorsWooden() {
        theme.setBoardStyle("Wooden");

        assertNotNull(theme.getLightSquare());
        assertNotNull(theme.getDarkSquare());
        assertNotNull(theme.getSelectedSquare());
        assertNotNull(theme.getHighlightSquare());
    }

    @Test
    public void testBoardColorsClassic() {
        theme.setBoardStyle("Classic");

        assertNotNull(theme.getLightSquare());
        assertNotNull(theme.getDarkSquare());
    }

    @Test
    public void testBoardColorsModern() {
        theme.setBoardStyle("Modern");

        assertNotNull(theme.getLightSquare());
        assertNotNull(theme.getDarkSquare());
    }

    @Test
    public void testBoardColorsGreen() {
        theme.setBoardStyle("Green");

        assertNotNull(theme.getLightSquare());
        assertNotNull(theme.getDarkSquare());
    }

    @Test
    public void testBoardColorsFallback() {
        theme.setBoardStyle("INVALID");

        assertNotNull(theme.getLightSquare());
        assertNotNull(theme.getDarkSquare());
    }

    // ========== Color Getters ==========

    @Test
    public void testAllColorGettersNotNull() {
        assertNotNull(theme.getPrimaryBackground());
        assertNotNull(theme.getSecondaryBackground());
        assertNotNull(theme.getCardBackground());
        assertNotNull(theme.getPrimaryText());
        assertNotNull(theme.getSecondaryText());
        assertNotNull(theme.getAccent());
        assertNotNull(theme.getBorder());
        assertNotNull(theme.getHover());
        assertNotNull(theme.getPrimaryButton());
        assertNotNull(theme.getPrimaryButtonText());
        assertNotNull(theme.getPrimaryButtonHover());
        assertNotNull(theme.getSecondaryButton());
        assertNotNull(theme.getSecondaryButtonText());
        assertNotNull(theme.getSecondaryButtonHover());
        assertNotNull(theme.getDisabledText());
        assertNotNull(theme.getSuccess());
        assertNotNull(theme.getError());
        assertNotNull(theme.getWarning());
    }

    // ========== Font Getters ==========

    @Test
    public void testFontGettersNotNull() {
        Font f1 = theme.getTitleFont();
        Font f2 = theme.getHeadingFont();
        Font f3 = theme.getBodyFont();
        Font f4 = theme.getSmallFont();

        assertNotNull(f1);
        assertNotNull(f2);
        assertNotNull(f3);
        assertNotNull(f4);
    }

    // ========== Spacing Getters ==========

    @Test
    public void testSpacingValuesArePositive() {
        assertTrue(theme.getSpacingXS() > 0);
        assertTrue(theme.getSpacingS() > 0);
        assertTrue(theme.getSpacingM() > 0);
        assertTrue(theme.getSpacingL() > 0);
        assertTrue(theme.getSpacingXL() > 0);
        assertTrue(theme.getBorderRadius() > 0);
        assertTrue(theme.getButtonRadius() > 0);
    }

    // ========== Theme Class Instantiation ==========

    @Test
    public void testDarkThemeInstantiation() {
        ThemeManager.DarkTheme dark = new ThemeManager.DarkTheme();

        assertNotNull(dark.primaryBackground);
        assertNotNull(dark.primaryText);
        assertNotNull(dark.titleFont);
    }

    @Test
    public void testLightThemeInstantiation() {
        ThemeManager.LightTheme light = new ThemeManager.LightTheme();

        assertNotNull(light.primaryBackground);
        assertNotNull(light.primaryText);
        assertNotNull(light.titleFont);
    }

    // ========== Defensive Coverage ==========

    @Test
    public void testMultipleListenersAreNotified() {
        AtomicBoolean l1 = new AtomicBoolean(false);
        AtomicBoolean l2 = new AtomicBoolean(false);

        ThemeManager.ThemeListener A = () -> l1.set(true);
        ThemeManager.ThemeListener B = () -> l2.set(true);

        theme.addListener(A);
        theme.addListener(B);

        theme.setDarkMode(false);

        assertTrue(l1.get());
        assertTrue(l2.get());

        theme.removeListener(A);
        theme.removeListener(B);
    }
}
