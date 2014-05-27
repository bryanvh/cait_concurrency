package edu.wustl.cait.concurrency.labs;

import java.util.HashSet;
import java.util.Set;

import edu.wustl.cait.concurrency.Card;
import edu.wustl.cait.concurrency.Player;
import edu.wustl.cait.concurrency.SwapResult;

public class Part2Lab2Template {
	private static final int EXECUTIONS = 2000;

	private static class Swapper implements Runnable {
		private final int base;
		private final Player p;
		private final Set<Card> oldCards;
		private final Set<SwapResult> results;

		public Swapper(int base, Player p, Set<Card> oldCards) {
			this.base = base;
			this.p = p;
			this.oldCards = oldCards;
			this.results = new HashSet<SwapResult>();
		}

		@Override
		public void run() {
			// run simulations; populate "results" set
		}
	}

	public static void main(String[] args) throws Exception {
		// Step 1: get the base results we can compare to later

		// Step 2: split the test into 2 halves

		// Step 3: create 2 swappers, one for each split

		// Step 4: create a thread for each swapper

		// Step 5: start both threads, then join both threads

		// Step 6: capture the time before starting and after joining, then
		// output the duration

		// Step 7: output the best result across both swappers
	}
}
