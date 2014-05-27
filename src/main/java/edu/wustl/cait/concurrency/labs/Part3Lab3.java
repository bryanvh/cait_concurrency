package edu.wustl.cait.concurrency.labs;

import java.util.HashSet;
import java.util.Set;

import edu.wustl.cait.concurrency.SwapResult;
import edu.wustl.cait.concurrency.Util;
import edu.wustl.cait.concurrency.Util.Registerable;

/**
 * The Swapper class demonstrates several forms of unsafe publication.
 */
public class Part3Lab3 {

	private static class UnsafePub implements Registerable {
		// TODO: unsafe!
		public Set<SwapResult> results;

		// private Set<SwapResult> results;

		public UnsafePub() {
			// TODO: unsafe publication!
			Util.register(this);
			// remove and call register from static factory

			results = new HashSet<SwapResult>();
		}

		// public static Swapper create() {
		// Swapper s = new Swapper();
		// Util.register(s);
		// return s;
		// }

		@Override
		public Set<SwapResult> getResults() {
			// TODO: unsafe publication!
			return results;

			// return  (results);
		}
	}

	public static void main(String[] args) throws Exception {
		UnsafePub p1 = new UnsafePub();
		UnsafePub p2 = new UnsafePub();
		// Swapper p1 = Swapper.create();
		// Swapper p2 = Swapper.create();

		// Step N
		Util.verifySafePublication(UnsafePub.class);
	}
}
