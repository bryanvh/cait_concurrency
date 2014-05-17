package com.github.bryanvh.concurrency;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

public interface Util {
	@SafeVarargs
	public static <T> void splitSet(Set<T> source, Set<T>... args) {
		int i = 0;
		for (T t : source) {
			args[i % args.length].add(t);
			i++;
		}
	}

	@SafeVarargs
	public static <T> T getMax(Comparator<? super T> c, Set<T>... args) {
		Stream<Set<T>> stream = Stream.of(args);
		return stream.flatMap(a -> a.stream()).max(c).orElse(null);
	}

	public static <T> void register(T t) {
		// do nothing, just for illustration
	}
	
	public static <T> int tabulate(T t) {
		// do nothing useful, just for illustration
		return 0;
	}
}
