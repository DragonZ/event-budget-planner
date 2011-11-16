package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
	void register(String email, String password, Integer role) throws NoSuchAlgorithmException;
	Integer login(String email, String password) throws NoSuchAlgorithmException;
	Integer attendeeLogin(String registrationCode) throws NoSuchAlgorithmException;
}
