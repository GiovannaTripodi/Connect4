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
     * Ask the player to choose the move to make.
     * 
     * @param g the current state of the game
     * @return the move to make as an integer indicating the column (0-6)
     */
    int selectMove(Grid g);
}
