package budgeteventplanner.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("add")
public interface AddService extends RemoteService {
	String add(Integer a, Integer b);
}
