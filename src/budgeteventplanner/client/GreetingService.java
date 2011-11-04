package budgeteventplanner.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	Integer sendEmail(String name, String email) throws IllegalArgumentException;
	String getAttendeeInfo(String name) throws IllegalArgumentException;
}
