package budgeteventplanner.client;

import java.util.List;
import java.util.Map.Entry;

import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {
	void addService(String categoryId, String vendorId, String name,
			Double price, String description, AsyncCallback<Void> callback);

	void deleteService(String serviceId, AsyncCallback<Void> callback);

	void getServiceByVendorId(String vendorId,
			AsyncCallback<List<Entry<String, Service>>> callback);

	void getServiceRequestByStatus(String vendorId, Integer status,
			AsyncCallback<List<ServiceRequest>> callback);

	void updateServiceRequestStatus(String serviceRequestId, Integer status,
			AsyncCallback<Void> callback);
}
