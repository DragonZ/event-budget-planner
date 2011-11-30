package budgeteventplanner.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import budgeteventplanner.client.entity.Attendee;

public interface AttendeeServiceAsync {
	void createAttendee(String eventId, String name, String email,
			AsyncCallback<Attendee> callback);

	void getAttendeeListByOrganizerId(String organizerId,
			AsyncCallback<ArrayList<Attendee>> callback);

	void getAttendeeListByEventId(String eventId,
			AsyncCallback<ArrayList<Attendee>> callback);

	void updateAttendeeInfo(String attendeeId, String name, String email,
			String jobTitle, String companyName, String address,
			String phoneNum, Integer status, AsyncCallback<Void> callback);

	void attendeeLogin(String registrationCode, AsyncCallback<Integer> callback);

	void getAttendeeByAttendeeId(String attendeeId,
			AsyncCallback<Attendee> callback);

	void deleteAttendeeByAttendeeIdList(ArrayList<String> attendeeIdList,
			AsyncCallback<Void> callback);

	void sendEmail(String attendeeId, Integer status,
			AsyncCallback<Void> callback);

	void sendEmailBatch(ArrayList<String> attendeeIdList, Integer status,
			AsyncCallback<Void> callback);

	void sendEmailBatchByOrganizer(ArrayList<String> attendeeIdList,
			Integer status, AsyncCallback<Void> callback);

	void sendCustomizedEmail(Attendee attendee, String subject, String msgBody,
			AsyncCallback<Void> callback);

	void sendCustomizedEmail(String attendeeId, String subject, String msgBody,
			AsyncCallback<Void> callback);

	void fillAttendeesInEvent(String eventId, ArrayList<String> attendeeIdList,
			AsyncCallback<Void> callback);
}