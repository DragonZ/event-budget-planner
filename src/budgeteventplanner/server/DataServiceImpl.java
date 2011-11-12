package budgeteventplanner.server;

import java.util.ArrayList;

import budgeteventplanner.client.DataService;
import budgeteventplanner.client.TestEntity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {
	public DataServiceImpl() {
		ObjectifyService.register(TestEntity.class);
	}

	@Override
	public void putEntity(String name, String address, Integer limit) {
		
		Objectify ofy = ObjectifyService.begin();
		TestEntity ent = new TestEntity.Builder(name).setAddress(address).setLimit(limit).build();
		ofy.put(ent);
	}

	@Override
	public ArrayList<TestEntity> getEntityByName(String name) {
		
		Objectify ofy = ObjectifyService.begin();
		Query<TestEntity> q = ofy.query(TestEntity.class).filter("name",
				name);
		ArrayList<TestEntity> entities = new ArrayList<TestEntity>();

		//Loop the query results and add to the array
		for (TestEntity fetched : q) {
			entities.add(fetched);
		}
		return entities;
	}

	@Override
	public ArrayList<TestEntity> getEntityByLimit(Integer limit) {
		
		Objectify ofy = ObjectifyService.begin();
		Query<TestEntity> q = ofy.query(TestEntity.class).filter("limit",
				limit);
		ArrayList<TestEntity> entities = new ArrayList<TestEntity>();

		//Loop the query results and add to the array
		for (TestEntity fetched : q) {
			entities.add(fetched);
		}
		return entities;
	}
}
