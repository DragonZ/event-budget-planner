package budgeteventplanner.client;

import java.util.ArrayList;

import budgeteventplanner.client.entity.Budget;
import budgeteventplanner.client.entity.BudgetItem;

import com.google.appengine.repackaged.com.google.common.base.Pair;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("budget")
public interface BudgetService extends RemoteService {
	Budget createBudget(String eventId, String name);
	BudgetItem addBudgetItemToBudget(String categoryId, String budgetId, Double limit);
	Double getTotalByEventId(String eventId);
	ArrayList<Pair<String, Double>> getSubtotalsByEventId(String eventId);
}