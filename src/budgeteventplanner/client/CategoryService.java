package budgeteventplanner.client;

import java.util.ArrayList;

import budgeteventplanner.client.entity.Category;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("category")
public interface CategoryService extends RemoteService {
	void createCategory(String categoryName);
	ArrayList<Category> getAllCategory();
}
