package budgeteventplanner.client.entity;

import budgeteventplanner.client.entity.Category;

import com.googlecode.objectify.annotation.Entity;

//		BudgetService Service = (new BudgetService).Builder(......).setId(id).build();
@Entity
public class Service {
	private String name;
	private Category category;
	private String description;
	private String VendorID;

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

		public Builder setDescription(String description) {
			this.service.description = description;
			return this;
		}
		
		public Builder setVendorID(String VendorID) {
			this.service.VendorID = VendorID;
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

	public String getDescription() {
		return description;
	}
	
	public String getVendorID() {
		return VendorID;
	}
}
