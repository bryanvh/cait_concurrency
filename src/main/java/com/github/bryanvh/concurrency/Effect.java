package com.github.bryanvh.concurrency;

public interface Effect {
	public void apply(Player actor, Player target);
}
