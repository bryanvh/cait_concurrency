package com.github.bryanvh.concurrency.json;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

//{name : "card1", count : 5}

@XmlRootElement()
public class HandInfo {
	private String name;
	private int count;

	public String getName() {
		return name;
	}

	@XmlTransient
	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	@XmlTransient
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "{name: " + name + ", count: " + count + "}";
	}

}
