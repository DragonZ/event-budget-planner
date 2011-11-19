package budgeteventplanner.client;


import java.util.List;
import java.util.Date;
import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.ServiceRequest;
import budgeteventplanner.client.entity.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OrganizerServiceAsync {
	
	void getEventsByUserId(String organizerID, AsyncCallback<List<Event>> callback);


	void AddNewEventByUserId(String organizerID, String eventTitle, Date startDate, Date endDate, String location, AsyncCallback<Void> callback);
	//Server should generate a unique EventID for the new event

	void updateEventByEventId(String eventID, String eventTItle, Date startDate, Date endDate, String location, AsyncCallback<Void> callback);

	void getAttendeeList(String eventOrganizerID, AsyncCallback<List<User>> callback);

	void getItemListByEventId(String eventID, AsyncCallback<List<ServiceRequest>> callback);

	void AddNewItemByEventId(String eventID, String category, String service, Date dueDate, String requestDetails, AsyncCallback<Void> callback);
}
