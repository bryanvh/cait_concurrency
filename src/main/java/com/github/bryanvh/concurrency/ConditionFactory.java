package com.github.bryanvh.concurrency;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.github.bryanvh.concurrency.json.EffectGroupInfo;

@XmlRootElement()
public class ConditionFactory {
	private List<Effect> effects;
	private String name;
	private int duration;

	protected ConditionFactory() {
	}

	public Condition create() {
		return new Condition(effects, name, duration);
	}

	@XmlTransient
	public void setDuration(int duration) {
		this.duration = duration;
	}

	@XmlTransient
	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public void setEffects(EffectGroupInfo effects) {
		this.effects = effects.getEffects();
	}

	public String getName() {
		return name;
	}
}
