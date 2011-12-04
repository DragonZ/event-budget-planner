package budgeteventplanner.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class BudgetPanel extends Composite{

  public BudgetPanel(String budgetId) {
    HorizontalPanel hpanelMain = new HorizontalPanel();
    initWidget(hpanelMain);
  }
}
