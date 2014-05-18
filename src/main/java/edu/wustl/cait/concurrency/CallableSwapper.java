package edu.wustl.cait.concurrency;

import java.util.Set;
import java.util.concurrent.Callable;

public abstract class CallableSwapper implements Callable<Set<SwapResult>> {
	private final int base;
	private final Player me;
	private final Set<Card> oldCards;

	public CallableSwapper(int base, Player me, Set<Card> oldCards) {
		this.base = base;
		this.me = me;
		this.oldCards = oldCards;
	}

	protected abstract Set<SwapResult> run(int base, Player p, Set<Card> oldCards);
	
	@Override
	public Set<SwapResult> call() {
		return run(base, me, oldCards);
	}

}