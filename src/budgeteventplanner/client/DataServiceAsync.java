package budgeteventplanner.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {
	void putEntity(TestEntity ent, AsyncCallback<TestEntity> callback);
	void getEntityByName(String name, AsyncCallback<TestEntity> callback);
}
