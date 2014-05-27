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

public class Part3Lab1 {

	private static final int EXECUTIONS = 2000;
	private static int counter = 0;

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

					// increment static counter here!
					counter++;

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
		// Step 1: add a static int named "counter"

		// Step 2:
		// Get the base results we can compare to later
		Player me = Player.load("me_p2e2.json");
		int base = Simulator.run(me, Opponent.LIAM, EXECUTIONS);
		System.out.println("base: " + base + " / " + EXECUTIONS);

		// Split the test into 2 halves
		Set<Card> cardSet1 = new HashSet<>();
		Set<Card> cardSet2 = new HashSet<>();
		Util.splitSet(me.getCardSet(), cardSet1, cardSet2);

		// Create 2 swappers, one for each split
		Swapper s1 = new Swapper(base, me, cardSet1);
		Swapper s2 = new Swapper(base, me, cardSet2);

		// Create a thread for each swapper
		Thread t1 = new Thread(s1);
		Thread t2 = new Thread(s2);

		// Step 3: create a third thread that waits for all permutations
		Thread t3 = new Thread(() -> {
			int perms = Library.getCards().size() - me.getCardSet().size();
			perms *= me.getCardSet().size();
			System.out.println("thread 3 waiting for permutations: " + perms);
			while (counter < perms) {
				// try {
				// Thread.sleep(1);
				// } catch (InterruptedException ignored) {
				// }

				continue;
			}
			System.out.println("thread 3 done!");
		});

		// Step 4: start all threads
		t1.start();
		t2.start();
		t3.start();

		// Step 8: join the 2 worker threads
		t1.join(); // establish happens-before
		t2.join(); // establish happens-before

		// Step 9: capture the time before starting and after joining, then
		// output the duration AND the value of counter
		System.out.println("counter: " + counter);

		// Step 10: output the best result across both swappers
		SwapResult max = Util.getMax(SwapResult::compareTo, s1.results,
				s2.results);
		System.out.println(max);

		// Observations
		// + the main thread observes final value of counter
		// + third thread stuck; unable to see current value of counter
		// + what happens if you put a sleep(1) in waiter thread's loop?
		// + what happens if you put make the counter volatile?
	}
}
