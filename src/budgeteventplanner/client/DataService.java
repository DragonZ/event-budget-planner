package budgeteventplanner.client;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService {
	void putEntity(String name, String address, Integer limit);
	ArrayList<TestEntity> getEntityByName(String name);
	ArrayList<TestEntity> getEntityByLimit(Integer limit);
}
