package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vendor")
public interface VendorService extends RemoteService {
	void addService(String categoryId, String vendorId, String name, Double price, String description);
	void deleteService(String serviceId);
	List<Service> getServiceByVendorId(String vendorId);
	List<ServiceRequest> getServiceRequestByStatus(String vendorId, Integer status);
	void updateServiceRequestStatus(String serviceRequestId, Integer status);
}
