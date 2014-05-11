package com.github.bryanvh.concurrency;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.github.bryanvh.concurrency.json.EffectGroupInfo;

@XmlRootElement()
public class Condition {
	private List<Effect> effects;
	private String name;
	private int durationStart;

	public Effect create() {
		return new Effect() {
			private int duration = durationStart;

			@Override
			public void apply(Player actor, Player target) {
				if (duration <= 0) {
					throw new IllegalStateException("condition duration <= 0");
				}
				for (Effect e : effects) {
					e.apply(actor, target);
				}
				duration--;
			}

			@Override
			public boolean isExpired() {
				return duration == 0;
			}

			@Override
			public String toString() {
				return name + "/" + duration;
			}
		};
	}

	@XmlTransient
	public void setDuration(int duration) {
		this.durationStart = duration;
	}

	@XmlTransient
	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public void setEffects(EffectGroupInfo effects) {
		this.effects = effects.getEffects();
	}

	@Override
	public String toString() {
		return name;
	}
}
