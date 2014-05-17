package com.github.bryanvh.concurrency.labs;

import java.util.HashSet;
import java.util.Set;

import com.github.bryanvh.concurrency.Card;
import com.github.bryanvh.concurrency.Library;
import com.github.bryanvh.concurrency.Player;
import com.github.bryanvh.concurrency.Player.Opponent;
import com.github.bryanvh.concurrency.RunnableSwapper;
import com.github.bryanvh.concurrency.Simulator;
import com.github.bryanvh.concurrency.SwapResult;
import com.github.bryanvh.concurrency.Util;

/**
 * Let's use a shared counter that only one thread updates. When that thread is
 * done, output the final value of that counter as seen by the writer thread.
 * Then, output the counter value as seen by the other thread that never updates
 * the counter.
 * 
 * This might turn out to be too difficult to reproduce in a dependable manner.
 *
 */
public class Part3Lab1 {
	private static final int EXECUTIONS = 2000;
	private static boolean STOP = false;

	private static class Swapper extends RunnableSwapper {
		public Swapper(int base, Player me, Set<Card> oldCards) {
			super(base, me, oldCards);
		}

		@Override
		public Set<SwapResult> run(int base, Player p, Set<Card> oldCards) {
			Set<SwapResult> results = new HashSet<SwapResult>();
			Set<Card> newCards = Library.getCards();
			newCards.removeAll(p.getCardSet());

			// for each new card, try swapping it with each card in hand
			Simulator sim = Simulator.create(Opponent.LIAM);
			System.out.println("test cases: "
					+ (newCards.size() * oldCards.size()));
			for (Card newCard : newCards) {
				for (Card oldCard : oldCards) {
					// CHECK FOR INTERRUPTION
					if (STOP) {
						// no cleanup required, just return
						System.out.println("stopped! "
								+ Thread.currentThread().getName());
						return results;
					}

					Player theNewMe = p.swap(oldCard, newCard);
					int wins = sim.run(theNewMe, EXECUTIONS);
					if (wins > base) {
						results.add(new SwapResult(oldCard, newCard, wins));
					}
				}
			}

			return results;
		}
	}

	public static void main(String[] args) throws Exception {
		Player me = Player
				.load("/com/github/bryanvh/concurrency/config/me.json");

		// Step 1: get the base results we can compare to later
		int base = Simulator.run(me, Opponent.LIAM, EXECUTIONS);
		System.out.println("base: " + base);

		// Step 2: split the test into 2 halves
		Set<Card> cardSet1 = new HashSet<Card>();
		Set<Card> cardSet2 = new HashSet<Card>();
		Util.splitSet(me.getCardSet(), cardSet1, cardSet2);

		RunnableSwapper s1 = new Swapper(base, me, cardSet1);
		RunnableSwapper s2 = new Swapper(base, me, cardSet2);

		Thread t1 = new Thread(s1, "s1");
		Thread t2 = new Thread(s2, "s2");

		long start = System.currentTimeMillis();
		t1.start();
		t2.start();

		// t1.join(); // establish happens-before
		// t2.join(); // establish happens-before
		Thread.sleep(1000);
		// s1.stop();
		// s2.stop();
		STOP = true;

		System.out.println("duration: " + (System.currentTimeMillis() - start));

		t1.join(); // establish happens-before
		t2.join(); // establish happens-before
		System.out.println("duration: " + (System.currentTimeMillis() - start));

		SwapResult max = Util.getMax(SwapResult::compareTo, s1.getResults(),
				s2.getResults());
		System.out.println(max);
	}
}
