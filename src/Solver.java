import java.util.*;
import java.io.*;

public class Solver {
	
	public static void solveSudoku(char[][] board) {
		
		// BitSets for storing used numbers
		BitSet[] row = new BitSet[9];
		BitSet[] col = new BitSet[9];
		BitSet[] subgrid = new BitSet[9];
		
		// Initializing BitSets 
		for (int i = 0; i < 9; i++) {
			row[i] = new BitSet(9);
			col[i] = new BitSet(9);
			subgrid[i] = new BitSet(9);
		}
		
		// Setting already used numbers to True
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				char digit = board[r][c];
				if (digit != '.') {
					int idx = digit - '0' - 1;
					row[r].set(idx);
					col[c].set(idx);
					int currentSubgrid = getSubgrid(r, c);
					subgrid[currentSubgrid].set(idx);
				}
			}
		}
		
		solve(board, 0, 0, row, col, subgrid);
	}
	
	private static boolean solve(char[][] board, int rowStart, int colStart, BitSet[] row, BitSet[] col, BitSet[] subgrid) {
		// Get the next empty cell and store as pair
		Pair pos = getNextEmpty(board, rowStart, colStart);
		
		// Base case: no empty positions
		if (pos == null) 
			return true;
		
		// Get the union of sets to know all the used numbers
		int currentSubgrid = getSubgrid(pos.r, pos.c);
		BitSet used = new BitSet(9);
		used.or(row[pos.r]);
		used.or(col[pos.c]);
		used.or(subgrid[currentSubgrid]);
		
		// Check if all the numbers (1 - 9) are used
		if (used.cardinality() == 9)
			return false;
		
		// Otherwise loop over all possible numbers
		// Try all possible numbers using backtracking
		for (int i = 0; i < 9; i++) {
			if (!used.get(i)) {
				board[pos.r][pos.c] = (char) (i + '0' + 1);
				row[pos.r].set(i);
				col[pos.c].set(i);
				subgrid[currentSubgrid].set(i);
				
				if (solve(board, pos.r, pos.c, row, col ,subgrid))
					return true;
				
				row[pos.r].set(i, false);
				col[pos.c].set(i, false);
				subgrid[currentSubgrid].set(i, false);
			}
		}
		board[pos.r][pos.c] = '.';
		return false;
	}
	
	// Get the next empty cell
	private static Pair getNextEmpty(char[][] board, int rowStart, int colStart) {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (board[r][c] == '.') 
					return new Pair(r, c);
			}
		}
		return null;
	}
	
	// Finds the index of 3x3 sub-grid given the row and column indices
	private static int getSubgrid(int row, int col) {
		return (row / 3) * 3 + col / 3;
	}
	
	private static char[][] flatTo2D(char[] flat) {
		char[][] result = new char[9][9];
		for (int i = 0; i < 81; i += 9) {
			int r = i / 9;
			for (int c = 0; c < 9; c++)
				result[r][c] = flat[i + c];
		}
		return result;
	}
	
	private static void printBoard(char[][] board) {
		for (char[] r : board) {
			for (char c : r)
				System.out.print(c + " ");
			System.out.println();
		}
	}
	
	static class Pair {
		int r;
		int c;
		
		public Pair(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("tests.txt"));
		long totalTime = 0;
		double puzzleCount = 0;
		while (sc.hasNextLine()) {
			char[] flatBoard = new char[81];
			String testBoard = sc.nextLine();
			for (int i = 0; i < 81; i++)
				flatBoard[i] = testBoard.charAt(i);
			
			char[][] board = flatTo2D(flatBoard);
			long start = System.currentTimeMillis(); 
			solveSudoku(board);
			long finish = System.currentTimeMillis();
			long time = finish - start;
			totalTime += time;
			puzzleCount++;
			printBoard(board);
			System.out.println();
			System.out.println("Finished in " + time + "ms\n");
		}
		System.out.println("Average runtime is: " + totalTime / puzzleCount + "ms");
	}

}
