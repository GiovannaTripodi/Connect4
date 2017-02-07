/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package ui;

import connect4.Grid;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import javax.swing.JPanel;

/**
 * Panel that graphically represents the game grid.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class GridPanel extends JPanel {
    int discs[][];
    int lastMove;
    int toMove;
    int discWidth;
    
    /**
     * Initialize the panel.
     */
    GridPanel() {
        discs = new int[6][7];
        lastMove = -1;
        toMove = Grid.YELLOW;
        discWidth = 1;
    }
    
    /**
     * Change the current displayed grid.
     * 
     * The content of the grid is copied so that it won't change until this
     * method is called again.
     */
    void setGrid(Grid grid) {
        for (int row = 0; row < 6; row++)
            for (int col = 0; col < 7; col++)
                discs[row][col] = grid.getDisc(row, col);
        lastMove = grid.lastMove();
        toMove = grid.sideToMove();
    }
    
    @Override
    public void paint(Graphics g) {        
        // Draw the grid.
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Compute the size of the circles and of the space between them.
        int width = this.getWidth();
        int height = this.getHeight();
        discWidth = width / 7;
        if (discWidth > height / 6)
            discWidth = height / 6;
        int border = discWidth / 5;
        int radius = (discWidth - border) / 2;
        
        // Draw the blue grid.
        g2.setColor(Color.BLUE); 
        g2.fillRect(0, 0, width, height);
        
        // Draw the array of circles.
        for (int row = 0; row < 6; row++) {
            int y = height - ((border / 2) + discWidth * row) - 2*radius;
            for (int col = 0; col < 7; col++) {
                int x = (border / 2) + discWidth * col;
                int disc = discs[row][col];
                if (disc == Grid.RED)
                    g2.setColor(Color.RED);
                else if (disc == Grid.YELLOW)
                    g2.setColor(Color.YELLOW);
                else
                    g2.setColor(Color.WHITE);
                g2.fillOval(x, y, 2 * radius, 2 * radius);
            }
        }
        
        // Mark the last move (if any).                        
        if (lastMove >= 0) {
            int trih = 3*border / 4;
            int triw = (2000 * trih) / 1732;
            if (toMove == Grid.RED)
                g2.setColor(Color.YELLOW);
            else
                g2.setColor(Color.RED);
            GeneralPath tri = new GeneralPath();
            int x = (border / 2) + discWidth * lastMove + radius;
            tri.moveTo(x, border - 1);
            tri.lineTo(x + triw / 2, border - 1 - trih);
            tri.lineTo(x - triw / 2, border - 1 - trih);
            g2.fill(tri);
        }
    }        

    int getDiscWidth() {
        return discWidth;
    }
}
