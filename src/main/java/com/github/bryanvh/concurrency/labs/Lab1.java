package com.github.bryanvh.concurrency.labs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.bryanvh.concurrency.Card;
import com.github.bryanvh.concurrency.Library;
import com.github.bryanvh.concurrency.Player;
import com.github.bryanvh.concurrency.Player.Opponent;
import com.github.bryanvh.concurrency.Simulator;
import com.github.bryanvh.concurrency.SwapResult;

public class Lab1 {
	private static final int EXECUTIONS = 2000;

	public static void main(String[] args) throws Exception {

		// Explain the core elements of API students will use
		// 1. Player.load()
		// 2. Simulator.createFast()
		// 3. Simulator.run()
		// 4. Library.getCards()
		// 5. Player.swap()

		Player me = Player
				.load("/com/github/bryanvh/concurrency/config/me.json");

		// Step 1: get the base results we can compare to later
		Simulator sim = Simulator.create(Opponent.LIAM);
		int base = sim.run(me, EXECUTIONS);
		System.out.println("base: " + base);

		// Step 2: figure out the set of cards we can test
		Set<Card> oldCards = me.getCardSet();
		Set<Card> newCards = Library.getCards();
		newCards.removeAll(oldCards);

		Set<SwapResult> goodSwaps = new HashSet<SwapResult>();

		long start = System.currentTimeMillis();
		
		// Step 3: for each new card, try swapping it with each card in hand
		for (Card newCard : newCards) {
			for (Card oldCard : oldCards) {
				Player theNewMe = me.swap(oldCard, newCard);
				int wins = sim.run(theNewMe, EXECUTIONS);
				if (wins > base) {
					goodSwaps.add(new SwapResult(oldCard, newCard, wins));
				}
			}
		}
		
		long stop = System.currentTimeMillis();
		System.out.println("duration: " + (stop - start));
		
		// TODO: make it more likely the same swap is always best
		System.out.println(Collections.max(goodSwaps));
	}

	public static void part2Lab3(String[] args) throws Exception {
		// now, try to put the "tasks" (card pairs) in a queue that both threads
		// share - need to synchronize!
		// Queue<Card> queue = new LinkedList<Card>(newCards);
		// MyRunnable r1 = new MyRunnable(me, queue);
		// MyRunnable r2 = new MyRunnable(me, queue);
		// Thread t1 = new Thread(r1);
		// Thread t2 = new Thread(r2);
		// t1.start(); t2.start();
		// t1.join(); t2.join();
	}

}
