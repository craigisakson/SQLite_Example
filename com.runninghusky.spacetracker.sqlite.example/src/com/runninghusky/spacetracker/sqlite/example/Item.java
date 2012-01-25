package com.runninghusky.spacetracker.sqlite.example;

import java.io.Serializable;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	private long Id;
	private String data;

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
