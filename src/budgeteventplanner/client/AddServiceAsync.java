package budgeteventplanner.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddServiceAsync {
	void add(Integer a, Integer b, AsyncCallback<String> callback);
}
