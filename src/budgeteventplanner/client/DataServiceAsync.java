package budgeteventplanner.client;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {
	void putEntity(String name, String address, Integer limit, AsyncCallback<Void> callback);
	void getEntityByName(String name, AsyncCallback<ArrayList<TestEntity>> callback);
	void getEntityByLimit(Integer limit, AsyncCallback<ArrayList<TestEntity>> callback);
}
