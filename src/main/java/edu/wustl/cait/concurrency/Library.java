package edu.wustl.cait.concurrency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

public class Library {
	static final Library PLAYER_LIB;
	static final Library ENEMY_LIB;
	private final Map<String, Card> cards;

	static {
		PLAYER_LIB = Util.loadFromJson(Builder.class, "library.json").build();
		ENEMY_LIB = Util.loadFromJson(Builder.class, "library_enemy.json")
				.build();
	}

	private Library(Map<String, Card> cards) {
		this.cards = cards;
	}

	/**
	 * Public method that returns a new set containing all cards in the player
	 * library of cards. None of the enemy cards are included.
	 */
	public static Set<Card> getCards() {
		return new HashSet<Card>(PLAYER_LIB.cards.values());
	}

	/**
	 * Package-protected method used when building players.
	 */
	Card getCard(String name) {
		return cards.get(name);
	}

	@XmlRootElement()
	private static class Builder {
		private Map<String, Card> map = new HashMap<String, Card>();

		@XmlTransient
		public void setCards(List<Card> list) {
			list.forEach(c -> map.put(c.getName(), c));
		}

		public Library build() {
			return new Library(map);
		}
	}
}
