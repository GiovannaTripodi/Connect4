/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package connect4;

/**
 * A generic player of the game.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public interface Player {

    /**
     * Notify the player that its his turn to move.
     * 
     * @param game the game the player is playing
     */
    void yourTurn(Game game);
}
