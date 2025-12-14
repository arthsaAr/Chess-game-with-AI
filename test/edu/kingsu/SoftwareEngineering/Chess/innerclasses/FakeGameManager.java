package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import java.util.ArrayList;
import java.util.List;

import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.Coordinate;
import edu.kingsu.SoftwareEngineering.Chess.GameManager;
import edu.kingsu.SoftwareEngineering.Chess.Piece;

public class FakeGameManager extends GameManager {
    public boolean menuShown = false;
    public boolean loaded = false;     // to track if loadGame succeeded
    public boolean wasCalled = false;  // to track if loadGame method was called
    public boolean returnValue = true; // control what loadGame returns
    public boolean menuCalled= false;

    public void showScreen(String name) {
        if ("menu".equals(name)) {
            menuShown = true;
        }
    }

    public boolean getMenu(){
        return menuShown;
    }
    
    public boolean loadGameFromPGN(String pgn) {
        wasCalled = true;
        loaded = returnValue;
        return returnValue;
    }
}
