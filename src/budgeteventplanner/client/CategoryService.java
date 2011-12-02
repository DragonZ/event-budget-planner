package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.client.entity.Category;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("category")
public interface CategoryService extends RemoteService {
	void createCategory(String categoryName);
	List<Category> getAllCategory();
	String getCategoryName(String categoryId);
}
