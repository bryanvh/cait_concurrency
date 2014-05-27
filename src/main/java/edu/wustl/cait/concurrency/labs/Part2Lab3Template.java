package edu.wustl.cait.concurrency.labs;

import java.util.HashSet;
import java.util.Set;

import edu.wustl.cait.concurrency.Card;
import edu.wustl.cait.concurrency.Player;
import edu.wustl.cait.concurrency.SwapResult;

public class Part2Lab3Template {
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
			this.results = new HashSet<>();
		}

		@Override
		public void run() {
			// Step 1: Start with the same code you wrote in the previous
			// exercise. However this time you need to check for interruption
			// just prior to calling Player.swap()
		}
	}

	public static void main(String[] args) throws Exception {
		// Step 2: Start with the same code you wrote in the previous exercise.
		// However this time create another thread called "timer" that sleeps
		// for 1 second, then interrupts both worker threads.

		// Step 3: Start the timer thread immediately after starting the two
		// worker threads.
	}
}
