package model;

public class Shift {
	//ATRUBUTES
	private char letter;
	private int number;
	private String shift;
	private boolean attended;
	private boolean active;
	
	//CONSTRUCTOR
	public Shift(char letter,int number, String shift,boolean attended,boolean active) {
		this.letter = letter;
		this.number = number;
		this.shift = shift;
		this.attended = attended;
		this.active = active;
	}
	
	//GETTERS AND SETTERS
	public boolean isAttended() {
		return attended;
	}
	public void setAttended(boolean attended) {
		this.attended = attended;
	}
	public String getShift() {
		 return this.shift;
	 }
	 public char getLetter() {
		return letter;
	}
	public void setLetter(char letter) {
		this.letter = letter;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public void setShift(String shift) {
		 this.shift = shift;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
