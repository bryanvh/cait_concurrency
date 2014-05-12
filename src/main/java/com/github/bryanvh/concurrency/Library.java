package com.github.bryanvh.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class Library {
	private static final String LIB_CONFIG = "/com/github/bryanvh/concurrency/config/library.json";

	private static final Map<String, Card> CARDS;
	static {
		Builder builder = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
			InputStream is = Class.class.getResourceAsStream(LIB_CONFIG);
			builder = mapper.readValue(is, Builder.class);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		CARDS = builder.map;
	}
	
	public static Set<Card> getUnusedCards(Player p) {
		Set<Card> oldCards = p.getCardSet();
		Set<Card> newCards = new HashSet<Card>(CARDS.values());
		newCards.removeAll(oldCards);
		return newCards;
	}
	
	public static Card getCard(String name) {
		return CARDS.get(name);
	}

	@XmlRootElement()
	private static class Builder {
		Map<String, Card> map = new HashMap<String, Card>();

		@XmlTransient
		public void setCards(List<Card> list) {
			for (Card c : list) {
				map.put(c.getName(), c);
			}
		}
	}
}
