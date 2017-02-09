/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package ui;

import connect4.GameObserver;
import connect4.Grid;
import connect4.Player;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;



/**
 * A window that shows the game.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class Connect4Window extends JFrame implements GameObserver {

    GridPanel gridPanel;
    private boolean clicked;   // used to stop to wait the user closing the dialog box
    
    /**
     * Create the window.
     */
    public Connect4Window() {
        gridPanel = new GridPanel();
        
        setTitle("Connect4");
        setSize(650, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        add("Center", gridPanel);        
    }
    
    @Override
    public void newGame(Grid grid) {
        gridPanel.setGrid(grid);
        gridPanel.repaint();
    }

    @Override
    public void newMove(int move, Grid grid) {
        gridPanel.setGrid(grid);
        gridPanel.repaint();
    }    

    /**
     * Return the inner panel.
     * 
     * @return the panel
     */
    public GridPanel getPanel() {
        return gridPanel;
    }

    synchronized private void waitClick() {
        while (!clicked) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Connect4Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    synchronized private void click() {
        clicked = true;
        notify();
    }
    
    
    
    @Override
    public void notifyWinner(Player player, Grid grid) {
        final String col = (grid.sideToMove() == Grid.RED ? "Yellow" : "Red");
        clicked = false;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, col + " player wins!",
                        "Game ended", JOptionPane.INFORMATION_MESSAGE);
                click();
            }
        });
        waitClick();
    }

    @Override
    public void notifyDraw(Grid grid) {
        clicked = false;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "The game is draw!",
                        "Game ended", JOptionPane.INFORMATION_MESSAGE);
                click();
            }
        });
        waitClick();
    }
}
