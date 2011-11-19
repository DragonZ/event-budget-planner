package budgeteventplanner.client.entity;

import java.io.Serializable;

import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class Category implements Serializable {
	@Id
	private String categoryId;
	private String name;

	public Category(){}
	
	public static class Builder
	{
		private Category category;
		
		public Builder(Category category) {
			this.category = category;
		}

		public Builder(String name)
		{
			this.category = new Category();
			this.category.categoryId = UUID.randomUUID();
			this.category.name = name;
		}
		
		public Category build()
		{
			return this.category;
		}
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getName() {
		return name;
	}

}
