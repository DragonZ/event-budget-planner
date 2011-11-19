package budgeteventplanner.client.entity;

import java.io.Serializable;

import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class Service implements Serializable {
	@Id
	private String serviceId;
	private String categoryId;
	private String vendorId;
	private String name;
	private Double price;
	private String description;

	public Service(){}
	
	public static class Builder {
		private Service service = new Service();

		public Builder(String categoryId, String vendorId, String name, Double price, String description) {
			this.service = new Service();
			this.service.serviceId = UUID.randomUUID();
			this.service.categoryId = categoryId;
			this.service.vendorId = vendorId;
			this.service.name = name;
			this.service.price = price;
			this.service.description = description;
		}
		
		public Builder(Service service) {
			this.service = service;
		}

		public Builder setName(String name) {
			this.service.name = name;
			return this;
		}

		public Builder setCategoryId(String categoryId) {
			this.service.categoryId = categoryId;
			return this;
		}

		public Builder setVendorId(String vendorId) {
			this.service.vendorId = vendorId;
			return this;
		}
		
		public Builder setDescription(String description) {
			this.service.description = description;
			return this;
		}
		
		public Service build() {
			return this.service;
		}
	}

	public String getServiceId() {
		return serviceId;
	}


	public String getCategoryId() {
		return categoryId;
	}


	public String getVendorId() {
		return vendorId;
	}


	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}
	
}