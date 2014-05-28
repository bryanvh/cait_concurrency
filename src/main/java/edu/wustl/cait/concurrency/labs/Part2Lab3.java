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

/**
 * Let's assume we don't want to wait more than 2 seconds to search for a
 * result. We can start a timer thread that waits 2 seconds, then interrupts the
 * worker threads. We need the worker threads to respond to interruption, then
 * report their findings.
 *
 */
public class Part2Lab3 {
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
			for (Card oldCard : oldCards) {
				for (Card newCard : newCards) {
					// CHECK FOR INTERRUPTION
					if (Thread.interrupted()) {
						// no cleanup required, just return
						System.out.println("interrupted! "
								+ Thread.currentThread().getName());
						return;
					}

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
		Player me = Player.load("me_p2e3.json");
		int base = Simulator.run(me, Opponent.LIAM, EXECUTIONS);
		System.out.println("base: " + base);

		// Step 2: split the test into 2 halves
		Set<Card> cardSet1 = new HashSet<>();
		Set<Card> cardSet2 = new HashSet<>();
		Util.splitSet(me.getCardSet(), cardSet1, cardSet2);

		Swapper s1 = new Swapper(base, me, cardSet1);
		Swapper s2 = new Swapper(base, me, cardSet2);

		Thread t1 = new Thread(s1, "swapper 1");
		Thread t2 = new Thread(s2, "swapper 2");
		Thread timer = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignored) {
				}
				t1.interrupt();
				t2.interrupt();
			}
		});

		long start = System.currentTimeMillis();
		t1.start();
		t2.start();
		timer.start();

		t1.join(); // establish happens-before
		t2.join(); // establish happens-before
		long stop = System.currentTimeMillis();
		System.out.println("duration: " + (stop - start));

		SwapResult max = Util.getMax(SwapResult::compareTo, s1.results,
				s2.results);
		System.out.println(max);
	}
}
