package edu.wustl.cait.concurrency.labs;

import java.util.HashSet;
import java.util.Set;

import edu.wustl.cait.concurrency.Card;
import edu.wustl.cait.concurrency.Library;
import edu.wustl.cait.concurrency.Player;
import edu.wustl.cait.concurrency.Player.Opponent;
import edu.wustl.cait.concurrency.Simulator;
import edu.wustl.cait.concurrency.SwapResult;
import edu.wustl.cait.concurrency.Util;

public class Part2Lab2 {
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
			Set<Card> newCards = Library.getCards();
			newCards.removeAll(p.getCardSet());

			// for each new card, try swapping it with each card in hand
			Simulator sim = Simulator.create(Opponent.LIAM);
			for (Card newCard : newCards) {
				for (Card oldCard : oldCards) {
					Player theNewMe = p.swap(oldCard, newCard);
					int wins = sim.run(theNewMe, EXECUTIONS);
					if (wins > base) {
						results.add(new SwapResult(oldCard, newCard, wins));
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// Step 1: get the base results we can compare to later
		Player me = Player.load("me_p2e2.json");
		int base = Simulator.run(me, Opponent.LIAM, EXECUTIONS);
		System.out.println("base: " + base + " / " + EXECUTIONS);

		// Step 2: split the test into 2 halves
		Set<Card> cardSet1 = new HashSet<>();
		Set<Card> cardSet2 = new HashSet<>();
		Util.splitSet(me.getCardSet(), cardSet1, cardSet2);

		// Step 3: create 2 swappers, one for each split
		Swapper s1 = new Swapper(base, me, cardSet1);
		Swapper s2 = new Swapper(base, me, cardSet2);

		// Step 4: create a thread for each swapper
		Thread t1 = new Thread(s1);
		Thread t2 = new Thread(s2);

		// Step 5: start both threads, then join both threads
		long start = System.currentTimeMillis();
		t1.start();
		t2.start();
		t1.join(); // establish happens-before
		t2.join(); // establish happens-before
		long stop = System.currentTimeMillis();

		// Step 6: capture the time before starting and after joining, then
		// output the duration
		System.out.println("duration: " + (stop - start));

		// Step 7: output the best result across both swappers
		SwapResult max = Util.getMax(SwapResult::compareTo, s1.results,
				s2.results);
		System.out.println(max);
	}
}
