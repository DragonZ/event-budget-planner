package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vendor")
public interface VendorService extends RemoteService {
	void createVendor(String vendorName, String location) throws NoSuchAlgorithmException;
	
	//add a new sevice to a vendor’s service list
	void addService(String categoryId, String vendorId, String name, Double price, String description);

	//get the vendor’s existing service list
	List<Category> getExistingCategory(String VendorID);

	//delete a service from the vendor’s service list
	void deleteExistingService(String ServiceID, String VendorID);

	//get all categories from the server
	List<Category> getAllCategory();

	//get all pending events from server
	//TODO new entity
	List<ServiceRequest> getPendingServiceRequest(String VendorID);

	//get all accepted events from server
	List<ServiceRequest> getAcceptedServiceRequest(String VendorID);

	//get all ignored events from server
	List<ServiceRequest> getIgnoredserviceRequest(String VendorID);

	//set a event from pending to ignore
	void ignoreServiceRequest(String VendorID, String ServiceRequestID);

	//set a event from pending to accepted
	void acceptServiceRequest(String VendorID, String ServiceRequestID);
	
	
}
