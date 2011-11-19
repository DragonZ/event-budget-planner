package budgeteventplanner.client;

import java.util.ArrayList;

import budgeteventplanner.client.entity.Attendee;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("attendee")
public interface AttendeeService extends RemoteService {
	String createAttendee(String eventId, String name, String email);
	ArrayList<Attendee> getAttendeeListByOrganizerId(String organizerId);
	ArrayList<Attendee> getAttendeeListByEventId(String eventId);
	void updateAttendeeInfo(String attendeeId, String name, String email, String jobTitle, String companyName, String address, String phoneNum);
	Integer attendeeLogin(String registrationCode);
}
