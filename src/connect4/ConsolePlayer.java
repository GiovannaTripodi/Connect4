/*
 * Code used in the "Software Engineering" course.
 *
 * Copyright 2017 by Claudio Cusano (claudio.cusano@unipv.it)
 * Dept of Electrical, Computer and Biomedical Engineering,
 * University of Pavia.
 */
package connect4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * A player that enters the moves from the terminal.
 * 
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class ConsolePlayer implements Player {

    BufferedReader reader;
        
    public ConsolePlayer() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    @Override
    public int selectMove(Grid g) {
        for (;;)  {
            printGrid(g);            
            System.out.print("Enter your move (1-7):");
            String input;
            try {
                input = reader.readLine();
                int col = Integer.decode(input);
                if (col >= 1 && col <= 7)
                    return col - 1;
            } catch (IOException | NumberFormatException ex) {
                System.out.println(ex);                        
                continue;
            }
            System.out.println("Invalid move!");
        }
    }
    
    private void printGrid(Grid grid) {
        for (int row = 5; row >= 0; row--) {
            for (int col = 0; col < 7; col++) {
                int d = grid.getDisc(row, col);
                String s = d == Grid.EMPTY ? " . " : (d == Grid.RED ? " X " : " O ");
                System.out.print(s);
            }
            System.out.println();
        }
        System.out.println(" 1  2  3  4  5  6  7");
    }
}
