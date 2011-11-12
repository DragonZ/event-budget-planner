package budgeteventplanner.client.entity;

import java.util.ArrayList;
import java.util.List;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = (new BudgetItem).Builder(......).setId(id).build();
@Entity
public class Budget {
	private String budgetID;
	private Category category;
	private String name;
	private List<BudgetItem> budget_item_list;
	private long eventID;
	private long limit;

	public static class Builder {
		private Budget budget = new Budget();

		public Builder(Budget budget) {
			this.budget = budget;
		}

		public Builder(String name) {
			this.budget.name = name;
			this.budget.budgetID = UUID.randomUUID();
		}

		public Builder set_budget_item_list(ArrayList<BudgetItem> list) {
			this.budget.budget_item_list = list;
			return this;
		}

		public Budget build() {
			return this.budget;
		}
	}

	public String getBudgetID() {
		return budgetID;
	}

	public Category getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public List<BudgetItem> getBudget_item_list() {
		return budget_item_list;
	}

	public long getEventID() {
		return eventID;
	}

	public long getLimit() {
		return limit;
	}
}