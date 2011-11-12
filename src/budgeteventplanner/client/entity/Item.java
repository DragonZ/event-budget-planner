package budgeteventplanner.client.entity;

import java.util.Date;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = (new BudgetItem).Builder(......).setId(id).build();
@Entity
public class Item {
	private String itemID;
	private Category category;
	private String name;
	private Date dueDate;
	private Vendor vendor;

	public static class Builder {
		private Item item;

		public Builder(Item item) {
			this.item = item;
		}

		public Builder(String name) {
			this.item.name = name;
			this.item.itemID = UUID.randomUUID();
		}

		public Builder setVendor(Vendor vendor) {
			this.item.vendor = vendor;
			return this;
		}

		public Builder setdueDate(Date date) {
			this.item.dueDate = date;
			return this;
		}

		public Builder setCategory(Category category) {
			this.item.category = category;
			return this;
		}

		public Item build() {
			return this.item;
		}
	}

	public String getItemID() {
		return itemID;
	}

	public Category getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Vendor getVendor() {
		return vendor;
	}

}