package budgeteventplanner.client.entity;
import java.util.UUID;

import budgeteventplanner.client.entity.Category;

import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = (new BudgetItem).Builder(......).setId(id).build();
@Entity
public class BudgetItem
{
	private UUID budgetId;
	private Category category;
	private String name;
	private double limit;
	
	
	/**************************************************************/
	public static class Builder
	{
		private BudgetItem budgetItem = new BudgetItem();
		/*------------------------------------------------------*/
		public Builder(BudgetItem budgetItem) {
			try {
				this.budgetItem = (BudgetItem) budgetItem.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*------------------------------------------------------*/

		public Builder(String name)
		{
			this.budgetItem.name= name;
		}
		

		
		public Builder setLimit(double limit)
		{
			this.budgetItem.limit = limit;
			return this;
		}
		//
		public BudgetItem build() 
		{
			return this.budgetItem;
		}
	}
	/**************** Getter *************************/


	public UUID getBudgetId() {
		return budgetId;
	}


	public Category getCategory() {
		return category;
	}


	public String getName() {
		return name;
	}


	public double getLimit() {
		return limit;
	}

	
}
