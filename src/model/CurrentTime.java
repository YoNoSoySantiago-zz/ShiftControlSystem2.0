package model;
import java.io.Serializable;
import java.time.LocalDateTime;
@SuppressWarnings("serial")
public class CurrentTime implements Serializable{
	private LocalDateTime nowTime = LocalDateTime.now();
	private int[] adelanted = new int[6];
	private LocalDateTime shiftTime;
	
	public CurrentTime(LocalDateTime time) {
		setShiftTime(time);
	}

	public LocalDateTime getShiftTime() {
		return shiftTime;
	}

	public void setShiftTime(LocalDateTime shiftTime) {
		this.shiftTime = shiftTime;
	}

	public LocalDateTime getNowTime() {
		return nowTime;
	}

	/**
	 * @return the adelanted
	 */
	public int[] getAdelanted() {
		return adelanted;
	}

	/**
	 * @param adelanted the adelanted to set
	 */
	public void setAdelanted(int[] adelanted) {
		this.adelanted = adelanted;
	}

	
	
}
