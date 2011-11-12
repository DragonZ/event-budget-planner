package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AttendeeServiceAsync {
	void createAttendee(String firstName, String lastName, String email, AsyncCallback<Void> callback) throws NoSuchAlgorithmException;
	
}
