package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Type implements Serializable{
	private String name;
	private double duration;
	public  Type(String name,double duration) {
		this.name = name;
		this.duration = duration;
	}
	public String getName() {
		return name;
	}
	public double getDuration() {
		return duration;
	}
	
}
