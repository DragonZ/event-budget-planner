package budgeteventplanner.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void sendEmail(String name, String email,AsyncCallback<Integer> callback) throws IllegalArgumentException;
	void getAttendeeInfo(String name,AsyncCallback<String> callback) throws IllegalArgumentException;
}
