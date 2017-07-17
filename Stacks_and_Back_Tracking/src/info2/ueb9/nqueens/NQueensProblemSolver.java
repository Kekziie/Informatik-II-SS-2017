package info2.ueb9.nqueens;

import info2.ueb9.nqueens.misc.TimeCounter;

/**
 * A simple implementation of the n-queens problem and
 * a respective solver.
 * @author Martin Butz, Sebastian Otte
 */
public class NQueensProblemSolver {
    
    private boolean[][] board;          // represents the game board (true == occupied by queen)
    private int[][]     threatened;     // counts for each field the number of threatening queens.
    private int         n;              // dimension.
    private int         q;              // remaining queens.
    
    /**
     * Resets everything.
     */
    public void clear() {
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                this.board[i][j]     = false;
                this.threatened[i][j] = 0;
            }
        }
        this.q = n;
    }
    
    /**
     * Returns the next free and unthreatened position relative
     * to the current position. Positions are encoded as a tupel (array)
     * of two indices (row-index, column-index).
     */
    private int[] nextUnthreatenedField(final int[] start) {
        for (int i = start[0]; i < this.n; i++) {
            //
            final int jj = (i > start[0])?(0):(start[1] + 1);
            //
            for (int j = jj; j < this.n; j++) {
                if (!this.board[i][j] && (this.threatened[i][j] == 0)) {
                    return new int[]{i, j};
                }
            }
        }
        //
        // no unthreatened free field.
        //
        return null;
    }
    
    /**
     * Returns whether the given coordinate lays within the game board. 
     */
    private boolean inBound(final int i, final int j) {
        if (i < 0 || i >= n || j < 0 || j >= n) return false;
        return true;
    }
    
    /**
     * A helper function, which casts a "ray" from the current position (i, j) by successively
     * incrementing i and j with incri and incrj, respectively. For all fields (i', j') within the path of this "ray",
     * 'value' is added to its threatened ctr. 
     */
    private void addThreateningLine(final int i, final int j, final int incri, final int incrj, final int value) {
        
        int ii = i;
        int jj = j;
        
        while (this.inBound(ii, jj)) {
            this.threatened[ii][jj] += value;
            ii += incri;
            jj += incrj;
        }
    }
    
    /**
     * Places a queen to the given position. 
     */
    private void placeQueen(final int[] pos) {
        final int i = pos[0];
        final int j = pos[1];
        // place queen at the position (i, j)
        board[i][j] = true;
        // add Threatening Lines in all 8 directions
        addThreateningLine(i, j, 0, 1, 1);
        addThreateningLine(i, j, 1, 1, 1);
        addThreateningLine(i, j, 1, 0, 1);
        addThreateningLine(i, j, 1, -1, 1);
        addThreateningLine(i, j, 0, -1, 1);
        addThreateningLine(i, j, -1, -1, 1);
        addThreateningLine(i, j, -1, 0, 1);
        addThreateningLine(i, j, -1, 1, 1); 
        // decrease remaining queens
        q--;
    }
    
    /**
     * Removes the queen from the given position. 
     */
    private void unplaceQueen(final int[] pos) {
        final int i = pos[0];
        final int j = pos[1];
        // unplace queen from position (i, j)
        board[i][j] = false;
        // delete Threatening Lines
        addThreateningLine(i, j, 0, 1, -1);
        addThreateningLine(i, j, 1, 1, -1);
        addThreateningLine(i, j, 1, 0, -1);
        addThreateningLine(i, j, 1, -1, -1);
        addThreateningLine(i, j, 0, -1, -1);
        addThreateningLine(i, j, -1, -1, -1);
        addThreateningLine(i, j, -1, 0, -1);
        addThreateningLine(i, j, -1, 1, -1); 
        // increase remaining queens
        q++;
    }
    
    /**
     * The recursive back-tracking method.
     */
    private boolean backTrack(final int[] start) {
    	// returns true, if remaining queens are 0
    	if (q == 0)
        	return true;
    	// position of start should be in dimension n x n 
    	if (start != null && inBound(start[0],start[1])) {
    		// place queen at position of start
	        placeQueen(start);
	        // backtracking a solution for already placed queens
	        if (backTrack(nextUnthreatenedField(start)))
	        	return true;
	        // unplace queen
	        unplaceQueen(start);
	        // recursive function of backtrack for trying another solution statement
	        return backTrack(nextUnthreatenedField(start));
    	}  
    	return false;
    }
    
    /**
     * Tries to solve the instance of the problem. Returns true in the case success
     * and false otherwise.
     */
    public boolean solve() {
        // 
        // start at the top left position.
        //
        return backTrack(new int[]{0, 0});
    }
    
    /**
     * Prints the current game situation.
     */
    public void print() {
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.board[i][j]) {
                    System.out.print("[Q]");
                } else if (this.threatened[i][j] > 0) {
                    System.out.print("[.]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
    }
    
    
    public NQueensProblemSolver(final int n) {
        this.threatened = new int[n][n];
        this.board     = new boolean[n][n];
        this.n         = n;
        this.clear();
    }
    
   
    
    public static void test(final int n) {
        NQueensProblemSolver solver = new NQueensProblemSolver(n);
        final TimeCounter tc = new TimeCounter();
        //
        tc.reset();
        final boolean result = solver.solve();
        //
        final long value = tc.value();
        //
        if (result) {
            System.out.println("Solved NQP for n = " + n + " in " + value + "ms.");
        } else {
            System.out.println("Could not solve NQP for n = " + n + " in " + value + "ms.");
        }
        System.out.println();
        solver.print();
        System.out.println();
    }
    
    
    public static void main(String[] args) {
        for (int i = 1; i < 20; i++) {
            test(i);
        }
    }
    
    
    
}