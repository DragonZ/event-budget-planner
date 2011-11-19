package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import budgeteventplanner.client.entity.Attendee;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("attendee")
public interface AttendeeService extends RemoteService {
	String createAttendee(String firstName, String lastName, String email) throws NoSuchAlgorithmException;
	ArrayList<Attendee> getAttendeeList(String eventOrganizerID);
	public void saveAttendeeInfo(String attendeeID,  String email, 
			String firstName, String midName, String lastName, 
			String jobTitle, String companyName, String address, String phoneNum);
	Integer attendeeLogin(String registrationCode) throws NoSuchAlgorithmException;
}
