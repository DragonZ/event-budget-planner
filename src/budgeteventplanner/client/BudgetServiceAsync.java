package budgeteventplanner.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BudgetServiceAsync {
	void createBudget(String budgetName, AsyncCallback<Void> callback) throws NoSuchAlgorithmException;
	
}
