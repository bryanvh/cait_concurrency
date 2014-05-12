package com.github.bryanvh.concurrency.labs;

import java.util.Set;

import com.github.bryanvh.concurrency.Card;
import com.github.bryanvh.concurrency.Library;
import com.github.bryanvh.concurrency.Player;
import com.github.bryanvh.concurrency.Player.Opponent;
import com.github.bryanvh.concurrency.Simulator;

public class Lab1 {

	public static void main(String[] args) throws Exception {
		Player me = Player
				.load("/com/github/bryanvh/concurrency/config/me.json");
		final int executions = 1;

		// Step 1: get the base results we can compare to later
		int base = Simulator.createFast(me, Opponent.LIAM).run(executions)
				.get(me);
		System.out.println("base: " + base);
	}

	public static void main2(String[] args) throws Exception {

		// Explain the core elements of API students will use
		// 1. Player.load()
		// 2. Simulator.createFast()
		// 3. Simulator.run()
		// 4. Library.getAllCards()
		// 5. Player.swap()

		Player me = Player
				.load("/com/github/bryanvh/concurrency/config/me.json");
		final int executions = 5000;

		// Step 1: get the base results we can compare to later
		int base = Simulator.createFast(me, Opponent.LIAM).run(executions)
				.get(me);

		// Step 2: figure out the set of cards we can test
		Set<Card> oldCards = me.getCardSet();
		Set<Card> newCards = Library.getUnusedCards(me);
		System.out.println("Testing " + newCards.size() + " new cards.");
		System.out.println("Working to improve " + base + "/" + executions);

		// Step 3: for each new card, try swapping it with each card in hand
		for (Card newCard : newCards) {
			for (Card oldCard : oldCards) {
				Player theNewMe = me.swap(oldCard, newCard);
				Simulator sim = Simulator.createFast(theNewMe, Opponent.LIAM);
				int wins = sim.run(executions).get(theNewMe);
				if (wins > base) {
					System.out.println("Found a swap! " + oldCard.getName()
							+ " / " + newCard.getName() + " - " + wins);
				}
			}
		}
	}
}
