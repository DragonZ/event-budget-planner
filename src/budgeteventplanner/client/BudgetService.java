package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.Budget;
import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.shared.Pair;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("budget")
public interface BudgetService extends RemoteService {
  Budget createBudget(String eventId, String name, Double limit);

  BudgetItem addBudgetItemToBudget(String categoryId, String budgetId, Double limit);

  Double getTotalByEventId(String eventId);

  List<Pair<String, Double>> getSubtotalsByBudgetId(String budgetId);

  List<Pair<String, BudgetItem>> getLimitsByBudgetId(String budgetId);

  List<Budget> getBudgetByOrganizerId(String organizerId);

  void updateBudgetItemLimit(String budgetItemId, Double limit);
}