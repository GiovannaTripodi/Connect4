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
import javax.swing.JFrame;
import javax.swing.JOptionPane;



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

    @Override
    public void notifyWinner(Player player, Grid grid) {
        String msg = (grid.sideToMove() == Grid.RED ? "Yellow" : "Red");
        msg += " player wins";
        String title = "Game ended";
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void notifyDraw(Grid grid) {
        String msg = "The game is draw";
        String title = "Game ended";
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
