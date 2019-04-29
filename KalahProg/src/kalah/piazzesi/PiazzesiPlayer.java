package kalah.piazzesi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gj.kalah.player.Player;

public class PiazzesiPlayer implements Player {
	private Board myBoard;
	private Manager gameManager;
	private int me;
	private int opponent;
	private int maxDepth;

	public PiazzesiPlayer() {
		myBoard = new Board();
		gameManager = new Manager(myBoard);
		me = 1;
		opponent = 0;
	}

	public void start(boolean isFirst) {
		myBoard.initBoard();
		maxDepth = isFirst ? 7 : 5;

	}

	public int move() {
		int move = chooseMove();
		gameManager.setCurrBoard(myBoard);
		myBoard.doMove(move, me);
		return move;
	}

	private int alphaBeta(Board board, int depth, int alpha, int beta, boolean isMax) {
		if (board.isGameOver() || depth == 0) {
			return board.evaluateBoard(me);
		} else {
			int value;
			gameManager.setCurrBoard(board);
			Iterator<Board> iter = gameManager.nextStates(isMax).iterator();
			while (iter.hasNext() && alpha < beta) {
				Board b = iter.next();
				if (isMax) {
					value = alphaBeta(b, depth - 1, alpha, beta, b.isFreeTurn());
					alpha = Math.max(value, alpha);
				} else {
					value = alphaBeta(b, depth - 1, alpha, beta, !b.isFreeTurn());
					beta = Math.min(value, beta);
				}
			}
			return isMax ? alpha : beta;
		}
	}

	private int chooseMove() {
		List<Integer> moves = gameManager.legalMoves(me);
		List<Board> st = gameManager.nextStates(true);
		List<Integer> scores = new ArrayList<>();
		st.forEach(s -> scores.add(alphaBeta(s, maxDepth, -1000, 1000, s.isFreeTurn())));
		int bestScore = scores.stream()
				.mapToInt(Integer::intValue)
				.max()
				.getAsInt();
		return moves.get(scores.indexOf(bestScore));
	}

	public void tellMove(int move) {
		myBoard.doMove(move, opponent);

	}

}
