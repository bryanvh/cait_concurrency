package edu.wustl.cait.concurrency;

public class SwapResult implements Comparable<SwapResult> {
	private final Card left;
	private final Card right;
	private final int wins;

	public SwapResult(Card left, Card right, int wins) {
		this.left = left;
		this.right = right;
		this.wins = wins;
	}

	public Card getLeft() {
		return left;
	}

	public Card getRight() {
		return right;
	}

	public int getWins() {
		return wins;
	}

	@Override
	public String toString() {
		return "[" + left + "," + right + "," + wins + "]";
	}

	@Override
	public int compareTo(SwapResult sr) {
		return Integer.compare(wins, sr.wins);
	}
}
