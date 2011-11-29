package budgeteventplanner.server;

import java.security.NoSuchAlgorithmException;

import budgeteventplanner.client.UserService;
import budgeteventplanner.client.entity.Attendee;
import budgeteventplanner.client.entity.Budget;
import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;
import budgeteventplanner.client.entity.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {
	static {
		ObjectifyService.register(User.class);
    ObjectifyService.register(Service.class);
    ObjectifyService.register(ServiceRequest.class);
    ObjectifyService.register(Event.class);
    ObjectifyService.register(Category.class);
    ObjectifyService.register(Budget.class);
    ObjectifyService.register(BudgetItem.class);
    ObjectifyService.register(Attendee.class);
	}

	@Override
	public void register(String email, String password, Integer role)
			throws NoSuchAlgorithmException {
		// String encrypted =
		// MessageDigest.getInstance("SHA-1").digest(password.getBytes()).toString();
		String encrypted = password;
		Objectify ofy = ObjectifyService.begin();
		User user = new User.Builder(email, encrypted, role).build();
		ofy.put(user);
	}

	@Override
	public Integer login(String email, String password)
			throws NoSuchAlgorithmException {
		// String encrypted =
		// MessageDigest.getInstance("SHA-1").digest(password.getBytes()).toString();
		String encrypted = password;
		Objectify ofy = ObjectifyService.begin();

		User user = ofy.query(User.class).filter("email", email).get();

		if (encrypted.equals(user.getPassword())) {
			return user.getRole();
		} else {
			return -1;
		}
	}

	@Override
	public Integer attendeeLogin(String registrationCode) {
		return 1;
	}

}