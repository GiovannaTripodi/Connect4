 /*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package connect4;

import java.util.logging.Logger;


/**
 * A basic engine that drives a computer player.
 *
 * The main algorithm uses a minimax strategy with alpha-beta pruning
 * (https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning).  With a
 * transposition table (https://en.wikipedia.org/wiki/Transposition_table).
 * Even though the algorithm is quite basic it must be strong enough for
 * casual players.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class AIPlayer implements Player {
        
    private final static int INF = 20000;
    private final static int LOSS = 10000;
    private final static int[] COLUMNS = {3, 2, 4, 1, 5, 0, 6};  // order in which the columns are tried
    public static final int TABLE_BITS = 16;
    public static final int HASH_BITS = 30;
    public static final int MILLISEC_PER_MOVE = 1000;
        
    int selectedMove;
    int depth;
    int[] killerMoves;
    int[][] transpositionTable;
    
    /**
     * Create the player.
     */
    public AIPlayer() {
        killerMoves = new int[42];                
        
        /* For each entry the four fields are:
           [0] hash code
           [1] depth
           [2] score
           [3] best move
        */
        transpositionTable = new int [1 << TABLE_BITS][4];
    }

    /* Good moves should be tried first.  This function places the move m at the
       beginning of the list of moves. */
    private static void prioritizeMove(int[] moves, int m) {
        if (moves[0] == m)
            return;
        int a, b;
        a = moves[0];
        moves[0] = m;
        for (int i = 1; i < moves.length; i++) {
            b = moves[i];
            moves[i] = a;
            a = b;
            if (a == m)
                break;
        }
    }
    
    /* The main search function. */
    private int alfaBeta(Grid grid, int alpha, int beta, int depth) {
        if (depth == 0)
            return grid.score();

        if (grid.draw())
            return 0;

        if (grid.won())
            return -LOSS - depth;

        int bestVal = -INF;
        int bestMove = 0;
        
        int[] moves = COLUMNS.clone();
        
        // Take the central TABLE_BITS of the hash code to get an entry in the table.
        int hash = (grid.getHash() >> ((HASH_BITS - TABLE_BITS) / 2)) & ((1 << TABLE_BITS) - 1);
        if (transpositionTable[hash][0] == grid.getHash()) {
            /* Hash hit: if the depth of the entry is enough, we can just take
               the move in the table, otherwise the move is just prioritized in
               the regular search. */
            if (transpositionTable[hash][1] >= depth + grid.numberOfMoves()) {
                selectedMove = transpositionTable[hash][3];
                return transpositionTable[hash][2];
            } else
                prioritizeMove(moves, transpositionTable[hash][3]);
        }
        
        if (depth - 1 >= killerMoves.length) {
            Logger.getLogger(AIPlayer.class.getName()).info(" " + depth + "  " + killerMoves.length);        
        }
        
        /* Killer moves are moves that recently caused a cut at the same depth.
           They are good candidates and should be tried first. */
        prioritizeMove(moves, killerMoves[depth - 1]);
        
        /* For each move ... */
        for (int col : moves) {
            try {
                grid.makeMove(col);
            } catch (Grid.IllegalMove ignored) {
                continue;
            }
            int val = -alfaBeta(grid, -beta, -alpha, depth - 1);
            grid.undoMove();
            if (val >= beta) {
                killerMoves[depth - 1] = col;
                return val;
            }
            if (val > bestVal) {
                bestVal = val;
                bestMove = col;
            }            
            if (val > alpha)
                alpha = val;
        }

        /* Record the best move found in the table. */
        transpositionTable[hash][0] = grid.getHash();
        transpositionTable[hash][1] = depth + grid.numberOfMoves();
        transpositionTable[hash][2] = bestVal;
        transpositionTable[hash][3] = bestMove;
     
        selectedMove = bestMove;
	return bestVal;
    }
    
    /* Randpomly choose a move. */
    public int randomSelection() {
        int  x = 0;
        for(int i = 0; i < 6; i++)
            if(Math.random() < 0.5)
                x++;  
        return x;
    }
    
    @Override
    public int selectMove(Grid grid) {
        if (grid.numberOfMoves() < 3)
            return randomSelection();
        long startTime = System.currentTimeMillis();
        long endTime = startTime;
        int depth = 0;
        int score = 0;
        while (endTime - startTime < MILLISEC_PER_MOVE && grid.numberOfMoves() + depth < 42) {
            depth++;
            score = alfaBeta(grid, -INF, INF, depth);
            endTime = System.currentTimeMillis();            
        } 
        String msg = "Best move: " + (selectedMove + 1) + " (" + score + ")   found at depth " + depth;
        Logger.getLogger(AIPlayer.class.getName()).info(msg);        
        return selectedMove;
    }
}
