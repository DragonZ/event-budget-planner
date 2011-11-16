package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import budgeteventplanner.client.entity.Organizer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EventServiceAsync {
	void createEvent(String eventName, String organizer, Integer visibility, AsyncCallback<Void> callback) throws NoSuchAlgorithmException;

}
