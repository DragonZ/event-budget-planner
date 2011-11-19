package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("event")
public interface EventService extends RemoteService {
	void createEvent(String eventName, String organizer, Integer visibility) throws NoSuchAlgorithmException;

}
