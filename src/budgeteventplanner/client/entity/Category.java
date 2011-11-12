package budgeteventplanner.client.entity;

public class Category
{
	
	String name;
	
	
	public static class Builder
	{
		private Category category;
		/*------------------------------------------------------*/
		public Builder(Event event) {
			try {
				this.category = (Category) category.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*------------------------------------------------------*/

		public Builder(String name)
		{
			this.category.name = name;

		}
		/*---------------------------------------------------*/

	}
	

	
}
