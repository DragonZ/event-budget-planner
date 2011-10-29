package budgeteventplanner.shared.dto.entity;

import java.util.UUID;

import budgeteventplanner.shared.dto.entity.Category;

import com.googlecode.objectify.annotation.Entity;

//		BudgetUser User = (new BudgetUser).Builder(......).setId(id).build();
@Entity
public class User
{
	private UUID UserID;
	private String email;
	private String name;
	private int role;
	private String password;
	private String company;
	private String address;
	
	
	/**************************************************************/
	public static class Builder
	{
		private User User;
		
		@Override
		public Builder(String email, int role, int password)
		{
			this.User.email= email;
			this.User.role = role;
			this.User.password = password;
			this.UserID = UUID.randomUUID();
		}
		/*---------------------------------------------------*/
		public Builder SetCompany (String company)
		{
			this.User.company = company;
			return this;
		}
		
		public Builder setAddress(String address)
		{
			this.User.address = address;
			return this;
		}
		
		public Builder name(String name)
		{
			this.User.name= name;
			return this;
		}
		/*---------------------------------------------------*/
		public User build() 
		{
			return this.User;
		}
		
		
	}
	/**************************************************************/
	
}
