package kalah.piazzesi;

import java.util.Arrays;

public class Board {

	private int[][] pits;
	private int[] mancalas;
	private boolean freeTurn; /* set to true if the board is generated by a move that
	                          ends in mancala  */

	public Board() {
		pits = new int[2][6];
		mancalas = new int[2];
		freeTurn = false;

	}

	public void initBoard() {
		Arrays.fill(pits[0], 0, pits[0].length, 4);
		Arrays.fill(pits[1], 0, pits[0].length, 4);
		mancalas[0] = 0;
		mancalas[1] = 0;
	}

	public Board copyBoard() {
		Board boardCopy = new Board();
		boardCopy.pits[0] = Arrays.copyOf(pits[0], pits[0].length);
		boardCopy.pits[1] = Arrays.copyOf(pits[1], pits[0].length);
		boardCopy.mancalas = Arrays.copyOf(mancalas, mancalas.length);
		boardCopy.freeTurn = this.freeTurn;
		return boardCopy;
	}

	public boolean isGameOver() {
		long stones1 = Arrays.stream(pits[0]).filter(e -> e > 0).count();
		long stones2 = Arrays.stream(pits[1]).filter(e -> e > 0).count();
		return stones1 == 0 || stones2 == 0 || mancalas[0] > 24 || mancalas[1] > 24;
	}

	private int mancalaGap(int player) {
		return mancalas[player] - mancalas[(player + 1) % 2];
	}

	public int[][] getPits() {
		return pits;
	}

	public int[] getMancalas() {
		return mancalas;
	}

	public void doMove(int move, int player) {
		int stones = emptyHole(move, player);
		int currMove = move + 1;
		while (stones > 0) {
			stones = updateSide(true, stones, currMove, player);
			player = (player + 1) % 2;
			currMove = 0;
			stones = updateSide(false, stones, currMove, player);
			player = (player + 1) % 2;
		}
	}

	public int evaluateBoard(int player) {
		// heuristic value of mancalaGap is doubled because those stones are not movable
		// anymore
		int myStones = Arrays.stream(this.pits[player]).sum();
		int enemyStones = Arrays.stream(this.pits[(player + 1) % 2]).sum();
		return myStones - enemyStones + 2 * mancalaGap(player);
	}

	private int updateSide(boolean mySide, int stones, int move, int side) {
		while (stones > 0 && move < pits[0].length) {
			if (mySide && stones == 1 && pits[side][move] == 0) {
				stealPit(move, side);
			} else {
				pits[side][move] = pits[side][move] + 1;
				move = move + 1;
			}
			stones = stones - 1;
		}
		if (mySide && stones > 0) {
			mancalas[side] = mancalas[side] + 1;
			stones = stones - 1;
		}
		return stones;
	}

	private void stealPit(int pit, int player) {
		int n = 1 + emptyHole(5 - pit, (player + 1) % 2);
		mancalas[player] = mancalas[player] + n;
	}

	private int emptyHole(int hole, int player) {
		int stones = pits[player][hole];
	    pits[player][hole] = 0;
		return stones;
	}

	public boolean isFreeTurn() {
		return freeTurn;
	}

	public void setFreeTurn(boolean freeTurn) {
		this.freeTurn = freeTurn;
	}

}
