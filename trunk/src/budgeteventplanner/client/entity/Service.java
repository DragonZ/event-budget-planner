package budgeteventplanner.client.entity;

import budgeteventplanner.client.entity.Category;

import com.googlecode.objectify.annotation.Entity;

//		BudgetService Service = (new BudgetService).Builder(......).setId(id).build();
@Entity
public class Service {
	private String name;
	private Category category;
	private Double price;

	public static class Builder {
		private Service service = new Service();

		public Builder(Service service) {
			this.service = service;
		}

		public Builder setName(String name) {
			this.service.name = name;
			return this;
		}

		public Builder setCategory(Category category) {
			this.service.category = category;
			return this;
		}

		public Builder setPrice(Double price) {
			this.service.price = price;
			return this;
		}

		public Service build() {
			return this.service;
		}
	}

	public String getName() {
		return name;
	}

	public Category getCategory() {
		return category;
	}

	public Double getPrice() {
		return price;
	}
}