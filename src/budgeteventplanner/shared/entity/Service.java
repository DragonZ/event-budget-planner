package budgeteventplanner.shared.dto.entity;

import java.util.UUID;
import budgeteventplanner.shared.dto.entity.Category;
import com.googlecode.objectify.annotation.Entity;

//		BudgetService Service = (new BudgetService).Builder(......).setId(id).build();
@Entity
public class Service
{
	private String name;
	private Category category;
	private Double price;
	
	
	/**************************************************************/
	public static class Builder
	{
		private Service Service;
		
		public Builder()
		{
		}
		/*---------------------------------------------------*/
		public Builder SetName (String name)
		{
			this.Service.name = name;
			return this;
		}
		
		public Builder setCategory (Category category)
		{
			this.Service.category = category;
			return this;
		}
		
		public Builder setPrice(Double price)
		{
			this.Service.price= price;
			return this;
		}
		/*---------------------------------------------------*/
		public Service build() 
		{
			return this.Service;
		}
	}
	/**************************************************************/
	public String name()
	{
		return name;
	}
	public Category category()
	{
		return category;
	}
	public Double price()
	{
		return price;
	}
}
