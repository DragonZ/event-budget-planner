package budgeteventplanner.server;
import java.util.ArrayList;
import java.util.List;
import budgeteventplanner.client.VendorService;
import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;
import budgeteventplanner.client.entity.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class VendorServiceImpl extends RemoteServiceServlet implements VendorService 
{
	public VendorServiceImpl() 
	{
		ObjectifyService.register(User.class);
	}
		
	@Override
	public void addService(String categoryId, String vendorId, String name, Double price, String description)
	{
		Objectify ofy = ObjectifyService.begin();
		Service srv = new Service.Builder(categoryId, vendorId, name, price, description).build();
		ofy.put(srv);
	}
	
	@Override
	public List<Category> getExistingCategory(String VendorID)
	{
		List<Service> vendorSvcList = getServiceByID(VendorID);
		List<Category> catList = new ArrayList<Category>();
		for(Service svc : vendorSvcList)
		{
			catList.add(getCategoryByID(svc.getCategoryId()));
		}
		
		List<String> catIdList = new ArrayList<String>();
		List<Category> newCatList = new ArrayList<Category>();
		for(Category c : catList) 
		{
			if (!catIdList.contains(c.getCategoryId())) 
			{
				catIdList.add(c.getCategoryId());
				newCatList.add(c);
			}
		}
		
		return newCatList;
	}
	
	@Override
	public void deleteExistingService(String ServiceID, String VendorID)
	{
		Objectify ofy = ObjectifyService.begin();
	//	Service svc= ofy.query(Service.class).filter("id", ServiceID).get();
		
		ofy.delete(Service.class, ServiceID);
	}
	
	@Override
	public List<Category> getAllCategory()
	{
		Objectify ofy = ObjectifyService.begin();
		@SuppressWarnings("unchecked")
		List<Category> list = (List<Category>)(ofy.query(Category.class).fetch());
		return list;
	}

	@Override
	public List<ServiceRequest> getPendingServiceRequest(String VendorID) 
	{
		Objectify ofy = ObjectifyService.begin();
		List<ServiceRequest> pendingSvc = new ArrayList<ServiceRequest>();
		
		Query<Service> svc = ofy.query(Service.class).filter("vendorId", VendorID);
		
		for (Service s : svc) 
		{
			Query<ServiceRequest> svcReq = ofy.query(ServiceRequest.class).filter("serviceId", s.getServiceId());
			for (ServiceRequest request : svcReq)
			{
				if (request.getStatus() == ServiceRequest.PENDING) 
				{
					pendingSvc.add(request);
				}
			}
		}			
		return pendingSvc;

	}

	@Override
	public List<ServiceRequest> getAcceptedServiceRequest(String VendorID) {
		Objectify ofy = ObjectifyService.begin();
		List<ServiceRequest> acceptedSvc = new ArrayList<ServiceRequest>();
		
		Query<Service> svc = ofy.query(Service.class).filter("vendorId", VendorID);
		
		for (Service s : svc) 
		{
			Query<ServiceRequest> svcReq = ofy.query(ServiceRequest.class).filter("serviceId", s.getServiceId());
			for (ServiceRequest request : svcReq)
			{
				if (request.getStatus() == ServiceRequest.ACCEPTED) 
				{
					acceptedSvc.add(request);
				}
			}
		}			
		return acceptedSvc;
	}

	@Override
	public List<ServiceRequest> getIgnoredserviceRequest(String VendorID) 
	{
		Objectify ofy = ObjectifyService.begin();
		List<ServiceRequest> ignoredSvc = new ArrayList<ServiceRequest>();
		
		Query<Service> svc = ofy.query(Service.class).filter("vendorId", VendorID);
		
		for (Service s : svc) 
		{
			Query<ServiceRequest> svcReq = ofy.query(ServiceRequest.class).filter("serviceId", s.getServiceId());
			for (ServiceRequest request : svcReq)
			{
				if (request.getStatus() == ServiceRequest.IGNORED) 
				{
					ignoredSvc.add(request);
				}
			}
		}			
		return ignoredSvc;
	}

	@Override
	public void ignoreServiceRequest(String VendorID, String ServiceRequestID) 
	{
		Objectify ofy = ObjectifyService.begin();
		ServiceRequest svcReq= ofy.query(ServiceRequest.class).filter("id", ServiceRequestID).get();
		new ServiceRequest.Builder(svcReq).setStatus(ServiceRequest.IGNORED).build(); // Set status to ignored
		
		
	}

	@Override
	public void acceptServiceRequest(String VendorID, String ServiceRequestID) {
		Objectify ofy = ObjectifyService.begin();
		ServiceRequest svcReq= ofy.query(ServiceRequest.class).filter("id", ServiceRequestID).get();
		new ServiceRequest.Builder(svcReq).setStatus(ServiceRequest.ACCEPTED).build(); // Set status to ignored
		
	}
	
	// Local Helper Function
	private List<Service> getServiceByID(String VendorID) 
	{
		Objectify ofy = ObjectifyService.begin();		
		Query<Service> svc = ofy.query(Service.class).filter("vendorId", VendorID);
		return svc.list();
	}
	private Category getCategoryByID(String ServiceID) 
	{
		Objectify ofy = ObjectifyService.begin();		
		Category cat = ofy.query(Category.class).filter("ServiceId", ServiceID).get();
		return cat;
	}
}