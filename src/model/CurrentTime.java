package model;
import java.time.*;
public class CurrentTime {
	private LocalTime nowTime = LocalTime.now();
	private LocalDate nowDate = LocalDate.now();
	private LocalTime shiftTime;
	private LocalDate shiftDate;
	
	public CurrentTime(LocalDate date,LocalTime time) {
		shiftDate = date;
		shiftTime = time;
	}

	public void setShiftTime(LocalTime shiftTime) {
		this.shiftTime = shiftTime;
	}

	public void setShiftDate(LocalDate shiftDate) {
		this.shiftDate = shiftDate;
	}

	public LocalTime getNowTime() {
		return nowTime;
	}

	public LocalDate getNowDate() {
		return nowDate;
	}

	public LocalTime getShiftTime() {
		return shiftTime;
	}

	public LocalDate getShiftDate() {
		return shiftDate;
	}
	
}
