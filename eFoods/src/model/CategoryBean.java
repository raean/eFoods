package model;

import java.sql.Blob;

public class CategoryBean {

	private String description;
	private String name;
	private String picture;

	private int id; // SQL Key

	public CategoryBean() {
	}

	@Override
	public String toString() {
		return "CategoryBean [description=" + description + ", name=" + name + ", id=" + id + "]";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
