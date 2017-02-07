/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package ui;

import connect4.Grid;
import connect4.Player;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Allow to play the game through the GUI.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class UIPlayer implements Player, MouseListener {

    GridPanel gridPanel;
    int selectedMove;
    boolean active;
    
    /**
     * Initialize the player
     * 
     * @param panel the panel used to graphically select the moves
     */
    public UIPlayer(GridPanel panel) {
        this.gridPanel = panel;
        panel.addMouseListener(this);
        active = false;
        selectedMove = -1;
    }
    
    @Override
    public int selectMove(Grid g) {
        /* Note that this method and the `mouseClicked' method will be called
           by different threads.  Therefore we need a form of synchronization. 
           This method wait until the move is selected by clicking the mouse on
           the panel. */
        int move;
        synchronized(this) {
            active = true;
            while (selectedMove < 0) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(UIPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            active = false;
            move = selectedMove;
            selectedMove = -1;
        }
        return move;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        synchronized(this) {
            if (active) {
                int x = me.getX();
                int col = x / gridPanel.getDiscWidth();
                if (col >= 0 && col < 7) {                    
                    selectedMove = col;
                    notify();
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
