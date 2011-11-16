package budgeteventplanner.client.entity;

import java.util.List;

import budgeteventplanner.shared.UUID;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;

//		BudgetAttendee Vendor = (new BudgetAttendee).Builder(......).setId(id).build();
@Entity
public class Vendor {
	private String vendorID;
	private String name;
	private List<Category> categoryList;
	private String location;
	private List<Item> itemList;
	private List<Service> serviceList;

	public static class Builder {
		private Vendor vendor = new Vendor();

		public Builder(Vendor vendor) {
			this.vendor = vendor;
			this.vendor.itemList = Lists.newArrayList();
			this.vendor.serviceList = Lists.newArrayList();
		}

		public Builder() {
			this.vendor.vendorID = UUID.randomUUID();
		}

		public Builder setLocation(String location) {
			this.vendor.location = location;
			return this;
		}

		public Builder setName(String name) {
			this.vendor.name = name;
			return this;
		}

		public Vendor build() {
			return this.vendor;
		}

		public Builder addService(Service service) {
			this.vendor.serviceList.add(service);
			return this;
		}
	}

	public String getVendorID() {
		return vendorID;
	}

	public String getName() {
		return name;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public String getLocation() {
		return location;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public List<Service> getServiceList() {
		return serviceList;
	}
}
