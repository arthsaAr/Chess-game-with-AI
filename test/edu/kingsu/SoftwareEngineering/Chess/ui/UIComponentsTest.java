package edu.kingsu.SoftwareEngineering.Chess.ui;

import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class UIComponentsTest {

    @Test
    public void testCreatePrimaryButton_textOnly() {
        JButton btn = UIComponents.createPrimaryButton("Primary");
        assertNotNull(btn);
        assertEquals("Primary", btn.getText());
        assertTrue(btn.isEnabled());
    }

    @Test
    public void testCreatePrimaryButton_withIcon() {
        JButton btn = UIComponents.createPrimaryButton("Primary", new ImageIcon());
        assertNotNull(btn);
        assertEquals("Primary", btn.getText());
    }

    @Test
    public void testCreateSecondaryButton_textOnly() {
        JButton btn = UIComponents.createSecondaryButton("Secondary");
        assertNotNull(btn);
        assertEquals("Secondary", btn.getText());
    }

    @Test
    public void testCreateSecondaryButton_withIcon() {
        JButton btn = UIComponents.createSecondaryButton("Secondary", new ImageIcon());
        assertNotNull(btn);
        assertEquals("Secondary", btn.getText());
    }

    @Test
    public void testCreateBackButton_withAction() {
        final boolean[] clicked = { false };

        JButton btn = UIComponents.createBackButton("Back", () -> clicked[0] = true);

        assertNotNull(btn);
        btn.doClick();
        assertTrue(clicked[0]);
    }

    @Test
    public void testCreateBackButton_noAction() {
        JButton btn = UIComponents.createBackButton("Back", null);
        assertNotNull(btn);
        btn.doClick(); // should not throw
    }

    @Test
    public void testCreateCard() {
        JPanel card = UIComponents.createCard();
        assertNotNull(card);
        assertFalse(card.isOpaque());
    }

    @Test
    public void testCreateTitle() {
        JLabel lbl = UIComponents.createTitle("Title");
        assertEquals("Title", lbl.getText());
    }

    @Test
    public void testCreateHeading() {
        JLabel lbl = UIComponents.createHeading("Heading");
        assertEquals("Heading", lbl.getText());
    }

    @Test
    public void testCreateBodyText() {
        JLabel lbl = UIComponents.createBodyText("Body");
        assertEquals("Body", lbl.getText());
    }

    @Test
    public void testCreateSmallText() {
        JLabel lbl = UIComponents.createSmallText("Small");
        assertEquals("Small", lbl.getText());
    }

    @Test
    public void testCreateBasePanel() {
        JPanel panel = UIComponents.createBasePanel();
        assertNotNull(panel);
        assertEquals(BorderLayout.class, panel.getLayout().getClass());
    }

    @Test
    public void testCreateTopBar() {
        JPanel bar = UIComponents.createTopBar("Back", () -> { });

        assertNotNull(bar);
        assertEquals(1, bar.getComponentCount());
    }

    @Test
    public void testCreateComboBox() {
        String[] items = {"One", "Two", "Three"};
        JComboBox<String> box = UIComponents.createComboBox(items);
        assertEquals(3, box.getItemCount());
        assertEquals("One", box.getItemAt(0));
    }

    @Test
    public void testCreateTextArea_placeholder() {
        JTextArea text = UIComponents.createTextArea("Hello");
        assertEquals("Hello", text.getText());
    }

    @Test
    public void testCreateScrollPane() {
        JLabel lbl = new JLabel("Scroll");
        JScrollPane pane = UIComponents.createScrollPane(lbl);
        assertNotNull(pane);
        assertEquals(lbl, pane.getViewport().getView());
    }

    @Test
    public void testPaintMethodsCoverage() {
        JButton btn = UIComponents.createPrimaryButton("Paint");
        btn.setSize(100, 40);

        JPanel card = UIComponents.createCard();
        card.setSize(200, 200);

        BufferedImage img = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        btn.paint(g);
        card.paint(g);
    }
}
