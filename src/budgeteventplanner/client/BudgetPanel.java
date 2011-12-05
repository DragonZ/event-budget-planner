package budgeteventplanner.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BudgetPanel extends Composite{

  public BudgetPanel(String budgetId) {
    DockPanel dpanelMain = new DockPanel();
    VerticalPanel vpanelTable = new VerticalPanel();
    vpanelTable.clear();
    vpanelTable.add(new BudgetExLmTable(budgetId));
    vpanelTable.add(new BudgetDetailsTable(budgetId));
    
    DockPanel dpanelCharts = new DockPanel();
    HorizontalPanel hpanePieBarChart = new HorizontalPanel();    
    HorizontalPanel hpanelGauges = new HorizontalPanel();
    
    hpanePieBarChart.clear();
    hpanePieBarChart.add(new BudgetPieChart(budgetId));
    hpanePieBarChart.add(new BudgetBarChart(budgetId));
    dpanelCharts.add(hpanePieBarChart, DockPanel.NORTH);    
    
    hpanelGauges.clear();
    hpanelGauges.add(new BudgetGaugeChart(budgetId));
    dpanelCharts.add(hpanelGauges, DockPanel.SOUTH);
    
    dpanelMain.add(vpanelTable, DockPanel.WEST);
    dpanelMain.add(dpanelCharts, DockPanel.EAST);
    initWidget(dpanelMain);
  }
}
