package budgeteventplanner.server;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;

import budgeteventplanner.client.VendorService;
import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class VendorServiceImpl extends RemoteServiceServlet implements VendorService {
  public VendorServiceImpl() {
    ObjectifyService.register(Service.class);
    ObjectifyService.register(ServiceRequest.class);
  }

  @Override
  public void addService(String categoryId, String vendorId, String name, Double price,
      String description) {
    Objectify ofy = ObjectifyService.begin();
    Service service = new Service.Builder(categoryId, vendorId, name, price, description).build();
    ofy.put(service);
  }

  @Override
  public void deleteService(String serviceId) {
    Objectify ofy = ObjectifyService.begin();
    ofy.delete(new Key<Service>(Service.class, serviceId));
  }



  @Override
  public List<ServiceRequest> getServiceRequestByStatus(String vendorId, Integer status) {
    Objectify ofy = ObjectifyService.begin();
    List<ServiceRequest> result = Lists.newArrayList();

    Query<Service> queryService = ofy.query(Service.class).filter("vendorId", vendorId);
    for (Service s : queryService) {
      Query<ServiceRequest> queryServiceRequest = ofy.query(ServiceRequest.class).filter(
          "serviceId", s.getServiceId());
      for (ServiceRequest request : queryServiceRequest) {
        if (request.getStatus().equals(status)) {
          result.add(request);
        }
      }
    }
    return result;
  }

  @Override
  public void updateServiceRequestStatus(String serviceRequestId, Integer status) {
    Objectify ofy = ObjectifyService.begin();
    ServiceRequest oldRequest = ofy.get(new Key<ServiceRequest>(ServiceRequest.class,
        serviceRequestId));
    ServiceRequest newRequest = new ServiceRequest.Builder(oldRequest).setStatus(status).build();
    ofy.put(newRequest);
  }

  @Override
  public List<Entry<String, Service>> getServiceByVendorId(String vendorId) {
    Objectify ofy = ObjectifyService.begin();
    List<Entry<String, Service>> list = Lists.newArrayList();
    
    Query<Service> q = ofy.query(Service.class).filter("vendorId", vendorId);
    for (Service service : q) {
      String categoryName = ofy.get(new Key<Category>(Category.class, service.getCategoryId()))
          .getName();
      list.add(new SimpleEntry<String, Service>(categoryName, service));
    }
    return list;
  }

}