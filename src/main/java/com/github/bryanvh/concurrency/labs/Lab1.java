package com.github.bryanvh.concurrency.labs;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.github.bryanvh.concurrency.Player.Opponent;
import com.github.bryanvh.concurrency.Player;
import com.github.bryanvh.concurrency.Simulator;

public class Lab1 {

	public static void main(String[] args) throws Exception {
		Player me = Player.load("/com/github/bryanvh/concurrency/config/me.json");
		Simulator sim = Simulator.createFast(me, Opponent.LIAM);
		Map<Player, Integer> results = sim.run(1000);
		System.out.println("-------------");
		Set<Player> sorted = new TreeSet<Player>(results.keySet());
		for (Player p : sorted) {
			System.out.println(p.getName() + ": " + results.get(p));
		}
	}

}
