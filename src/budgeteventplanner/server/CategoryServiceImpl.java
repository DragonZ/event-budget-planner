package budgeteventplanner.server;

import java.util.ArrayList;

import budgeteventplanner.client.CategoryService;
import budgeteventplanner.client.entity.Category;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class CategoryServiceImpl extends RemoteServiceServlet implements
		CategoryService {

	public CategoryServiceImpl() {
		try {
			ObjectifyService.register(Category.class);
		} catch (Exception e) {
		}
	}

	@Override
	public void createCategory(String categoryName) {
		Objectify ofy = ObjectifyService.begin();
		Category category = new Category.Builder(categoryName).build();
		ofy.put(category);
	}

	@Override
	public ArrayList<Category> getAllCategory() {
		Objectify ofy = ObjectifyService.begin();
		Query<Category> q = ofy.query(Category.class);

		ArrayList<Category> entities = new ArrayList<Category>();

		// Loop the query results and add to the array
		for (Category fetched : q) {
			entities.add(fetched);
		}
		return entities;
	}

	@Override
	public String getCategoryName(String categoryId) {
		Objectify ofy = ObjectifyService.begin();
		Category category = ofy.get(new Key<Category>(Category.class,
				categoryId));
		return category.getName();
	}

}
