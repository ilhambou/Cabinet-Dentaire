package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data @NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarObj {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	   private String title;
	   private int startHour;
	   private int startMin;
	   private int endHour;
	   private int endMin;
	   private int duration;
	   
	   private String startEnd;
	private String dayOfWeek; // New attribute for day of the week


	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}


	public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getStartHour() {
			return startHour;
		}
		public void setStartHour(int startHour) {
			this.startHour = startHour;
		}
		public int getStartMin() {
			return startMin;
		}
		public void setStartMin(int startMin) {
			this.startMin = startMin;
		}
		public int getEndHour() {
			return endHour;
		}
		public void setEndHour(int endHour) {
			this.endHour = endHour;
		}
		public int getEndMin() {
			return endMin;
		}
		public void setEndMin(int endMin) {
			this.endMin = endMin;
		}
		public int getDuration() {
			return duration;
		}
		public void setDuration(int duration) {
			this.duration = duration;
		}
		public String getStartEnd() {
			return startEnd;
		}
		public void setStartEnd(String startEnd) {
			this.startEnd = startEnd;
		}



	private Date startDateTime;
	private Date endDateTime;

	// other methods

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime=endDateTime;
	}
	@Column(name = "start_time")
	private LocalDateTime startTime;

	@Column(name = "end_time")
	private LocalDateTime endTime;
}
