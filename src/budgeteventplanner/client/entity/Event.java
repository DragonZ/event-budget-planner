package budgeteventplanner.client.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class Event implements Serializable {
	@Id
	private String eventId;
	private String organizerId;
	private String name;
	private Date startTime;
	private Date endTime;
	private Address location;
	private Integer status;

	public static final Integer ACTIVE = 0;
	public static final Integer INACTIVE = 1;
	public static final Integer TRASHED = 2;

	public Event() {}

	public static class Builder {
		private Event event;

		public Builder(Event event) {
			this.event = event;
		}

		public Builder(String organizerId, String name, Integer status) {
			this.event = new Event();
			this.event.eventId = UUID.randomUUID();
			this.event.organizerId = organizerId;
			this.event.name = name;
			this.event.status = INACTIVE;
		}

		public Builder setName(String name) {
			this.event.name = name;
			return this;
		}

		public Builder setStartTime(Date startTime) {
			this.event.startTime = startTime;
			return this;
		}

		public Builder setEndTime(Date endTime) {
			this.event.endTime = endTime;
			return this;
		}

		public Builder setLocation(Address location) {
			this.event.location = location;
			return this;
		}

		public Builder setStatus(Integer status) {
			this.event.status = status;
			return this;
		}

		public Event build() {
			return this.event;
		}
	}

	public String getEventId() {
		return eventId;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public String getName() {
		return name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public Address getLocation() {
		return location;
	}

	public Integer getStatus() {
		return status;
	}
	
	
}