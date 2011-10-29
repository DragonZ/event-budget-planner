package budgeteventplanner.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

public class BudgetPanel implements EntryPoint {

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		HorizontalSplitPanel panel=new HorizontalSplitPanel();
		panel.addStyleName("budget split panel");
		
		panel.setLeftWidget(new HTML("left"));
		panel.setRightWidget(new HTML("right"));

	}

}
