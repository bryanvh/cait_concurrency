package edu.wustl.cait.concurrency;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement()
public class EffectGroup {
	private List<Effect> effects = new ArrayList<Effect>();

	public List<Effect> getEffects() {
		return effects;
	}

	@XmlTransient
	public void setMelee(int i) {
		effects.add(Effect.createMelee(i));
	}

	@XmlTransient
	public void setMagic(int i) {
		effects.add(Effect.createMagic(i));
	}

	@XmlTransient
	public void setArmor(int i) {
		effects.add(Effect.createArmor(i));
	}

	@XmlTransient
	public void setWard(int i) {
		effects.add(Effect.createWard(i));
	}

	@XmlTransient
	public void setStun(int i) {
		effects.add(Effect.createStun(i));
	}

	@XmlTransient
	public void setBane(Condition c) {
		effects.add(Effect.createBane(c));
	}

	@XmlTransient
	public void setAura(Condition c) {
		effects.add(Effect.createAura(c));
	}
}
