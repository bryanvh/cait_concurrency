package com.github.bryanvh.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class Player implements Comparable<Player> {
	private final String name;
	private int health;
	private int armor;
	private int ward;
	private int stun;
	private List<Card> hand;
	private int next; // index of card in hand to play next
	private final LinkedList<Effect> conditions;

	private final List<Card> backupHand;
	private final int backupHealth;

	public enum Opponent {
		LIAM;

		public Player load() throws IOException {
			return Player.load("/com/github/bryanvh/concurrency/config/"
					+ name() + ".json");
		}
	}

	@XmlRootElement()
	public static class CardGroup {
		private String name;
		private int count;

		@XmlTransient
		public void setName(String name) {
			this.name = name;
		}

		@XmlTransient
		public void setCount(int count) {
			this.count = count;
		}
	}

	@XmlRootElement()
	private static class Builder {
		private String name;
		private int health;
		private List<CardGroup> cardGroups;

		@XmlTransient
		public void setName(String name) {
			this.name = name;
		}

		@XmlTransient
		public void setHand(List<CardGroup> cardGroups) {
			this.cardGroups = cardGroups;
		}

		@XmlTransient
		public void setHealth(int health) {
			this.health = health;
		}

		public static Player load(InputStream is) throws IOException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
			Builder builder = mapper.readValue(is, Builder.class);

			List<Card> hand = new ArrayList<Card>();
			for (CardGroup cg : builder.cardGroups) {
				Card c = Library.CARDS.get(cg.name);
				for (int i = 0; i < cg.count; i++) {
					hand.add(c);
				}
			}
			return new Player(builder.name, builder.health, hand);
		}
	}

	private Player(String name, int health, List<Card> hand) {
		this.name = name;
		this.backupHand = hand;
		this.backupHealth = health;
		reset();
		this.conditions = new LinkedList<Effect>();

		if (health < 1) {
			throw new IllegalArgumentException("health must be > 0");
		}
		if (hand == null || hand.size() == 0) {
			throw new IllegalArgumentException("hand must have at least 1 card");
		}
	}

	public void reset() {
		this.hand = new ArrayList<Card>(backupHand);
		this.health = backupHealth;
		this.armor = 0;
		this.ward = 0;
		this.stun = 0;
		this.next = 0;
		Collections.shuffle(hand);
	}

	public static Player load(String path) throws IOException {
		return Builder.load(Class.class.getResourceAsStream(path));
	}

	public void act(Player opponent) {
		applyConditions(opponent);
		if (stun > 0) {
			stun--;
			return;
		}
		Card c = hand.get(next++);
		// System.out.println(name + " playing " + c);
		c.play(this, opponent);
	}

	private void applyConditions(Player opponent) {
		List<Effect> remove = new ArrayList<Effect>();
		for (Effect c : conditions) {
			// System.out.println(name + " triggering condition " + c);
			c.apply(this, this);
			if (c.isExpired()) {
				remove.add(c);
			}
		}
		conditions.removeAll(remove);
	}

	public boolean isDefeated() {
		return health <= 0 || next >= hand.size();
	}

	void addCondition(Effect c) {
		conditions.addLast(c);
	}

	void applyMelee(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("value must be >= 0");
		}

		int dmg = 0;
		if (armor > value) {
			armor -= value;
		} else if (armor <= value) {
			dmg = value - armor;
			armor = 0;
		}

		health -= (health > dmg) ? dmg : health;
	}

	void applyMagic(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("value must be >= 0");
		}

		int dmg = 0;
		if (ward > value) {
			ward -= value;
		} else if (ward <= value) {
			dmg = value - ward;
			ward = 0;
		}

		health -= (health > dmg) ? dmg : health;
	}

	void applyArmor(int value) {
		armor += value;
	}

	void applyWard(int value) {
		ward += value;
	}

	void applyStun(int value) {
		stun += value;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Player: " + name + " Health: " + health + " Armor: " + armor
				+ " Ward: " + ward + " Spirit: " + (hand.size() - next)
				+ " Conditions: " + conditions.size();
	}

	@Override
	public int compareTo(Player p) {
		return name.compareTo(p.name);
	}
}
