package budgeteventplanner.shared.dto.entity;

import java.util.UUID;
import budgeteventplanner.shared.dto.entity.Category;
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
			this.attendee = (Attendee) attendee.clone();
		}
		/*------------------------------------------------------*/
		@Override
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
		
		public Builder setCategory (Category category)
		{
			this.attendee.category = category;
			return this;
		}
		
		public Builder setPrice(Double price)
		{
			this.attendee.price= price;
			return this;
		}
		/*---------------------------------------------------*/
		public Attendee build() 
		{
			return this.attendee;
		}
		
		
	}
	/**************************************************************/
	
}
