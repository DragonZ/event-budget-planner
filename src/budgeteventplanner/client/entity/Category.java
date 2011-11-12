package budgeteventplanner.client.entity;

public class Category
{
	private String name;

	public static class Builder
	{
		private Category category;
		
		public Builder(Category category) {
			this.category = category;
		}

		public Builder(String name)
		{
			this.category.name = name;
		}
	}

	public String getName() {
		return name;
	}
}
