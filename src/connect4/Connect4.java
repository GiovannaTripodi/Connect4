/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package connect4;

import ui.Connect4Window;
import ui.UIPlayer;

/**
 * Setup and initiate the game.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class Connect4 {

    /**
     * The main function creates the players and continuosly run games.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Connect4Window win = new Connect4Window();
        Player p1 = new UIPlayer(win.getPanel());
        Player p2 = new AIPlayer();
        Game game = new Game(p1, p2);
        game.addObserver(win);
        win.setVisible(true);
        
        for (;;) {
            game.play();
            game.swapPlayers();
        }
    }    
}
