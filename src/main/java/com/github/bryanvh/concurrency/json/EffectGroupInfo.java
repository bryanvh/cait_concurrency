package com.github.bryanvh.concurrency.json;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.github.bryanvh.concurrency.ConditionFactory;
import com.github.bryanvh.concurrency.Effect;
import com.github.bryanvh.concurrency.Effects;

@XmlRootElement()
public class EffectGroupInfo {
	public List<Effect> getEffects() {
		return effects;
	}

	private List<Effect> effects = new ArrayList<Effect>();

	@XmlTransient
	public void setMelee(int i) {
		effects.add(Effects.createMelee(i));
	}

	@XmlTransient
	public void setMagic(int i) {
		effects.add(Effects.createMagic(i));
	}

	@XmlTransient
	public void setArmor(int i) {
		effects.add(Effects.createArmor(i));
	}

	@XmlTransient
	public void setWard(int i) {
		effects.add(Effects.createWard(i));
	}

	@XmlTransient
	public void setStun(int i) {
		effects.add(Effects.createStun(i));
	}

	@XmlTransient
	public void setBane(ConditionFactory c) {
		effects.add(Effects.createBane(c));
	}

	@XmlTransient
	public void setAura(ConditionFactory c) {
		effects.add(Effects.createAura(c));
	}
}
