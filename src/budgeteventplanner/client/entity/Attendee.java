package budgeteventplanner.client.entity;

import java.util.UUID;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Attendee = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Attendee
{
	private UUID attendeeID;
	private String firstName;
	private String lastName;
	private String middleName;
	private String email;
	private Address address;
	
	private String jobTitle;
	private String company;
	
	
	
	/**************************************************************/
	public static class Builder
	{
		private Attendee attendee = new Attendee();
		/*------------------------------------------------------*/
		public Builder(Attendee attendee) {
			try {
				this.attendee = (Attendee) attendee.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*------------------------------------------------------*/
		
		public Builder(String firstName, String lastName, String email)
		{
			this.attendee.firstName = firstName;
			this.attendee.lastName = lastName;
			this.attendee.email= email;
			this.attendee.attendeeID= UUID.randomUUID();
		}
		/*---------------------------------------------------*/
		public Builder SetFirstName (String name)
		{
			this.attendee.firstName = name;
			return this;
		}
		/*---------------------------------------------------*/
		public Builder SetMiddleName (String name)
		{
			this.attendee.middleName = name;
			return this;
		}
		/*---------------------------------------------------*/
		public Builder SetLastName (String name)
		{
			this.attendee.lastName = name;
			return this;
		}
		/*---------------------------------------------------*/
		public Builder SetJobTitle (String jobTitle)
		{
			this.attendee.jobTitle = jobTitle;
			return this;
		}
		/*---------------------------------------------------*/
		public Attendee build() 
		{
			return this.attendee;
		}
	}
	/**************************************************************/


	
	
	public String toString()
	{
		String temp = ""+firstName+" "+middleName+" "+lastName+"\n"+email+" \n"+jobTitle+" "+company+"\n" + address.toString()+"\n";
		return temp;
	}
	public UUID getAttendeeID() {
		return attendeeID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getEmail() {
		return email;
	}

	public Address getAddress() {
		return address;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getCompany() {
		return company;
	}

}
