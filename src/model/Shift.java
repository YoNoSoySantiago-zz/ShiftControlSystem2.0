package model;
import java.io.Serializable;
import java.time.*;

@SuppressWarnings("serial")
public class Shift implements Serializable{
	//ATRUBUTES
	private Type type;
	private char letter;
	private int number;
	private String shift;
	private CurrentTime current;
	private boolean attended;
	private boolean active;
	
	//CONSTRUCTOR
	public Shift(LocalDateTime time,Type type,char letter,int number, String shift,boolean attended,boolean active) {
		setCurrent(new CurrentTime(time));
		this.setType(type);
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the current
	 */
	public CurrentTime getCurrent() {
		return current;
	}

	/**
	 * @param current the current to set
	 */
	public void setCurrent(CurrentTime current) {
		this.current = current;
	}

	/**
	 * @return the ban
	 */
}
