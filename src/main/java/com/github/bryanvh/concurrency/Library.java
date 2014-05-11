package com.github.bryanvh.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.github.bryanvh.concurrency.json.LibraryInfo;

public class Library {
	static public final Map<String, Card> CARDS = load();
	private static final String CONFIG_ROOT = "/com/github/bryanvh/concurrency/config";
	private static final String LIB_CONFIG = CONFIG_ROOT + "/library.json";

	public enum Opponent {
		LIAM;

		public Player load() throws IOException {
			InputStream is = Class.class.getResourceAsStream(CONFIG_ROOT + "/"
					+ name() + ".json");
			return Player.load(is);
		}
	}

	private static Map<String, Card> load() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());

		InputStream is = null;
		LibraryInfo li = null;
		try {
			is = Class.class.getResourceAsStream(LIB_CONFIG);
			li = mapper.readValue(is, LibraryInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Card> map = new HashMap<String, Card>();
		for (Card c : li.getList()) {
			map.put(c.getName(), c);
		}

		return map;
	}
}
