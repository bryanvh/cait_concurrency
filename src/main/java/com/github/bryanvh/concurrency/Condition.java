package com.github.bryanvh.concurrency;

import java.util.List;

public class Condition {
	private final List<Effect> effects;
	private final String name;
	private int duration;

	public Condition(List<Effect> effects, String name, int duration) {
		this.effects = effects;
		this.name = name;
		this.duration = duration;
	}
		
	public boolean isExpired() {
		return duration == 0;
	}

	void trigger(Player actor, Player target) {
		if (duration <= 0) {
			throw new IllegalStateException("condition duration <= 0");
		}
		// System.out.println("  actor: " + actor.getName() + " target: " + target.getName());
		for (Effect e : effects) {
			e.apply(actor, target);
		}
		duration--;
	}

	@Override
	public String toString() {
		return name + "/" + duration;
	}
}
