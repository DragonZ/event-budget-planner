package budgeteventplanner.server;

import java.util.ArrayList;
import java.util.List;

import budgeteventplanner.client.VendorService;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;
import budgeteventplanner.client.entity.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class VendorServiceImpl extends RemoteServiceServlet implements
		VendorService {
	
	public VendorServiceImpl() {
		ObjectifyService.register(Service.class);
	}
	@Override
	public void addService(String categoryId, String vendorId, String name,
			Double price, String description) {
		Objectify ofy = ObjectifyService.begin();
		Service service = new Service.Builder(categoryId, vendorId, name,
				price, description).build();
		ofy.put(service);
	}

	@Override
	public void deleteService(String serviceId) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(serviceId);
	}

	@Override
	public List<Service> getServiceByVendorId(String vendorId) {
		Objectify ofy = ObjectifyService.begin();
		Query<Service> q = ofy.query(Service.class)
				.filter("vendorId", vendorId);
		return q.list();
	}

	@Override
	public List<ServiceRequest> getServiceRequestByStatus(String vendorId,
			String status) {
		Objectify ofy = ObjectifyService.begin();
		List<ServiceRequest> result = new ArrayList<ServiceRequest>();

		Query<Service> queryService = ofy.query(Service.class).filter(
				"vendorId", vendorId);
		for (Service s : queryService) {
			Query<ServiceRequest> queryServiceRequest = ofy.query(
					ServiceRequest.class).filter("serviceId", s.getServiceId());
			for (ServiceRequest request : queryServiceRequest) {
				if (request.getStatus().equals(status)) {
					result.add(request);
				}
			}
		}
		return result;
	}

	@Override
	public void updateServiceRequestStatus(String serviceRequestId,
			Integer status) {
		Objectify ofy = ObjectifyService.begin();
		ServiceRequest oldRequest = ofy.query(ServiceRequest.class)
				.filter("serviceRequestId", serviceRequestId).get();
		ServiceRequest newRequest = new ServiceRequest.Builder(oldRequest)
				.setStatus(status).build();
		ofy.put(newRequest);
	}

}