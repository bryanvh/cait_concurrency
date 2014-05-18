package edu.wustl.cait.concurrency;

import edu.wustl.cait.concurrency.Player.Opponent;

public final class Simulator {
	private final Player rhs;
	private final Speed speed;

	public enum Speed {
		SLOW(50, 0), LIESURELY(25, 0), FAST(0, 0);
		private final long ms;
		private final int ns;

		private Speed(long ms, int ns) {
			this.ms = ms;
			this.ns = ns;
		}

		public void sleep() throws InterruptedException {
			if (ms > 0 || ns > 0) {
				Thread.sleep(ms, ns);
			}
		}
	}

	private Simulator(Player rhs, Speed speed) {
		this.rhs = rhs;
		this.speed = speed;
	}

	public int run(Player lhs, int n) {
		int lhsWins = 0;

		try {
			speed.sleep();
			for (int i = 0; i < n; i++) {
				lhs.reset();
				rhs.reset();
				if (run(lhs) == lhs) {
					lhsWins++;
				}
			}
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}

		return lhsWins;
	}

	public Player run(Player lhs) {
		// int round = 0;
		while (true) {
			// round++;
			// System.out.println("------" + round + "------");

			lhs.act(rhs);
			if (rhs.isDefeated()) {
				return lhs;
			}
			if (lhs.isDefeated()) {
				return rhs;
			}

			rhs.act(lhs);
			if (lhs.isDefeated()) {
				return rhs;
			}
			if (rhs.isDefeated()) {
				return lhs;
			}
			// System.out.println(lhs);
			// System.out.println(rhs);
		}
	}
	
	public static int run(Player p, Opponent o, int n) {
		return create(o).run(p, n);
	}

	public static Simulator create(Opponent o) {
		return create(o, Speed.LIESURELY);
	}

	public static Simulator create(Opponent o, Speed speed) {
		return new Simulator(o.load(), speed);
	}
}
