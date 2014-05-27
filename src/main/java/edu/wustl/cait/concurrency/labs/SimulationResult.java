package edu.wustl.cait.concurrency.labs;

import java.util.List;

public class SimulationResult implements Comparable<SimulationResult> {
	public final long wins;
	public final List<String> hand;

	public SimulationResult(long wins, List<String> hand) {
		this.wins = wins;
		this.hand = hand;
	}

	@Override
	public int compareTo(SimulationResult sr) {
		return Long.compare(wins, sr.wins);
	}
	
	@Override
	public String toString() {
		return wins + ": " + hand;
	}
}
