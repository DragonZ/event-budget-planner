package budgeteventplanner.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("budget")
public interface BudgetService extends RemoteService {
	void createBudget(String budgetName);
}
