package com.github.bryanvh.concurrency.json;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

@XmlRootElement()
public class PlayerInfo {
	private String name;
	private int health;
	private List<HandInfo> hand;

	public String getName() {
		return name;
	}

	@XmlTransient
	public void setName(String name) {
		this.name = name;
	}

	public List<HandInfo> getHand() {
		return hand;
	}

	@XmlTransient
	public void setHand(List<HandInfo> hand) {
		this.hand = hand;
	}

	public int getHealth() {
		return health;
	}

	@XmlTransient
	public void setHealth(int health) {
		this.health = health;
	}

	public static void main(String[] args) throws Exception {
		PlayerInfo pi = new PlayerInfo();
		pi.setName("Bryan");
		HandInfo hi = new HandInfo();
		hi.setName("card1");
		hi.setCount(5);
		List<HandInfo> list = new ArrayList<HandInfo>();
		list.add(hi);
		pi.setHand(list);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());

		// Printing JSON
		String result = mapper.writeValueAsString(pi);
		System.out.println(result);

		PlayerInfo retr = mapper.readValue(result, PlayerInfo.class);

		System.out.println("Name  : " + retr.getName());
		System.out.println("Health: " + retr.getHealth());
		System.out.println("Hand: " + retr.getHand());
	}
}
