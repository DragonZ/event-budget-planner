package budgeteventplanner.shared.dto.entity;

import java.util.UUID;
import budgeteventplanner.shared.dto.entity.Category;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Address = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Address
{

	private String firstName;
	private String lastName;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	private String country;

	
	
	/**************************************************************/
	public static class Builder
	{
		private Address address;
		/*------------------------------------------------------*/
		public Builder(Address address) {
			this.address = (Address) address.clone();
		}
		/*------------------------------------------------------*/
		@Override
		public Builder(String firstName, String lastName)
		{
			this.address.firstName = firstName;
			this.address.lastName= lastName;
		}
		/*---------------------------------------------------*/
		public Builder SetStreet (String street)
		{
			this.address.street = street;
			return this;
		}
		
		public Builder setCity (String city)
		{
			this.address.city = city;
			return this;
		}
		
		public Builder setState (String state)
		{
			this.address.state = state;
			return this;
		}
		
		public Builder setZipcode (String zipcode)
		{
			this.address.zipcode = zipcode;
			return this;
		}
		public Builder setCountry (String country)
		{
			this.address.country = country;
			return this;
		}
		/*---------------------------------------------------*/
		public Address build() 
		{
			return this.address;
		}
	}
	/**************************************************************/
	public static boolean validate()
	{
		return false;
	}
	public static String toString()
	{
		return firstName+" "+lastName+" "+street+" "+city+" "+state+" "+zipcode+" "+country;
	}
	
	
	
}
