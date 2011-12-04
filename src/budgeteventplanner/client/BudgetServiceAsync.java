package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.Budget;
import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.shared.Pair;
import budgeteventplanner.shared.Pent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BudgetServiceAsync {
  void createBudget(String eventId, String name, Double limit, AsyncCallback<Budget> callback);

  void addBudgetItemToBudget(String categoryId, String budgetId, Double limit,
      AsyncCallback<BudgetItem> callback);

  void getTotalByEventId(String eventId, AsyncCallback<Double> callback);

  void getSubtotalsByBudgetId(String budgetId, AsyncCallback<List<Pair<String, Double>>> callback);

  void getLimitsByBudgetId(String budgetId, AsyncCallback<List<Pair<String, BudgetItem>>> callback);

  void getBudgetByOrganizerId(String organizerId, AsyncCallback<List<Budget>> callback);

  void updateBudgetItemLimit(String budgetItemId, Double limit, AsyncCallback<Void> callback);
  
  void getAllCostInfoByBudgetId(String budgetId,
			AsyncCallback<List<Pent<String, String, String, Integer, Double>>> callback);
}
