package budgeteventplanner.client.entity;

import java.io.Serializable;

import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class Budget implements Serializable {
	@Id
	private String budgetId;
	private String eventId;
	private String name;
	private Double limit;

	public Budget() {
	}

	public static class Builder {
		private Budget budget;

		public Builder(Budget budget) {
			this.budget = budget;
		}

		public Builder(String eventId, String name) {
			this.budget = new Budget();
			this.budget.budgetId = UUID.randomUUID();
			this.budget.eventId = eventId;
			this.budget.name = name;
		}

		public Builder setEventId(String eventId) {
			this.budget.eventId = eventId;
			return this;
		}

		public Builder setName(String name) {
			this.budget.name = name;
			return this;
		}

		public Builder setLimit(Double limit) {
			this.budget.limit = limit;
			return this;
		}

		public Budget build() {
			return this.budget;
		}
	}

	public String getBudgetId() {
		return budgetId;
	}

	public String getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}

	public Double getLimit() {
		return limit;
	}
}