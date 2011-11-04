package budgeteventplanner.shared.dto.entity;

import java.util.Date;
import java.util.UUID;

import budgeteventplanner.shared.dto.entity.Event.Builder;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

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
