package budgeteventplanner.shared.dto;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;
import java.util.UUID;


@Entity
public class BudgetItem
{
	@Id private UUID budgetId;
	private int limit;
	
	public static class Builder {
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
