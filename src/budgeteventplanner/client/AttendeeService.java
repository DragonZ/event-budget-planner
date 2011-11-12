package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("attendee")
public interface AttendeeService extends RemoteService {
	void createAttendee(String firstName, String lastName, String email) throws NoSuchAlgorithmException;

}
