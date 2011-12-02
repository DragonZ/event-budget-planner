package budgeteventplanner.server;

import java.util.List;

import budgeteventplanner.client.BudgetService;
import budgeteventplanner.client.entity.Budget;
import budgeteventplanner.client.entity.BudgetItem;
import budgeteventplanner.client.entity.Category;
import budgeteventplanner.client.entity.Event;
import budgeteventplanner.client.entity.Service;
import budgeteventplanner.client.entity.ServiceRequest;
import budgeteventplanner.shared.Pair;

import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class BudgetServiceImpl extends RemoteServiceServlet implements
		BudgetService {

	static {
		try {
			ObjectifyService.register(Budget.class);
			ObjectifyService.register(BudgetItem.class);
			ObjectifyService.register(Service.class);
			ObjectifyService.register(ServiceRequest.class);
			ObjectifyService.register(Category.class);
			ObjectifyService.register(Event.class);
		} catch (Exception e) {
		}
	}

	@Override
	public Budget createBudget(String eventId, String name, Double Limit) {
		Objectify ofy = ObjectifyService.begin();
		Budget budget = new Budget.Builder(eventId, name).setLimit(Limit).build();
		ofy.put(budget);
		return budget;
	}

	@Override
	public BudgetItem addBudgetItemToBudget(String categoryId, String budgetId,
			Double limit) {
		Objectify ofy = ObjectifyService.begin();
		Query<BudgetItem> q = ofy.query(BudgetItem.class)
				.filter("budgetId", budgetId).filter("categoryId", categoryId);
		BudgetItem budgetItem;

		if (q.count() > 0) {
			budgetItem = new BudgetItem.Builder(q.get()).setLimit(limit)
					.build();
		} else {
			budgetItem = new BudgetItem.Builder(budgetId, categoryId, limit)
					.build();
		}

		ofy.put(budgetItem);
		return budgetItem;
	}

	@Override
	public Double getTotalByEventId(String eventId) {
		Double total = 0.0;
		Objectify ofy = ObjectifyService.begin();
		Query<ServiceRequest> queryServiceRequest = ofy.query(
				ServiceRequest.class).filter("eventId", eventId);
		for (ServiceRequest request : queryServiceRequest) {
			Double price = ofy.get(
					new Key<Service>(Service.class, request.getServiceId()))
					.getPrice();
			total += price * request.getQuantity();
		}
		return total;
	}

	@Override
	public List<Pair<String, Double>> getSubtotalsByEventId(String eventId) {
		List<Pair<String, Double>> list = Lists.newArrayList();
		Objectify ofy = ObjectifyService.begin();
		Query<ServiceRequest> queryServiceRequest = ofy.query(
				ServiceRequest.class).filter("eventId", eventId);
		for (ServiceRequest request : queryServiceRequest) {
			String categoryId = ofy.get(
					new Key<Service>(Service.class, request.getServiceId()))
					.getCategoryId();
			String categoryName = ofy.get(
					new Key<Category>(Category.class, categoryId)).getName();
			Double price = ofy.get(
					new Key<Service>(Service.class, request.getServiceId()))
					.getPrice();
			list.add(new Pair<String, Double>(categoryName, price));
		}
		return list;
	}
	
	@Override
	public List<Pair<String, BudgetItem>> getLimitsByBudgetId(String budgetId) {
	  List<Pair<String, BudgetItem>> list = Lists.newArrayList();
	  Objectify ofy = ObjectifyService.begin();
	  Query<BudgetItem> queryBudgetItem = ofy.query(
	      BudgetItem.class).filter("budgetId", budgetId);
	    for (BudgetItem item : queryBudgetItem) {
	      String categoryName = ofy.get(
	          new Key<Category>(Category.class, item.getCategoryId())).getName();
	      list.add(new Pair<String, BudgetItem>(categoryName, item));
	    }
	    return list;
	}

  @Override
  public List<Budget> getBudgetByOrganizerId(String organizerId) {
    List<Budget> budgetList = Lists.newArrayList();
    Objectify ofy = ObjectifyService.begin();
    Query<Event> queryEvent = ofy.query(Event.class).filter("organizerId", organizerId);
    for (Event event : queryEvent) {
      Query<Budget> queryBudgetItem = ofy.query(
          Budget.class).filter("eventId", event.getEventId());
      budgetList.addAll(queryBudgetItem.list());
    }
    return budgetList;
  }

  @Override
  public void updateBudgetItemLimit(String budgetItemId, Double limit) {
	  Objectify ofy = ObjectifyService.begin();
	  BudgetItem newBudgetItem = ofy.query(BudgetItem.class).filter("budgetItemId", budgetItemId).get();
	  newBudgetItem = new BudgetItem.Builder(newBudgetItem).setLimit(limit).build();
	  ofy.put(newBudgetItem);
  }
}