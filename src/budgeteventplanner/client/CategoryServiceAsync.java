package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.Category;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CategoryServiceAsync {
	void createCategory(String categoryName, AsyncCallback<Void> callback);
	void getAllCategory(AsyncCallback<List<Category>> callback);
	void getCategoryName(String categoryId, AsyncCallback<String> callback);
}
