package budgeteventplanner.shared.dto.entity;

import java.util.UUID;

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
		/*------------------------------------------------------*/
		public Builder(User user) {
			try {
				this.user = (User) user.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*------------------------------------------------------*/

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
			return this.user;
		}
	}
	/**************************************************************/
	public UUID userID()
	{
		return userID;
	}
	public String email()
	{
		return email;
	}
	public String name()
	{
		return name;
	}
	public int role()
	{
		return role;
	}
	public String password()
	{
		return password;
	}
	public String company()
	{
		return company;
	}
	public String address()
	{
		return address;
	}
	
}
