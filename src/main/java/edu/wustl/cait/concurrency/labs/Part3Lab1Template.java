package edu.wustl.cait.concurrency.labs;

import java.util.Set;

import edu.wustl.cait.concurrency.Card;
import edu.wustl.cait.concurrency.Player;

public class Part3Lab1Template {

	private static final int EXECUTIONS = 2000;

	// Step 1a: define a static int named "counter" here

	private static class Swapper implements Runnable {
		public Swapper(int base, Player p, Set<Card> oldCards) {
			// same code as prior exercise
		}

		@Override
		public void run() {
			// same code as prior exercise
			
			// Step 1b: increment counter within the inner-most loop
		}
	}

	public static void main(String[] args) throws Exception {
		// Step 2: prepare first 2 threads similar to previous exercises

		// Step 3: create a third thread that waits for all permutations
		Thread t3 = new Thread(() -> {
			// Step 7.1: calc the number of permutations
			// Step 7.2: output the number of permutations
			// Step 7.3: write a loop that spins while counter < permutations
			// Step 7.4: output a message after loop exits
		});

		// Step 4: start all threads, then join ONLY the 2 worker threads

		// Step 5: output the value of counter observed by main thread
	}
}
