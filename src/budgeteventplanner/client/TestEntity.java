package budgeteventplanner.client;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class TestEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private String name;
	private String address;
	private Integer limit;
	
    public TestEntity(){}
    
	public Integer getLimit() {
		return this.limit;
	}
	
	@Override
	public String toString() {
	  return name + "\t" + address + "\t" + Integer.toString(limit);
	}
	public static class Builder {
		TestEntity entity;

		public Builder(String name) {
			this.entity.name = name;
		}

		public Builder setAddress(String address) {
			this.entity.address = address;
			return this;
		}

		public Builder setLimit(Integer limit) {
			this.entity.limit = limit;
			return this;
		}

		public TestEntity build() {
			return entity;
		}
	}
}
