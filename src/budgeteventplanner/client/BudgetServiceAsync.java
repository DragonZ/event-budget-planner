package budgeteventplanner.client;

import java.util.ArrayList;

import budgeteventplanner.client.entity.Budget;
import budgeteventplanner.client.entity.BudgetItem;

import com.google.appengine.repackaged.com.google.common.base.Pair;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BudgetServiceAsync {
	void createBudget(String eventId, String name, AsyncCallback<Budget> callback);
	void addBudgetItemToBudget(String categoryId, String budgetId, Double limit, AsyncCallback<BudgetItem> callback);
	void getTotalByEventId(String eventId, AsyncCallback<Double> callback);
	void getSubtotalsByEventId(String eventId, AsyncCallback<ArrayList<Pair<String, Double>>> callback);
}
