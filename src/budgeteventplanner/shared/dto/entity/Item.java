package budgeteventplanner.shared.dto.entity;

import java.util.UUID;

import budgeteventplanner.shared.dto.nonentity.Category;

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
		
		@Override
		public Builder(String name)
		{
			this.item.name = name;
			this.itemID = UUID.randomUUID();
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
	
}
