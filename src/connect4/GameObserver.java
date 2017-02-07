/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package connect4;

/**
 * Interface representing entities interested in following the game.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public interface GameObserver {

    /**
     * Called at the beginning of a new game.
     * 
     * @param grid the grid
     */
    public void newGame(Grid grid);
    
    /**
     * Called after each move.
     * 
     * @param move the move maid
     * @param grid the grid
     */
    public void newMove(int move, Grid grid);    
    
    /**
     * Called when a player wins
     * 
     * @param player the winner
     * @param grid the grid
     */
    public void notifyWinner(Player player, Grid grid);
    
    /**
     * Called when the game finishes as a draw.
     * 
     * @param grid the grid
     */
    public void notifyDraw(Grid grid);
}
