package edu.wustl.cait.concurrency.labs;

import edu.wustl.cait.concurrency.Player;

public class Part2Lab1Template {
	private static final int EXECUTIONS = 2000;

	public static void main(String[] args) throws Exception {
		// Step 1: call Player.load() to create a new Player
		Player me = Player.load("me_p2e1.json");

		// Step 2: Get the base results for comparison later

		// Step 3: Figure out the set of cards we can test
		// (get the set of all cards in the Library, then subtract your player's
		// cards from it, so that only "new" cards remain)

		// Step 4: Create a collection of SwapResult we can use to store the
		// outcome of each simulation

		// Step 5: For each new card, swap it with each card in your player's
		// hand, then run the simulation. Create a SwapResult and store it in a
		// collection if the # wins is higher than the base from above.

		// Step 6: Capture the system time before/after step 4 and output the
		// duration in milliseconds

		// Step 7: Use Collectoins.max() to output the best SwapResult
	}
}
