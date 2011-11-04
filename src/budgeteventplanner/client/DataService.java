package budgeteventplanner.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService {
	TestEntity putEntity(TestEntity ent);
	TestEntity getEntityByName(String name);
}
