package edu.wustl.cait.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

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

	public static interface Registerable {
		public Set<SwapResult> getResults();
	}

	public final static Set<Registerable> REGISTERED = new HashSet<>();

	public static void register(Registerable r) {
		if (r.getResults() == null) {
			System.err.println("Unsafe publication!");
		}
		REGISTERED.add(r);
	}

	public static void verifySafePublication(Class<? extends Registerable> c) {
		
		if (REGISTERED.size() != 2) {
			System.err.println("error: must register 2 Registerables!");
			return;
		}
		if (c.getConstructors().length != 0) {
			System.err.println("error: no public constructors allowed!");
			return;
		}
		if (c.getFields().length > 0) {
			System.err.println("error: no public fields allowed!");
			return;
		}
		for (Registerable as : REGISTERED) {
			Set<SwapResult> set = as.getResults();
			try {
				set.clear();
			} catch (UnsupportedOperationException uoe) {
				continue;
			}
			System.err.println("error: unsafe publication from getResults()!");
		}

		System.out.println("Congratulations on safe publication!");
	}

	public static final String CONFIG_ROOT = "/META-INF/config/";

	public static <T> T loadFromJson(Class<T> type, String path) {
		InputStream is = Util.class.getResourceAsStream(CONFIG_ROOT + path);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
		T result = null;
		try {
			result = mapper.readValue(is, type);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
