package edu.wustl.cait.concurrency.labs;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.wustl.cait.concurrency.Card;
import edu.wustl.cait.concurrency.Library;
import edu.wustl.cait.concurrency.Player;
import edu.wustl.cait.concurrency.Simulator;
import edu.wustl.cait.concurrency.SwapResult;
import edu.wustl.cait.concurrency.Util;
import edu.wustl.cait.concurrency.Player.Opponent;

/**
 * The Swapper class demonstrates several forms of unsafe publication.
 */
public class Part3Lab2 {
	private static final int EXECUTIONS = 2000;

	private static class Swapper implements Runnable {
		private final int base;
		private final Player p;
		private final Set<Card> oldCards;
		private Set<SwapResult> results;

		public Swapper(int base, Player p, Set<Card> oldCards) {
			this.base = base;
			this.p = p;
			this.oldCards = oldCards;
			results = new HashSet<SwapResult>();

			// TODO: unsafe publication!
			Util.register(this);
		}

		public Set<SwapResult> getResults() {
			// TODO: unsafe publication!
			return results;
		}

		public int tabulate() {
			// TODO: fix use of alien method (either make Swapper thread-safe or
			// pass a CopyOnWriteArraySet)
			return Util.tabulate(results);
		}

		@Override
		public void run() {
			results = run(base, p, oldCards);
		}

		public Set<SwapResult> run(int base, Player p, Set<Card> oldCards) {
			Set<SwapResult> results = new HashSet<SwapResult>();
			Set<Card> newCards = Library.getCards();
			newCards.removeAll(p.getCardSet());

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
		System.out.println("base: " + base);

		// Step 2: split the test into 2 halves
		Set<Card> cardSet1 = new HashSet<Card>();
		Set<Card> cardSet2 = new HashSet<Card>();
		Util.splitSet(me.getCardSet(), cardSet1, cardSet2);

		Swapper s1 = new Swapper(base, me, cardSet1);
		Swapper s2 = new Swapper(base, me, cardSet2);

		Thread t1 = new Thread(s1, "s1");
		Thread t2 = new Thread(s2, "s2");

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
