package budgeteventplanner.server;

import java.util.List;

import budgeteventplanner.client.CategoryService;
import budgeteventplanner.client.entity.Category;

import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class CategoryServiceImpl extends RemoteServiceServlet implements
		CategoryService {

	static {
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
	public List<Category> getAllCategory() {
		Objectify ofy = ObjectifyService.begin();
		Query<Category> q = ofy.query(Category.class);

		List<Category> entities = Lists.newArrayList();

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
