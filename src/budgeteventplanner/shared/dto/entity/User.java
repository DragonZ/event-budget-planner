package budgeteventplanner.shared.dto.entity;

import java.util.UUID;

import budgeteventplanner.shared.dto.entity.Category;

import com.googlecode.objectify.annotation.Entity;

//		BudgetUser User = (new BudgetUser).Builder(......).setId(id).build();
@Entity
public class User
{
	private UUID userID;
	private String email;
	private String name;
	private int role;
	private String password;
	private String company;
	private String address;
	
	
	/**************************************************************/
	public static class Builder
	{
		private User user;
		
		@Override
		public Builder(String email, int role, String password)
		{
			this.user.email= email;
			this.user.role = role;
			this.user.password = password;
			this.user.userID = UUID.randomUUID();
		}
		/*---------------------------------------------------*/
		public Builder SetCompany (String company)
		{
			this.user.company = company;
			return this;
		}
		
		public Builder setAddress(String address)
		{
			this.user.address = address;
			return this;
		}
		
		public Builder name(String name)
		{
			this.user.name= name;
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
