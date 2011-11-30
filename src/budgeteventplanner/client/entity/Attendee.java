package budgeteventplanner.client.entity;

import java.io.Serializable;

import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class Attendee implements Serializable {
	@Id
	private String attendeeId;
	private String eventId;
	private String name;
	private String email;
	private String address;
	private String jobTitle;
	private String companyName;
	private String phoneNumber;
	private Integer status;
	
	public final static Integer YES = 0;
	public final static Integer NO = 1;
	public final static Integer MAYBE = 2;

	public Attendee() {
	}

	public static class Builder {
		private Attendee attendee;

		public Builder(Attendee attendee) {
			this.attendee = attendee;
		}
		
		public Builder(Attendee attendee, String eventId) 
		{
			this.attendee = attendee;
			this.attendee.eventId = eventId;
			this.attendee.attendeeId = UUID.randomUUID();
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

		public Builder setEmail(String email) {
			this.attendee.email = email;
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

		public Builder setPhoneNumber(String phoneNumber) {
			this.attendee.phoneNumber = phoneNumber;
			return this;
		}

		public Builder setStatus(Integer status) {
			this.attendee.status = status;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public Integer getStatus() {
		return status;
	}

	public String toString() {
		String result = "Name: " + name + "\nEmail: " + email + "\nAddress: "
				+ address + "\nJob Title: " + jobTitle + "\nCompany Name: "
				+ companyName + "\nPhone Number: " + phoneNumber;
		return result;
	}
}