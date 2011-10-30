package budgeteventplanner.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BudgetPanel implements EntryPoint {

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		
		HorizontalSplitPanel event_h_panel=new HorizontalSplitPanel();
		event_h_panel.setSize("100%", "500px");
		VerticalPanel event_right_v_panel=new VerticalPanel();
		event_right_v_panel.add(new Button("Add"));
		event_right_v_panel.add(new HTML("place_holder"));
		event_h_panel.setLeftWidget(new HTML("left"));
		event_h_panel.setRightWidget(event_right_v_panel);
		event_h_panel.setSplitPosition("20%");
		
		
		
		
		
		HorizontalSplitPanel budget_h_panel=new HorizontalSplitPanel();
		budget_h_panel.setSize("100%", "500px");
		VerticalPanel budget_right_v_panel=new VerticalPanel();
		
		FlexTable budget_table=new FlexTable();
		//budget_table.insertRow(0);
		//budget_table.getRowFormatter().addStyleName(0, "FlexTable-Header");
		//budget_table.setText(0, 0, "ID");
		//budget_table.setText(0, 0, "Title");
		//budget_table.setText(0, 2, "Action");
		
//		budget_table.setText(0, 0, "ID");
//		budget_table.getCellFormatter().setWidth(0, 0, "10%");
//		budget_table.setText(0, 1, "Title");
//		budget_table.getCellFormatter().setWidth(0, 1, "70%");
//		budget_table.setText(0, 2, "Action");
//		budget_table.getCellFormatter().setWidth(0, 2, "20%");
//		budget_table.setText(1, 0, "12345");
//		budget_table.getCellFormatter().setWidth(1, 0, "10%");
//		Hyperlink hl=new Hyperlink();
//		hl.setText("title_text");
//		budget_table.setWidget(1, 1, hl);
//		budget_table.getCellFormatter().setWidth(1, 1, "70%");
//		budget_table.setWidget(1, 2, new Button("Modify"));
//		budget_table.getCellFormatter().setWidth(1, 2, "20%");
//		budget_table.setSize("500px", "100%");
		
		Grid g=new Grid(3,3);
		for (int i=0;i<3;i++)
		{
			for (int j=0;j<3;j++)
				{
					g.setText(i, j, ""+i+","+j+"");
					g.getCellFormatter().setWidth(i, j, "100px");
				}
		}
		
		budget_right_v_panel.add(new Button("Add"));
		budget_right_v_panel.add(g);
		budget_h_panel.setLeftWidget(new HTML("left"));
		budget_h_panel.setRightWidget(budget_right_v_panel);
		budget_h_panel.setSplitPosition("20%");
		
		
		
		TabPanel t_panel=new TabPanel();
		t_panel.setAnimationEnabled(true);
		t_panel.add(event_h_panel,"Event");
		t_panel.add(budget_h_panel,"Budget");
		RootPanel.get().add(t_panel);
		t_panel.setSize("100%", "100%");
		//Button test=new Button("test");
		t_panel.selectTab(0);
		budget_h_panel.setSplitPosition("20%");
		event_h_panel.setSplitPosition("20%");
		t_panel.selectTab(1);
		budget_h_panel.setSplitPosition("20%");
		event_h_panel.setSplitPosition("20%");
		t_panel.selectTab(0);
		

	}

}
