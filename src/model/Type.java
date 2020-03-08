package model;

public class Type {
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
