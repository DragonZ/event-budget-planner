package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {
	void createVendor(String vendorName, String location, AsyncCallback<Void> callback) throws NoSuchAlgorithmException;
	
	//add a new sevice to a vendor’s service list
	void addService(String categoryId, String vendorId, String name, Double price, String description, AsyncCallback<Void> callback);

	//get the vendor’s existing service list
	void getExistingCategory(String VendorID, AsyncCallback<List<Category>> callback);

	//delete a service from the vendor’s service list
	void deleteExistingService(String ServiceID, String VendorID, AsyncCallback<Void> callback);

	//get all categories from the server
	void getAllCategory(AsyncCallback<List<Category>> callback);

	//get all pending events from server
	//TODO new entity
	void getPendingServiceRequest(String VendorID, AsyncCallback<List<ServiceRequest>> callback);

	//get all accepted events from server
	void getAcceptedServiceRequest(String VendorID, AsyncCallback<List<ServiceRequest>> callback);

	//get all ignored events from server
	void getIgnoredserviceRequest(String VendorID, AsyncCallback<List<ServiceRequest>> callback);

	//set a event from pending to ignore
	void ignoreServiceRequest(String VendorID, String ServiceRequestID, AsyncCallback<Void> callback);

	//set a event from pending to accepted
	void acceptServiceRequest(String VendorID, String ServiceRequestID, AsyncCallback<Void> callback);
}
