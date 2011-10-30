package budgeteventplanner.shared.dto.entity;

import java.util.ArrayList;
import java.util.UUID;
import budgeteventplanner.shared.dto.entity.Category;
import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = (new BudgetItem).Builder(......).setId(id).build();
@Entity
public class Budget
{
	private UUID budgetID;
	private Category category;
	private String name;
	private ArrayList<BudgetItem> budget_item_list;
	private long eventID;
	private long limit;
	
	/**************************************************************/
	public static class Builder
	{
		private Budget budget;
		/*------------------------------------------------------*/
		public Builder(Budget budget) {
			this.budget = (Budget) budget.clone();
		}
		/*------------------------------------------------------*/
		@Override
		public Builder(String name)
		{
			this.budget.name= name;
			this.budget.budgetID = UUID.randomUUID();
		}
				
		public Builder set_budget_item_list(ArrayList<BudgetItem> list)
		{
			this.budget.budget_item_list = list;
			return this;
		}
		
		public Builder setId(int id)
		{
			this.budget.budgetID = id;
			return this;
		}
		//
		public Budget build() 
		{
			return this.budget;
		}
		
		
	}
	/**************************************************************/
	
	
}