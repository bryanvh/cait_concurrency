package com.github.bryanvh.concurrency;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.github.bryanvh.concurrency.json.EffectGroupInfo;

@XmlRootElement()
public class Card {
	protected String name;
	protected List<Effect> effects;

	@XmlTransient
	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public void setEffects(EffectGroupInfo effects) {
		this.effects = effects.getEffects();
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
		return name + "/" + effects;
	}
}
