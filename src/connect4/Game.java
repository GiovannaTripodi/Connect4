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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private int selectedMove;
    
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
     * Access to the grid representing the game.
     * 
     * @return the grid
     */
    public Grid getGrid() {
        return grid;
    }
    
    synchronized public void makeMove(int move) {
        synchronized (this) {
            selectedMove = move;
            notify();
        }
    }
    
    /**
     * Register a new observer.
     * 
     * @param obs the obeserver to be added
     */
    public void addObserver(GameObserver obs) {
        observers.add(obs);
    }
    
    synchronized private int askMove(Player p) {
        selectedMove = -1;
        p.yourTurn(this);
        while (selectedMove == -1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return selectedMove;
    }
    
    /**
     * Run the game untill its finished.
     */
    public void play() {
        grid.clear();
        
        for (GameObserver obs : observers)
            obs.newGame(grid);
        
        for (;;) {
            // 1.  Ask the current player to move.
            int move = askMove(grid.sideToMove() == Grid.RED ? redPlayer : yellowPlayer);
            
            // 2. Make the move.
            try {
                grid.makeMove(move);
            } catch (Grid.IllegalMove ex) {
                continue;
            }
            
            // 3. Notify the observers.
            for (GameObserver obs : observers)
                obs.newMove(move, grid);
        
            // 4. Verify if the game ended.
            if (grid.won()) {
                Player p = (grid.sideToMove() == Grid.RED ? yellowPlayer : redPlayer);
                for (GameObserver obs : observers)
                    obs.notifyWinner(p, grid);
                break;
            } else if (grid.draw()) {
                for (GameObserver obs : observers)
                    obs.notifyDraw(grid);
                break;
            }
        } 
    }
    
    /**
     * Exchange the order of the two players.
     */
    void swapPlayers() {
        Player temp = redPlayer;
        redPlayer = yellowPlayer;
        yellowPlayer = temp;
    }
}
