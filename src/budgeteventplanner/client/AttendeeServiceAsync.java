package budgeteventplanner.client;

import java.util.List;

import com.google.common.collect.SetMultimap;
import com.google.gwt.user.client.rpc.AsyncCallback;

import budgeteventplanner.client.entity.Attendee;

public interface AttendeeServiceAsync {
	void createAttendee(String eventId, String name, String email, AsyncCallback<Attendee> callback);

	void getAttendeeListByOrganizerId(String organizerId, AsyncCallback<List<Attendee>> callback);

	void getAttendeeListByEventId(String eventId, AsyncCallback<List<Attendee>> callback);

	void updateAttendeeInfo(String attendeeId, String name, String email, String jobTitle,
			String companyName, String address, String phoneNum, Integer status,
			AsyncCallback<Void> callback);

	void attendeeLogin(String registrationCode, AsyncCallback<Integer> callback);

	void getAttendeeByAttendeeId(String attendeeId, AsyncCallback<Attendee> callback);

	void removeAttendeeByAttendeeList(List<Attendee> attendeeList, AsyncCallback<Void> callback);

	void sendEmail(String attendeeId, Integer status, AsyncCallback<Void> callback);

	void sendEmailBatch(List<String> attendeeIdList, Integer status, AsyncCallback<Void> callback);

	void sendEmailBatchByOrganizer(List<String> attendeeIdList, Integer status,
			AsyncCallback<Void> callback);

	void sendCustomizedEmail(Attendee attendee, String subject, String msgBody,
			AsyncCallback<Void> callback);

	void sendCustomizedEmail(String attendeeId, String subject, String msgBody,
			AsyncCallback<Void> callback);

	void fillAttendeesInEvent(String eventId, List<Attendee> attendeeIdList,
			AsyncCallback<List<Attendee>> callback);

	void getSortedAttendeeList(String organizerId, String excludedEventId,
			AsyncCallback<SetMultimap<String, Attendee>> callback);
}