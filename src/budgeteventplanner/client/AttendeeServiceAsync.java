package budgeteventplanner.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import budgeteventplanner.client.entity.Attendee;

public interface AttendeeServiceAsync {
	void createAttendee(String eventId, String name, String email, AsyncCallback<Attendee> attendeeId);
	void getAttendeeListByOrganizerId(String organizerId, AsyncCallback<ArrayList<Attendee>> attendeeList);
	void getAttendeeListByEventId(String eventId, AsyncCallback<ArrayList<Attendee>> attendeeList);
	void updateAttendeeInfo(String attendeeId, String name, String email, String jobTitle, String companyName, String address, String phoneNum, Integer status, AsyncCallback<Void> feedback);
	void attendeeLogin(String registrationCode, AsyncCallback<Integer> status);
	void getAttendeeByAttendeeId(String attendeeId, AsyncCallback<Attendee> attendee);
	void deleteAttendeeByAttendeeIdList(ArrayList<String> attendeeIdList, AsyncCallback<Void> attendee);
}