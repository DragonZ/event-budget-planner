package budgeteventplanner.client.entity;

import java.util.Date;

import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class ServiceRequest {
	@Id
	private String requestId;
	private String serviceId;
	private String eventId;
	private String name;
	private Date dueDate;
	private Integer status;

	public static final Integer PENDING = 0;
	public static final Integer ACCEPTED = 1;
	public static final Integer IGNORED = 2;

	public ServiceRequest() {}

	public static class Builder {
		private ServiceRequest request;

		public Builder(String serviceId, String eventId, String name,
				Date dueDate) {
			this.request.requestId = UUID.randomUUID();
			this.request.serviceId = serviceId;
			this.request.eventId = eventId;
			this.request.name = name;
			this.request.dueDate = dueDate;
			this.request.status = PENDING;
		}

		public Builder(ServiceRequest item) {
			this.request = item;
		}

		public Builder setEventId(String eventId) {
			this.request.eventId = eventId;
			return this;
		}

		public Builder setName(String name) {
			this.request.name = name;
			return this;
		}

		public Builder setDueDate(Date date) {
			this.request.dueDate = date;
			return this;
		}

		public Builder setStatus(Integer status) {
			this.request.status = status;
			return this;
		}

		public ServiceRequest build() {
			return this.request;
		}
	}

	public String getRequestId() {
		return requestId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Integer getStatus() {
		return status;
	}
}