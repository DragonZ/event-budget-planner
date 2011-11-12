package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("budget")
public interface BudgetService extends RemoteService {
	void createBudget(String budgetName) throws NoSuchAlgorithmException;

}
