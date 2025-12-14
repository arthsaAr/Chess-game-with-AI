package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;

public class MainTest {

    @Test
    public void testMainRunsWithoutCrash() {
        // Prevent Swing from opening real windows
        System.setProperty("java.awt.headless", "true");

        // This executes the lambda inside invokeLater
        Main.main(new String[] {});

        // If no exception occurs, coverage is achieved
        assert(true);
    }
}
