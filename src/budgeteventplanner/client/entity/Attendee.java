package budgeteventplanner.client.entity;


import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class Attendee {
	@Id
	private String attendeeId;
	private String eventId;
	private String name;
	private String email;
	private String address;
	private String jobTitle;
	private String companyName;

	public static class Builder {
		private Attendee attendee;

		public Builder(Attendee attendee) {
			this.attendee = attendee;
		}

		public Builder(String eventId, String name, String email) {
			this.attendee = new Attendee();
			this.attendee.attendeeId = UUID.randomUUID();
			this.attendee.eventId = eventId;
			this.attendee.name = name;
			this.attendee.email = email;
		}

		public Builder setName(String name) {
			this.attendee.name = name;
			return this;
		}
		
		public Builder setAddress(String address) {
			this.attendee.address = address;
			return this;
		}
		
		public Builder setJobTitle(String jobTitle) {
			this.attendee.jobTitle = jobTitle;
			return this;
		}
		
		public Builder setCompanyName(String companyName) {
			this.attendee.companyName = companyName;
			return this;
		}
		
		public Attendee build() {
			return this.attendee;
		}
	}

	public String getAttendeeId() {
		return attendeeId;
	}

	public String getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getCompanyName() {
		return companyName;
	}
}