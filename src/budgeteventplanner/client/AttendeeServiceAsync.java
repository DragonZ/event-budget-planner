package budgeteventplanner.client;

import java.util.ArrayList;

import budgeteventplanner.client.entity.Attendee;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AttendeeServiceAsync {
	void createAttendee(String firstName, String lastName, String email, AsyncCallback<Void> callback);
	void getAttendeeList(String eventOrganizerID, AsyncCallback<ArrayList<Attendee>> callback);
	void saveAttendeeInfo(String attendeeID,  String email, 
			String firstName, String midName, String lastName, 
			String jobTitle, String companyName, String address, String phoneNum, AsyncCallback<Void> callback);

}
