/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package connect4;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a single game of connect4.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class Game {

    private Grid grid;
    private Player redPlayer;
    private Player yellowPlayer;
    private List<GameObserver> observers;
    
    /**
     * Create a new game.
     * 
     * @param red the first player
     * @param yellow the second player
     */
    public Game(Player red, Player yellow) {
        grid = new Grid();
        redPlayer = red;
        yellowPlayer = yellow;
        observers = new ArrayList<>();
    }
    
    /**
     * Register a new observer.
     * 
     * @param obs the obeserver to be added
     */
    public void addObserver(GameObserver obs) {
        observers.add(obs);
    }
    
    /**
     * Run the game untill its finished.
     */
    public void play() {
        grid.clear();
        Player[] players = new Player[2];
        players[0] = redPlayer;
        players[1] = yellowPlayer;
        int p = 0;        
        
        for (GameObserver obs : observers)
            obs.newGame(grid);
        
        for (;;) {
            int move = players[p].selectMove(grid);
            try {
                grid.makeMove(move);
            } catch (Grid.IllegalMove ex) {
                continue;  // ask again to the same player
            }
            
            // Notify the move the observers.
            for (GameObserver obs : observers)
                obs.newMove(move, grid);
            
            // Check if the game is finished.
            if (grid.won()) {
                for (GameObserver obs : observers)
                    obs.notifyWinner(players[p], grid);
                break;
            } else if (grid.draw()) {
                for (GameObserver obs : observers)
                    obs.notifyDraw(grid);
                break;                
            }
            p = 1 - p;  // change the player
        }
    }
}
