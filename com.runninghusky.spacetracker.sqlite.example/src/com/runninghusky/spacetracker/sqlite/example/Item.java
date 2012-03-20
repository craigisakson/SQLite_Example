package com.runninghusky.spacetracker.sqlite.example;

import java.io.Serializable;

/**
 * The Class Item.
 */
public class Item implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Id. */
	private long Id;
	
	/** The data. */
	private String data;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return Id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		Id = id;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(String data) {
		this.data = data;
	}
}
