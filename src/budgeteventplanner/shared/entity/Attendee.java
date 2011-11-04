package budgeteventplanner.shared.dto.entity;

import java.util.UUID;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Attendee = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Attendee
{
	private UUID attendeeID;
	private String name;
	private String email;
	private Address address;
	
	
	/**************************************************************/
	public static class Builder
	{
		private Attendee attendee;
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
		
		public Builder(String name, String email)
		{
			this.attendee.name = name;
			this.attendee.email= email;
			this.attendee.attendeeID= UUID.randomUUID();
		}
		/*---------------------------------------------------*/
		public Builder SetName (String name)
		{
			this.attendee.name = name;
			return this;
		}
		

		/*---------------------------------------------------*/
		public Attendee build() 
		{
			return this.attendee;
		}
	}
	/**************************************************************/
	public UUID getAttendeeID()
	{
		return attendeeID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	
	public Address getAddress()
	{
		return address;
	}
}
