package com.github.bryanvh.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement()
public class Card {
	protected String name;
	protected List<Effect> effects;

	@XmlRootElement()
	private static class EffectGroup {
		private List<Effect> effects = new ArrayList<Effect>();

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

	@XmlTransient
	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public void setEffects(EffectGroup effectGroup) {
		this.effects = effectGroup.effects;
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
