package budgeteventplanner.client.entity;

public class Category
{
	private String name;

	public static class Builder
	{
		private Category category = new Category();
		
		public Builder(Category category) {
			this.category = category;
		}

		public Builder(String name)
		{
			this.category.name = name;
		}
		public Category build()
		{
			return this.category;
		}
	}

	public String getName() {
		return name;
	}
}
