package edu.kingsu.SoftwareEngineering.Chess.innerclasses;

import java.util.ArrayList;
import java.util.List;

import edu.kingsu.SoftwareEngineering.Chess.Bishop;
import edu.kingsu.SoftwareEngineering.Chess.Board;
import edu.kingsu.SoftwareEngineering.Chess.Coordinate;
import edu.kingsu.SoftwareEngineering.Chess.GameScreen;
import edu.kingsu.SoftwareEngineering.Chess.King;
import edu.kingsu.SoftwareEngineering.Chess.Knight;
import edu.kingsu.SoftwareEngineering.Chess.Move;
import edu.kingsu.SoftwareEngineering.Chess.Piece;
import edu.kingsu.SoftwareEngineering.Chess.Rook;
import edu.kingsu.SoftwareEngineering.Chess.ui.MainMenu.Callbacks;

public class InnerCallbacks implements Callbacks {
        public boolean newGame = false;
        public boolean load = false;
        public boolean tutorial = false;
        public boolean settings = false;
        public boolean help = false;

        @Override
        public void onNewGame() { newGame = true; }

        @Override
        public void onLoadGame() { load = true; }

        @Override
        public void onTutorial() { tutorial = true; }

        @Override
        public void onSettings() { settings = true; }

        @Override
        public void onHelp() { help = true; }
    }
