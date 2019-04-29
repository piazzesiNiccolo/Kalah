package kalah.piazzesi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Manager {
	private Board currBoard;

	public Manager(Board board) {
		this.currBoard = board;
	}

	public List<Board> nextStates(boolean isMe) {
		int player = isMe ? 1 : 0;
		List<Board> boards = new ArrayList<>();
		List<Integer> moves = this.legalMoves(player);
		moves.forEach(m -> boards.add(newState(player, m)));
		return boards;
	}

	private Board newState(int player, int move) {
		Board b = currBoard.copyBoard();
		b.doMove(move, player);
		b.setFreeTurn(isFree(move, player));
		return b;
	}

	public List<Integer> legalMoves(int player) {
		int[] side = currBoard.getPits()[player];
		List<Integer> possibleMoves = Stream
				.iterate(0, i -> i + 1)
				.limit(side.length)
				.filter(i -> side[i] > 0)
				.collect(Collectors.toList());
		return possibleMoves;
	}

	private boolean isFree(int move, int player) {
		int[] t = currBoard.getPits()[player];
		return (move + t[move]) % 13 == t.length;
	}

	public Board getCurrBoard() {
		return currBoard;
	}

	public void setCurrBoard(Board currBoard) {
		this.currBoard = currBoard;
	}
}