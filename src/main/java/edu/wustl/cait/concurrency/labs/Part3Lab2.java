package edu.wustl.cait.concurrency.labs;

/**
 * Stale read; cannot be solved using a volatile.
 *
 */
public class Part3Lab2 {
	public static class Counter {
		private volatile long value = 0;

		// private AtomicLong value = new AtomicLong(0);

		public void increment() {
			value++;
			// value.getAndIncrement();
		}
	}

	public static void main(String[] args) throws Exception {
		Counter counter = new Counter();
		
		Runnable r = () -> {
			for (long i = 0; i < 10_000_000; i++) {
				counter.increment();
			}
			System.out.println(Thread.currentThread().getName() + ": "
					+ counter.value);
		};

		Thread t1 = new Thread(r, "t1");
		Thread t2 = new Thread(r, "t2");
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(counter.value);

	}

}
