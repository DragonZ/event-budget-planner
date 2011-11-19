package budgeteventplanner.client.entity;

import java.util.Date;

import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

//		BudgetItem item = (new BudgetItem).Builder(......).setId(id).build();
@Entity
public class ServiceRequest {
	List<Service> listService= query(Service.class).filter("vendorId","xxxxx")
	for (Service s : listService)
	
	@Id
	private String requestId;
	private String serviceId;
	private String name;
	private Date dueDate;

	private Integer status;
	
	public static final Integer PENDING = 1;
	public static final Integer ACCEPTED = 2;
	public static final Integer IGNORED = 0;
	
	
	public ServiceRequest(){}
	
	public static class Builder {
		private ServiceRequest request = new ServiceRequest();

		public Builder(ServiceRequest item) {
			this.request = item;
		}

		public Builder(String name) {
			this.request.name = name;
			this.request.requestId = UUID.randomUUID();
		}

		public Builder setdueDate(Date date) {
			this.request.dueDate = date;
			return this;
		}


		public ServiceRequest build() {
			return this.request;
		}
	}

	public String getItemID() {
		return requestId;
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