package edu.wustl.cait.concurrency.labs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.cait.concurrency.Card;

public class Part3Lab5Solution {
	private static class ImmutablePlayer {
		// TODO: make all fields are private and final
		private final String name;
		private final int maxHealth;
		private final List<Card> hand;
		private final Set<Card> cardSet;

		// TODO: make sure class can't be extended
		private ImmutablePlayer(String name, int maxHealth, Set<Card> cardSet) {
			this.name = name;
			this.maxHealth = maxHealth;
			this.cardSet = cardSet; // no need for defensive copy here

			List<Card> newHand = new ArrayList<>();
			for (Card c : this.cardSet) {
				for (int i = 0; i < 5; i++) {
					newHand.add(c);
				}
			}
			Collections.shuffle(newHand);
			this.hand = newHand;
		}

		public static ImmutablePlayer load(String json) {
			Set<Card> cardSet = new HashSet<>();
			return new ImmutablePlayer("Sample", 100, cardSet);
		}

		// TODO: create a new immutable object rather than mutate current one
		public ImmutablePlayer swap(Card oldCard, Card newCard) {
			Set<Card> newCardSet = new HashSet<>(cardSet);
			if (oldCard != newCard) {
				newCardSet.remove(oldCard);
				newCardSet.add(newCard);
			}
			return new ImmutablePlayer(name, maxHealth, newCardSet);
		}

		// TODO: return an unmodifiable set
		public Set<Card> getCardSet() {
			return Collections.unmodifiableSet(cardSet);
		}
	}

	public static void main(String[] args) {
		ImmutablePlayer p = ImmutablePlayer.load("");
		p.swap(null, null);
		p.getCardSet();
	}
}
