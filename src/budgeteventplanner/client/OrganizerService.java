package budgeteventplanner.client;


import java.util.Date;
import java.util.List;

import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.ServiceRequest;
import budgeteventplanner.client.entity.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("organizer")
public interface OrganizerService extends RemoteService {
	List<Event> getEventsByUserId(String organizerID);

	void AddNewEventByUserId(String organizerID, String eventTitle, Date startDate, Date endDate, String location);
	//Server should generate a unique EventID for the new event

	void updateEventByEventId(String eventID, String eventTItle, Date startDate, Date endDate, String location);

	List<User> getAttendeeList(String eventOrganizerID);

	List<ServiceRequest> getItemListByEventId(String eventID);

	void AddNewItemByEventId(String eventID, String category, String service, Date dueDate, String requestDetails);
}
