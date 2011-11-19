package budgeteventplanner.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BudgetServiceAsync {
	void createBudget(String budgetName, AsyncCallback<Void> callback);
}
