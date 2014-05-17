package com.github.bryanvh.concurrency;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement()
public class Card {
	private final String name;
	private final List<Effect> effects;

	@JsonCreator
	private static Card createCard(@JsonProperty("name") String name,
			@JsonProperty("effects") EffectGroup eg) {
		return new Card(name, eg.getEffects());
	}

	private Card(String name, List<Effect> effects) {
		this.name = name;
		this.effects = effects;
	}

	public List<Effect> getEffects() {
		return Collections.unmodifiableList(effects);
	}

	public void play(Player actor, Player target) {
		for (Effect e : effects) {
			e.apply(actor, target);
		}
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
