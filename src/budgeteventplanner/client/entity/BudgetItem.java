package budgeteventplanner.client.entity;

import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = (new BudgetItem).Builder(......).setId(id).build();
@Entity
public class BudgetItem
{
	private String budgetId;
	private Category category;
	private String name;
	private Double limit;

	public static class Builder
	{
		private BudgetItem budgetItem = new BudgetItem();
		
		public Builder(BudgetItem budgetItem) {
			this.budgetItem = budgetItem;
		}

		public Builder(String name)
		{
			this.budgetItem.name= name;
		}

		public Builder setLimit(double limit)
		{
			this.budgetItem.limit = limit;
			return this;
		}

		public BudgetItem build() 
		{
			return this.budgetItem;
		}
	}

	public String getBudgetId() {
		return budgetId;
	}

	public Category getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public Double getLimit() {
		return limit;
	}
}
