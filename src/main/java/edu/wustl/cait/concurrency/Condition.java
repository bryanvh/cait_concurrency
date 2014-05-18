package edu.wustl.cait.concurrency;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement()
public class Condition {
	private final List<Effect> effects;
	private final String name;
	private final int initialDuration;

	@JsonCreator
	private static Condition createCondition(@JsonProperty("name") String name,
			@JsonProperty("effects") EffectGroup eg,
			@JsonProperty("duration") int initialDuration) {
		return new Condition(name, eg.getEffects(), initialDuration);
	}

	private Condition(String name, List<Effect> effects, int initialDuration) {
		this.name = name;
		this.effects = effects;
		this.initialDuration = initialDuration;
	}

	public Effect create() {
		return new Effect() {
			private int duration = initialDuration;

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

	@Override
	public String toString() {
		return name;
	}
}
