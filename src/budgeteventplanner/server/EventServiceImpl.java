package budgeteventplanner.server;

import java.util.Date;
import java.util.List;

import budgeteventplanner.client.EventService;
import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class EventServiceImpl extends RemoteServiceServlet implements EventService 
{
	public EventServiceImpl() 
	{
		ObjectifyService.register(Event.class);
	}

	@Override
	public void createEvent(String organizerId, String name, Date startTime,
			Date endTime, String address) {
		Objectify ofy = ObjectifyService.begin();
		Event event= new Event.Builder(organizerId, name, startTime, endTime, address).build();
		ofy.put(event);
	}

	@Override
	public void updateEventByEventId(String eventId, String name,
			Date startTime, Date endTime, String address, Integer status) {
		Objectify ofy = ObjectifyService.begin();
		Event oldEvent = ofy.query(Event.class).filter("eventId", eventId).get();
		Event newEvent = new Event.Builder(oldEvent).setName(name).setStartTime(startTime).setEndTime(endTime).setAddress(address).setStatus(status).build();
		ofy.put(newEvent);
	}

	@Override
	public List<Event> getEventsByOrganizerId(String organizerId) {
		Objectify ofy = ObjectifyService.begin();
		Query<Event> q = ofy.query(Event.class).filter("organizerId", organizerId);
		return q.list();
	}

	@Override
	public void addServiceRequest(String serviceId, String eventId,
			String name, Date dueDate) {
		Objectify ofy = ObjectifyService.begin();
		ServiceRequest request = new ServiceRequest.Builder(serviceId, eventId, name, dueDate).build();
		ofy.put(request);
	}

	@Override
	public List<ServiceRequest> getServiceRequestsByEventId(String eventId) {
		Objectify ofy = ObjectifyService.begin();
		Query<ServiceRequest> q = ofy.query(ServiceRequest.class).filter("eventId", eventId);
		return q.list();		
	}

	@Override
	public List<Service> getServicesByCategoryId(String categoryId) {
		Objectify ofy = ObjectifyService.begin();
		Query<Service> q = ofy.query(Service.class).filter("categoryId", categoryId);
		return q.list();
	}
}