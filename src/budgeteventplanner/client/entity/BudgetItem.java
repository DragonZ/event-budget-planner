package budgeteventplanner.client.entity;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class BudgetItem implements Serializable {
	@Id
	private String budgetItemId;
	private String budgetId;
	private String categoryId;
	private Double limit;

	public BudgetItem() {
	}

	public static class Builder {
		private BudgetItem budgetItem;

		public Builder(BudgetItem budgetItem) {
			this.budgetItem = budgetItem;
		}

		public Builder(String budgetId, String categoryId, Double limit) {
			this.budgetItem = new BudgetItem();
			this.budgetItem.budgetId = budgetId;
			this.budgetItem.categoryId = categoryId;
			this.budgetItem.limit = limit;
		}

		public Builder setBudgetId(String budgetId) {
			this.budgetItem.budgetId = budgetId;
			return this;
		}

		public Builder setCategoryId(String categoryId) {
			this.budgetItem.categoryId = categoryId;
			return this;
		}

		public Builder setLimit(double limit) {
			this.budgetItem.limit = limit;
			return this;
		}

		public BudgetItem build() {
			return this.budgetItem;
		}
	}

	public String getBudgetItemId() {
		return budgetItemId;
	}

	public String getBudgetId() {
		return budgetId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public Double getLimit() {
		return limit;
	}
}
