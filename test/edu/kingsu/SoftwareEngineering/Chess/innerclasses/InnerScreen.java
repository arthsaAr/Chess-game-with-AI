package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.Coordinate;
import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.Piece;
import edu.kingsu.SoftwareEngineering.Chess.ui.BaseScreen;
/**
     * Test implementation of BaseScreen for testing purposes
     */
public class InnerScreen extends BaseScreen {
    public InnerScreen(){
        //nothing
        super(new GameManager());
    }

    @Override
    protected void initializeUI() {
        this.contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);
    }
}
