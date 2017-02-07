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
        win.show();
        
        Player[] players = new Player[2];
        players[0] = new UIPlayer(win.getPanel());
        players[1] = new AIPlayer();        
                                
        int p = 0;
        for (;;) {
            Game game = new Game(players[p], players[1 - p]);
            game.addObserver(win);
            game.play();
            p = 1 - p;  // invert the order of the players            
        }
    }    
}
