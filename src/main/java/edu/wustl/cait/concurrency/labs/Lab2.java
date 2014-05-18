package edu.wustl.cait.concurrency.labs;

import java.util.HashSet;
import java.util.Set;

import edu.wustl.cait.concurrency.Card;
import edu.wustl.cait.concurrency.Library;
import edu.wustl.cait.concurrency.Player;
import edu.wustl.cait.concurrency.RunnableSwapper;
import edu.wustl.cait.concurrency.Simulator;
import edu.wustl.cait.concurrency.SwapResult;
import edu.wustl.cait.concurrency.Util;
import edu.wustl.cait.concurrency.Player.Opponent;

public class Lab2 {
	private static final int EXECUTIONS = 2000;

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
			for (Card newCard : newCards) {
				for (Card oldCard : oldCards) {
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
		Player me = Player.load("me.json");

		// Step 1: get the base results we can compare to later
		int base = Simulator.run(me, Opponent.LIAM, EXECUTIONS);
		System.out.println("base: " + base + " / " + EXECUTIONS);

		// Step 2: split the test into 2 halves
		Set<Card> cardSet1 = new HashSet<Card>();
		Set<Card> cardSet2 = new HashSet<Card>();
		Util.splitSet(me.getCardSet(), cardSet1, cardSet2);

		RunnableSwapper s1 = new Swapper(base, me, cardSet1);
		RunnableSwapper s2 = new Swapper(base, me, cardSet2);

		Thread t1 = new Thread(s1);
		Thread t2 = new Thread(s2);

		long start = System.currentTimeMillis();
		t1.start();
		t2.start();
		t1.join(); // establish happens-before
		t2.join(); // establish happens-before
		long stop = System.currentTimeMillis();
		System.out.println("duration: " + (stop - start));

		SwapResult max = Util.getMax(SwapResult::compareTo, s1.getResults(),
				s2.getResults());
		System.out.println(max);
	}
}
