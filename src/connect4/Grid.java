/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package connect4;

/**
 * The state of the game represented by a grid of 7x6 discs.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class Grid {
    // Integers representing the colors of the two players (or nothing).
    public static int EMPTY = 0;
    public static int RED = 1;
    public static int YELLOW = 2;

    /* This 42 15-bits numbers are used to build hash values representing the
       current game state.  The hash code is the XOR of the codes of the
       positions of RED discs, xored with those of the YELLOW DISCS shifted by
       15 bits.
    */
    private static final int[][] ZOBRIST = {
        {5040, 28166, 18578, 11899, 27416, 18494, 24376},
        {18037, 5549, 2387, 20239, 852, 13729, 23079},
        {18089, 1583, 14531, 22767, 10245, 13529, 11182},
        {15034, 5081, 24876, 344, 18641, 3194, 31501},
        {20303, 27892, 613, 29776, 2375, 25162, 25168},
        {17774, 22153, 22475, 13783, 17739, 29237, 29900}
    };
            
    /**
     * Exception used to signal illegal moves.
     */
    static public class IllegalMove extends RuntimeException { };

    int discs[][];  // the 7x6 grid of discs
    int top[];      // the first free row for the 7 columns
    int moves[];    // history of the moves made so far
    int nMoves;     // number of moves made
    int hash;       // hash code
    
    /**
     * Create an empty grid.
     */
    public Grid() {
        discs = new int[6][7];
        top = new int[7];
        moves = new int[42];
        clear();
    }
    
    /** 
     * Reset the grid.
     */
    public void clear() {
        for (int j = 0; j < 7; j++) {
            top[j] = 0;
            for (int i = 0; i < 6; i++)
                discs[i][j] = EMPTY;
        }
        nMoves = 0;
        hash = 0;
    }
    
    /**
     * Return the number of moves already made.
     * 
     * @return the number of moves.
     */
    public int numberOfMoves() {
        return nMoves;
    }

    /**
     * Return the index of the last column selected by a player (0-6), or -1
     * if it is the first move.
     * 
     * @return the last move
     */
    public int lastMove() {
        return (nMoves > 0 ? moves[nMoves - 1] : -1);
    }
    
    /**
     * Rerturn the hash code for the current game state.
     * 
     * @return a 30 bit hash code.
     */
    public int getHash() {
        return hash;
    }
        
    /**
     * Return true if the last one was the winning move.
     * 
     * @return true if the last move caused the player to win the game
     */
    public boolean won() {
        /* The last move was winning if it allowed the opponent of the current
           player to place four discs on the same row. */
        return (countCombinations(3 - sideToMove(), 4) > 0);
    }

    /**
     * Return true if the game ended without any winning player.
     * 
     * @return true if the game is draw
     */
    public boolean draw() {
        /* The game is draw when all the 42 places in the grid have been filled
           without any player winning. */
        return (nMoves == 42 && !won());
    }

    /**
     * The player that has to move (RED or YELLOW).
     * 
     * @return the current player
     */
    public int sideToMove() {
        return (nMoves % 2 == 0 ? RED : YELLOW);
    }
    
    /**
     * Make a move for the current player.
     * 
     * @param col the column (0-6) where the player places its disc
     * 
     * An IllegalMove exception is thrown if the column is full.
     */
    public void makeMove(int col) throws IllegalMove {
        if (top[col] > 5)
            throw new IllegalMove();
        // 1. Update the hash code.
        hash ^=  ZOBRIST[top[col]][col] << 15 * (nMoves % 2);
        // 2. Place the new disk.
        discs[top[col]][col] = sideToMove();
        top[col]++;
        // 3. Record the move in the history.
        moves[nMoves] = col;
        nMoves++;
    }
    
    /**
     * Undo the last move in the history.
     */
    public void undoMove() {
        if (nMoves == 0)
            return;  // no moves to undo
        // 1. Remove the move from the history.
        nMoves--;
        int col = moves[nMoves];
        // 2. Remove the disc from the grid.
        top[col]--;
        discs[top[col]][col] = EMPTY;
        // 3. Recover the old hash code.
        hash ^=  ZOBRIST[top[col]][col] << 15 * (nMoves % 2);
    }
    
    /**
     * Return the color of the disk at the specified position.
     * 
     * @param row index of the row (0-5)
     * @param col index of the column (0-6)
     * @return RED, YELLOW or NOTHING
     */
    public int getDisc(int row, int col) {
        return discs[row][col];
    }
        
    /* Count the number of discs of the given color in the positions of
       coordinates:
       (row, col), (row + drow, col + dcol), (row + 2*drow, col + 2*dcol),
       (row + 3*drow, col + 3*dcol).  */
    private int count4(int row, int col, int drow, int dcol, int color) {
	int counter = 0;
	for (int k = 0; k < 4; k++) {
	    if (discs[row][col] == color)
		counter++;
	    row += drow;
	    col += dcol;
	}
	return counter;
    }
    
    /* Count how many sequences of 4 places have exactly target disks of the
       required color. */
    public int countCombinations(int color, int target) {
	int counter = 0;
        
        // Check along the columns.
	for (int row = 0; row < 6; row++)
	    for (int col = 0; col < 4; col++)
		if (count4(row, col, 0, 1, color) == target)
		    counter++;
        
        // Check along the rows.
	for (int row = 0; row < 3; row++)
	    for (int col = 0; col < 7; col++)
		if (count4(row, col, 1, 0, color) == target)
		    counter++;
        
        // Check along the diagonals.
	for (int row = 0; row < 3; row++)
	    for (int col = 0; col < 4; col++)
		if (count4(row, col, 1, 1, color) == target)
		    counter++;
        
        // Check along the "antidiagonals".
	for (int row = 3; row < 6; row++)
	    for (int col = 0; col < 4; col++)
		if (count4(row, col, -1, 1, color) == target)
		    counter++;
	return counter;
    }
    
    /* Compute a score that indicates who is winning by counting the difference
       in the number of possible ways to win the game.  In practice, for each
       player it is counted the number of sequences of 4 places where it still
       doesn't have any disc.  This number is considered positive for the other
       player.  The returned value is positive if the current player is winning
       and negative if he is losing.  A value of zero represents a tie
       situation.
       
       This function can be used by automated players to chose the most
       effective move.
     */
    public int score() {
	return (countCombinations(3 - sideToMove(), 0) -
		countCombinations(sideToMove(), 0));
    }
}
