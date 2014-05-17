package com.github.bryanvh.concurrency;

import java.util.HashSet;
import java.util.Set;

public abstract class RunnableSwapper implements Runnable {
	private final int base;
	private final Player me;
	private final Set<Card> oldCards;
	private Set<SwapResult> results;

	public RunnableSwapper(int base, Player me, Set<Card> oldCards) {
		this.base = base;
		this.me = me;
		this.oldCards = oldCards;
		results = new HashSet<SwapResult>();
	}

	protected abstract Set<SwapResult> run(int base, Player p,
			Set<Card> oldCards);

	@Override
	public void run() {
		results = run(base, me, oldCards);
	}

	public Set<SwapResult> getResults() {
		return results;
	}

}