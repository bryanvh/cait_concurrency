package edu.wustl.cait.concurrency.labs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;

public class SimulationTask extends RecursiveTask<SimulationResult> {
	private static final long serialVersionUID = 1L;

	private static final AtomicLong s = new AtomicLong(0);

	private final List<String> cards;
	private final Set<String> candidates;
	private final int position;
	private final int goal;
	private final boolean isSwapScenario;

	public SimulationTask(List<String> cards, Set<String> candidates, int p,
			int goal, boolean isSwapScenario) {
		this.cards = cards;
		this.candidates = candidates;
		this.position = p;
		this.goal = goal;
		this.isSwapScenario = isSwapScenario;
	}

	private SimulationResult simulate() {
		return new SimulationResult(s.incrementAndGet(), cards);
	}

	@Override
	protected SimulationResult compute() {
		// 1. simulate the current hand
		// 2. increment position
		// 3. create sub-task for each swap
		// 4. create 1 more sub-task for non-swap
		// 5. return the max of current result and results from all sub-tasks

		if (position == cards.size()) {
			return simulate();
		}

		Set<SimulationResult> results = new HashSet<>();
		if (isSwapScenario) {
			results.add(simulate());
		}

		// Spawn new sub-task for "next" root
		List<SimulationTask> tasks = new ArrayList<>(candidates.size());
		if (position + 1 < cards.size()) {
			SimulationTask task = new SimulationTask(cards, candidates,
					position + 1, goal, false);
			tasks.add(task);
			task.fork();
		}

		for (String c : candidates) {
			List<String> newCards = new ArrayList<>(cards);
			newCards.set(position, c);
			Set<String> newCandidates = new HashSet<>(candidates);
			newCandidates.remove(c);
			SimulationTask task = new SimulationTask(newCards, newCandidates,
					position + 1, goal, true);
			tasks.add(task);
			task.fork();
		}

		for (SimulationTask st : tasks) {
			SimulationResult sr = st.join();
			if (sr.wins >= goal) {
				results.add(sr);
			}
		}

		return Collections.max(results);
	}

	public static final Set<String> library = new HashSet<>();
	static {
		fill(library, 20);
		System.out.println("Library: " + library);
	}

	private static void fill(Collection<String> collection, int n) {
		char c = 'A';
		for (int i = 0; i < n; i++, c++) {
			collection.add(Character.toString(c));
		}
	}

	public static void main(String[] args) throws Exception {
		List<String> cards = new ArrayList<>();
		fill(cards, 5);
		System.out.println("Cards: " + cards);

		Set<String> candidates = new HashSet<>(library);
		candidates.removeAll(cards);

		SimulationTask task = new SimulationTask(cards, candidates, 0, 0, true);
		ForkJoinPool pool = new ForkJoinPool(2);
		pool.submit(task);
		System.out.println("winner: " + task.get());

		pool.shutdown();
	}
}
