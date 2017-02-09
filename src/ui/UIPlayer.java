/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package ui;

import connect4.Game;
import connect4.Player;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Allow to play the game through the GUI.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class UIPlayer implements Player, MouseListener {

    GridPanel gridPanel;
    Game waitingGame;   // the game that is waiting the move, or null
    
    /**
     * Initialize the player
     * 
     * @param panel the panel used to graphically select the moves
     */
    public UIPlayer(GridPanel panel) {
        this.gridPanel = panel;
        panel.addMouseListener(this);
        waitingGame = null;
    }
    
    @Override
    public void yourTurn(Game game) {
        synchronized (this) {
            waitingGame = game;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        synchronized(this) {
            if (waitingGame != null) {
                int x = me.getX();
                int col = x / gridPanel.getDiscWidth();
                if (col >= 0 && col < 7) {
                    waitingGame.makeMove(col);
                    waitingGame = null;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }    
}
