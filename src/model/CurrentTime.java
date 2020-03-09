package model;
import java.time.*;
public class CurrentTime {
	private LocalDateTime nowTime = LocalDateTime.now();
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
	
}
