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
    
    DockPanel dpanelCharts = new DockPanel();
    HorizontalPanel hpanelPieChart = new HorizontalPanel();    
    HorizontalPanel hpanelBarChart = new HorizontalPanel();
    HorizontalPanel hpanelGauges = new HorizontalPanel();
    
    hpanelPieChart.clear();
    hpanelPieChart.add(new BudgetPieChart(budgetId));
    dpanelCharts.add(hpanelPieChart, DockPanel.NORTH);
    
    hpanelBarChart.clear();
    hpanelBarChart.add(new BudgetBarChart(budgetId));
    dpanelCharts.add(hpanelBarChart, DockPanel.CENTER);
    
    hpanelGauges.clear();
    hpanelGauges.add(new BudgetGaugeChart(budgetId));
    dpanelCharts.add(hpanelGauges, DockPanel.SOUTH);
    
    dpanelMain.add(vpanelTable, DockPanel.WEST);
    dpanelMain.add(dpanelCharts, DockPanel.EAST);
    initWidget(dpanelMain);
  }
}
