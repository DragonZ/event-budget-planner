package budgeteventplanner.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("event")
public interface EventService extends RemoteService {
	void createEvent(String organizerId, String name, Date startTime,
			Date endTime, String address);

	void updateEventByEventId(String eventId, String name, Date startTime,
			Date endTime, String address, Integer status);

	List<Event> getEventsByOrganizerId(String organizerId);

	List<Event> getEventsByOrganizerIdAndStatus(String organizerId,
			Integer status);

	void addServiceRequest(String serviceId, String eventId, String name,
			Date dueDate);

	List<ServiceRequest> getServiceRequestsByEventId(String eventId);

	List<Service> getServicesByCategoryId(String categoryId);

	void fillAttendeesInEvent(String eventId, ArrayList<String> attendeeIdList);

	void changeEventStatusByEventId(String eventId, Integer status);

	void deleteEventByEventId(String eventId);
}
