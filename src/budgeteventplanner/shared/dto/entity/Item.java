package budgeteventplanner.shared.dto.entity;

import java.util.Date;
import java.util.UUID;

import budgeteventplanner.shared.dto.entity.Category;

import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = (new BudgetItem).Builder(......).setId(id).build();
@Entity
public class Item
{
	private UUID itemID;
	private Category category;
	private String name;
	private Date dueDate;
	private Vendor vendor;

	
	
	/**************************************************************/
	public static class Builder
	{
		private Item item;
		/*------------------------------------------------------*/
		public Builder(Item item) {
			try {
				this.item = (Item) item.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*------------------------------------------------------*/

		public Builder(String name)
		{
			this.item.name = name;
			this.item.itemID = UUID.randomUUID();
		}
		/*---------------------------------------------------*/
		public Builder setVendor(Vendor vendor)
		{
			this.item.vendor = vendor;
			return this;
		}
		
		public Builder setdueDate(Date date)
		{
			this.item.dueDate = date;
			return this;
		}
		
		public Builder setCategory(Category category)
		{
			this.item.category = category;
			return this;
		}
		/*---------------------------------------------------*/
		public Item build() 
		{
			return this.item;
		}
	}
	/**************************************************************/
	public UUID itemID()
	{
		return itemID;
	}
	public Category category()
	{
		return category;
	}
	public String name()
	{
		return name;
	}
	public Date dueDate()
	{
		return dueDate;
	}
	public Vendor vendor()
	{
		return vendor;
	}
}
