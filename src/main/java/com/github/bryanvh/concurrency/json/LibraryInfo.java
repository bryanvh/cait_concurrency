package com.github.bryanvh.concurrency.json;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.github.bryanvh.concurrency.Card;

@XmlRootElement()
public class LibraryInfo {
	private List<Card> cards = null;

	public List<Card> getList() {
		return cards;
	}

	@XmlTransient
	public void setCards(List<Card> list) {
		this.cards = list;
	}

}
