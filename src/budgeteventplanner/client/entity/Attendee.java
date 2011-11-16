package budgeteventplanner.client.entity;


import budgeteventplanner.shared.UUID;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Attendee = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Attendee {
	private String attendeeID;
	private String name;
	private String email;
	private String address;
	private String jobtitle;
	private String companyName;

	public static class Builder {
		private Attendee attendee = new Attendee();

		public Builder(Attendee attendee) {
			this.attendee = attendee;
		}

		public Builder(String name, String email) {
			this.attendee.name = name;
			this.attendee.email = email;
			this.attendee.attendeeID = UUID.randomUUID();
		}

		public Builder setName(String name) {
			this.attendee.name = name;
			return this;
		}
		
		public Builder setAddress(String address) {
			this.attendee.address = address;
			return this;
		}
		
		public Builder setJobtitle(String jobtitle) {
			this.attendee.jobtitle = jobtitle;
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

	public String getAttendeeID() {
		return attendeeID;
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
}