package budgeteventplanner.client;

import java.util.ArrayList;

import budgeteventplanner.client.entity.Attendee;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("attendee")
public interface AttendeeService extends RemoteService {
	Attendee createAttendee(String eventId, String name, String email);
	ArrayList<Attendee> getAttendeeListByOrganizerId(String organizerId);
	ArrayList<Attendee> getAttendeeListByEventId(String eventId);
	void updateAttendeeInfo(String attendeeId, String name, String email, String jobTitle, String companyName, String address, String phoneNum, Integer status);
	Integer attendeeLogin(String registrationCode);
	Attendee getAttendeeByAttendeeId(String attendeeId);
	void deleteAttendeeByAttendeeIdList(ArrayList<String> attendeeIdList);
	void sendEmail(String attendeeId, Integer status);
	void sendEmailBatch(ArrayList<String> attendeeIdList, Integer status);
	void sendEmailBatchByOrganizer(ArrayList<String> attendeeIdList,Integer status);
	void sendCustomizedEmail(Attendee attendee, String subject,String msgBody);
	void sendCustomizedEmail(String attendeeId, String subject,String msgBody);
}