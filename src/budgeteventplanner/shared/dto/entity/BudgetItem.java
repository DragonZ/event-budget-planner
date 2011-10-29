package budgeteventplanner.shared.dto.entity;
package budgeteventplanner.shared.dto.nonentity;

import java.util.UUID;

import budgeteventplanner.shared.dto.Nonentity.Category;

import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = new BudgetItem.Builder(500, new UUID()).setId(id).build();
@Entity
public class BudgetItem
{

	private Category category;
	private double limit;
	
	
	
	public static class Builder
	{
		private BudgetItem budgetItem;
		
		@Override
		public Builder(int limit, UUID setId) {
			this.budgetItem.limit = limit;
		}
		
		public BudgetItem build() {
			return this.budgetItem;
		}
		
		public Builder setLimit(int limit) {
			this.budgetItem.limit = limit;
			return this;
		}
		
		public Builder setId(int id) {
			this.budgetItem.budgetId = id;
			return this;
		}
	}
	
	
}
