package edu.wustl.cait.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement()
public class Player implements Comparable<Player> {
	private final String name;
	private final List<Card> hand;
	private int health;
	private int armor;
	private int ward;
	private int stun;
	private int next; // index of card in hand to play next
	private LinkedList<Effect> conditions;

	private final Set<Card> cardSet;
	private final int origHealth;

	public enum Opponent {
		LIAM;

		public Player load() {
			return Player.load(name() + ".json");
		}
	}

	@XmlRootElement()
	public static class CardGroup {
		private final String name;

		// TODO: may allow "count" field later

		@JsonCreator
		private CardGroup(@JsonProperty("name") String name) {
			this.name = name;
		}
	}

	@JsonCreator
	private static Player createPlayer(@JsonProperty("name") String name,
			@JsonProperty("health") int health,
			@JsonProperty("isEnemy") boolean isEnemy,
			@JsonProperty("hand") Set<CardGroup> cardGroups) {
		Set<Card> cardSet = new HashSet<Card>();
		Library lib = (isEnemy) ? Library.ENEMY_LIB : Library.PLAYER_LIB;
		cardGroups.forEach(cg -> cardSet.add(lib.getCard(cg.name)));
		return new Player(name, health, cardSet);
	}

	private Player(String name, int health, Set<Card> cardSet) {
		this.name = name;
		this.cardSet = cardSet;
		this.origHealth = health;
		this.hand = new ArrayList<Card>();
		for (Card c : cardSet) {
			for (int i = 0; i < 5; i++) {
				hand.add(c);
			}
		}
		reset();

		if (health < 1) {
			throw new IllegalArgumentException("health must be > 0");
		}
		if (hand == null || hand.size() == 0) {
			throw new IllegalArgumentException("hand must have at least 1 card");
		}
	}

	public Player swap(Card oldCard, Card newCard) {
		Set<Card> newCardSet = new HashSet<Card>(cardSet);
		newCardSet.remove(oldCard);
		newCardSet.add(newCard);

		return new Player(name, origHealth, newCardSet);
	}

	public Set<Card> getCardSet() {
		return Collections.unmodifiableSet(cardSet);
	}

	/**
	 * We could have used the Memento pattern here, but that would have been
	 * overkill since we only need to restore the Player to its original state
	 * (not arbitrary states).
	 */
	void reset() {
		this.health = origHealth;
		this.conditions = new LinkedList<Effect>();
		this.armor = 0;
		this.ward = 0;
		this.stun = 0;
		this.next = 0;
		Collections.shuffle(hand);
	}

	public static Player load(String path) {
		return Util.loadFromJson(Player.class, path);
	}

	void act(Player opponent) {
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
		for (Effect c : conditions) {
			if (c.isExpired()) {
				// continue
			} else {
				c.apply(this, this);
			}
		}
	}

	boolean isDefeated() {
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
