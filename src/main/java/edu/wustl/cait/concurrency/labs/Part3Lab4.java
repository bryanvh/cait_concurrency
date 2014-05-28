package edu.wustl.cait.concurrency.labs;

/**
 * Must synchronize getters and setters, not just setters
 *
 */
public class Part3Lab4 {

	// mutator is synchronized, but corresponding getter is not
	public static class Counter {
		private long value = 0;

		public long getValue() {
			return value;
		}

		public synchronized void increment() {
			value++;
		}
	}

	public static void main(String[] args) throws Exception {
		long m = 10_000_000;
		Counter c = new Counter();
		Thread worker = new Thread(() -> {
			System.out.println("Worker thread starting...");
			for (long i = 0; i < m; i++) {
				c.increment();
			}
			System.out.println("Worker thread done! " + c.getValue());
		});
		Thread waiter = new Thread(() -> {
			System.out.println("Waiter thread starting...");
			while (c.getValue() < m) {
				continue;
			}
			System.out.println("Waiter thread done! " + c.getValue());
		});

		worker.start();
		waiter.start();

		worker.join();
		System.out.println("Main thread's counter: " + c.getValue());

		// deadlock, even though increment() is synchronized
	}

}
