package budgeteventplanner.shared.dto.entity;
package budgeteventplanner.shared.dto.nonentity;

import java.util.UUID;

import budgeteventplanner.shared.dto.Nonentity.Category;

import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = (new BudgetItem).Builder(......).setId(id).build();
@Entity
public class BudgetItem
{
	private UUID budgetId;
	private Category category;
	private String name;
	private double limit;
	
	
	
	public static class Builder
	{
		private BudgetItem budgetItem;
		
		@Override
		public Builder(String name)
		{
			this.budgetItem.name= name;
		}
		
		public BudgetItem build() 
		{
			return this.budgetItem;
		}
		
		public Builder setLimit(double limit)
		{
			this.budgetItem.limit = limit;
			return this;
		}
		
		public Builder setId(int id)
		{
			this.budgetItem.budgetId = id;
			return this;
		}
	}
	
	
}
