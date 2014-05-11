package com.github.bryanvh.concurrency;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.bryanvh.concurrency.Player.Opponent;

public final class Simulator {
	private final Player lhs;
	private final Player rhs;
	private final Speed speed;

	public enum Speed {
		SLOW(100), LIESURELY(50), FAST(0);
		private final long ms;

		private Speed(long ms) {
			this.ms = ms;
		}

		public void sleep() {
			if (ms > 0) {
				try {
					Thread.sleep(ms);
				} catch (InterruptedException ignored) {
				}
			}
		}
	}

	private Simulator(Player lhs, Player rhs, Speed speed) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.speed = speed;
	}

	public Map<Player, Integer> run(int n) {
		Map<Player, Integer> results = new HashMap<Player, Integer>();
		results.put(lhs, 0);
		results.put(rhs, 0);

		for (int i = 0; i < n; i++) {
			speed.sleep();
			lhs.reset();
			rhs.reset();
			if (run() == lhs) {
				results.put(lhs, results.get(lhs) + 1);
			} else {
				results.put(rhs, results.get(rhs) + 1);
			}
		}

		return results;
	}

	public Player run() {
		int round = 0;
		while (true) {
			round++;
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

	public static Simulator create(Player p, Opponent o, Speed speed)
			throws IOException {
		Player opponent = o.load();
		return new Simulator(p, opponent, speed);
	}

	public static Simulator createSlow(Player p, Opponent o) throws IOException {
		Player opponent = o.load();
		return new Simulator(p, opponent, Speed.SLOW);
	}

	public static Simulator createFast(Player p, Opponent o) throws IOException {
		Player opponent = o.load();
		return new Simulator(p, opponent, Speed.FAST);
	}

	public static Simulator createLiesurely(Player p, Opponent o)
			throws IOException {
		Player opponent = o.load();
		return new Simulator(p, opponent, Speed.LIESURELY);
	}
}
