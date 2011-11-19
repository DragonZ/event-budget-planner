package budgeteventplanner.server;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import budgeteventplanner.client.OrganizerService;
import budgeteventplanner.client.entity.Address;
import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;
import budgeteventplanner.client.entity.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class OrganizerServiceImpl extends RemoteServiceServlet implements OrganizerService 
{
	public OrganizerServiceImpl() 
	{
		ObjectifyService.register(User.class);
	}
		
	@Override
	public List<Event> getEventsByUserId(String OrganizerID) {
		Objectify ofy = ObjectifyService.begin();		
		List<Event> wantedEventList = new ArrayList<Event>();
		List<Event> events = ofy.query(Event.class).list();
		for(Event evn : events)
		{
			if(evn.getOrganizerId().equalsIgnoreCase(OrganizerID))
			{
				wantedEventList.add(evn);
			}
		}
		return wantedEventList;
	}


	@Override
	public void AddNewEventByUserId(String organizerID, String eventTitle,
			Date startDate, Date endDate, String location) 
	{
		Objectify ofy = ObjectifyService.begin();
		Event newEvent = new Event.Builder(organizerID, eventTitle, Event.INACTIVE)
		.setStartTime(startDate)
		.setEndTime(endDate)
		.setLocation(new Address.Builder(location).build())
		.setOrganizerId(organizerID)
		.build();
		ofy.put(newEvent);
		
	}


	@Override
	public void updateEventByEventId(String eventID, String eventTitle,
			Date startDate, Date endDate, String location) {
		Objectify ofy = ObjectifyService.begin();
		Event event = ofy.query(Event.class).filter("Id", eventID).get();
		new Event.Builder(event).setSventTitle(eventTitle)
		.setStartTime(startDate)
		.setEndTime(endDate)
		.setLocation(new Address.Builder(location).build())
		.build();
		ofy.put(event);
	}



	@Override
	public List<ServiceRequest> getServiceRequestByEventId(String eventID) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void AddNewItemByEventId(String eventID, String category,
			String service, Date dueDate, String requestDetails) {
		// TODO Auto-generated method stub
		
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