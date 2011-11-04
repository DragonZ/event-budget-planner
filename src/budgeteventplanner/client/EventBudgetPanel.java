/*
 CS307 Software Engineering, Event Budget Management
 Yuan Xia
 Computer Science, Purdue University
 */


package budgeteventplanner.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class EventBudgetPanel extends VerticalPanel implements EntryPoint {

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		
		//HorizontalSplitPanel event_h_panel=new HorizontalSplitPanel();
		HorizontalPanel event_h_panel=new HorizontalPanel();
		event_h_panel.setBorderWidth(1);
		event_h_panel.setSize("100%", "500px");
		VerticalPanel event_right_v_panel=new VerticalPanel();
		Button event_add=new Button("Add");
		event_right_v_panel.add(event_add);
		
		final FlexTable event_table=new FlexTable();
		event_table.setSize("500px", "100%");
		event_table.setText(0, 0, "Event Name");
		event_table.getCellFormatter().setWidth(0, 0, "20%");
		event_table.setText(0, 1, "Modify");
		event_table.getCellFormatter().setWidth(0, 1, "100%");
		event_table.setText(0, 2, "Delete");
		event_table.getCellFormatter().setWidth(0, 2, "20%");
		event_table.setWidget(1, 0, new Label("Event_Name_From_Server"));
		event_table.getCellFormatter().setWidth(1, 0, "70%");
		Button event_mod=new Button("Modify");
		event_table.setWidget(1, 1, event_mod);
		event_table.getCellFormatter().setWidth(1, 1, "20%");
		event_table.setWidget(1, 2, new Button("Delete"));
		
		event_right_v_panel.add(event_table);
		//event_h_panel.setLeftWidget(new HTML("left"));
		//event_h_panel.setRightWidget(event_right_v_panel);
		//event_h_panel.setSplitPosition("20%");
		Label l1=new Label("Left Place Holder");
		l1.setWidth("100px");
		event_h_panel.add(l1);
		event_h_panel.add(event_right_v_panel);
		
		
		event_add.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				event_pop_up();
			}
		});
		
		event_mod.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				event_pop_up();
			}
		});
		
		
		
		
		
		//HorizontalSplitPanel budget_h_panel=new HorizontalSplitPanel();
		HorizontalPanel budget_h_panel=new HorizontalPanel();
		budget_h_panel.setBorderWidth(1);
		budget_h_panel.setSize("100%", "500px");
		VerticalPanel budget_right_v_panel=new VerticalPanel();
		
		FlexTable budget_table=new FlexTable();

		budget_table.setSize("500px", "100%");
		budget_table.setText(0, 0, "Budget Name");
		budget_table.getCellFormatter().setWidth(0, 0, "20%");
		budget_table.setText(0, 1, "Modify");
		budget_table.getCellFormatter().setWidth(0, 1, "100%");
		budget_table.setText(0, 2, "Delete");
		budget_table.getCellFormatter().setWidth(0, 2, "20%");
		budget_table.setWidget(1, 0, new Label("Budget_Name_From_Server"));
		budget_table.getCellFormatter().setWidth(1, 0, "70%");
		Button budget_mod=new Button("Modify");
		budget_table.setWidget(1, 1, budget_mod);
		budget_table.getCellFormatter().setWidth(1, 1, "20%");
		budget_table.setWidget(1, 2, new Button("Delete"));
		
//		Grid g=new Grid(3,3);
//		for (int i=0;i<3;i++)
//		{
//			for (int j=0;j<3;j++)
//				{
//					g.setText(i, j, ""+i+","+j+"");
//					g.getCellFormatter().setWidth(i, j, "100px");
//				}
//		}
		Button budget_add=new Button("Add");
		budget_right_v_panel.add(budget_add);
		budget_right_v_panel.add(budget_table);
//		budget_h_panel.setLeftWidget(new Label("Left"));
//		budget_h_panel.setRightWidget(budget_right_v_panel);
//		budget_h_panel.setSplitPosition("20%");
		Label l2=new Label("Left Place Holder");
		l2.setWidth("100px");
		budget_h_panel.add(l2);
		budget_h_panel.add(budget_right_v_panel);
		
		budget_add.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				budget_pop_up(0);
			}
		});
		budget_mod.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				budget_pop_up(1);
			}
		});
		
		TabPanel t_panel=new TabPanel();
		t_panel.setAnimationEnabled(true);
		t_panel.add(event_h_panel,"Event");
		t_panel.add(budget_h_panel,"Budget");
		RootPanel.get("XiaYuan").add(t_panel);
		t_panel.setSize("100%", "100%");
		t_panel.selectTab(0);
		//budget_h_panel.setSplitPosition("20%");
		//event_h_panel.setSplitPosition("20%");
		t_panel.selectTab(1);
		//budget_h_panel.setSplitPosition("20%");
		//event_h_panel.setSplitPosition("20%");
		t_panel.selectTab(0);
		

	}
	public void event_pop_up()
	{
		final DialogBox d=new DialogBox();
		
		VerticalPanel panel=new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		
		FlexTable table=new FlexTable();
		table.setWidget(0, 0, new Label("Event Title:"));
		TextBox event_title=new TextBox();
		table.setWidget(0, 1, event_title);
		
		table.getColumnFormatter().setWidth(0, "100px");
		table.getColumnFormatter().setWidth(1, "150px");
		
		table.setWidget(1, 0, new Label("Event Fee:"));
		TextBox event_fee=new TextBox();
		table.setWidget(1, 1, event_fee);
		
		table.setWidget(2, 0, new Label("Start Date:"));
		DatePicker start_date=new DatePicker();
		table.setWidget(2, 1, start_date);
		
		table.setWidget(3, 0, new Label("End Date:"));
		DatePicker end_date=new DatePicker();
		table.setWidget(3, 1, end_date);
		
		table.setWidget(4, 0, new Label("Location:"));
		TextArea location=new TextArea();
		table.setWidget(4, 1, location);
		
		table.setWidget(5, 0, new Label("Contact Email:"));
		TextBox contact_email=new TextBox();
		table.setWidget(5, 1, contact_email);
		
		table.setWidget(6, 0, new Label("Category:"));
		ListBox category=new ListBox();
		category.addItem("1");
		category.addItem("2");
		table.setWidget(6, 1, category);
		
		table.setWidget(7, 0, new Label("Event Agenda:"));
		TextArea event_agenda=new TextArea();
		table.setWidget(7, 1, event_agenda);
		
		Button close_d=new Button("Close");
		Button save_d=new Button("Save");
		
		panel.add(close_d);
		panel.add(table);
		panel.add(save_d);
		
		d.setWidget(panel);
		d.setAnimationEnabled(true);
		d.center();
		d.show();
		
		//hp=new HorizontalPanel();
		
		
		
		
		close_d.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				d.hide();
			}
		});
		save_d.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				d.hide();
			}
		});
	}
	public void budget_pop_up(int flag)
	{
		final DialogBox d=new DialogBox();
		VerticalPanel panel=new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		
		Button add_row=new Button("Add Entry");
		//Button delete=new Button("Delete Selected");
		
		
		
		FlexTable upper_table=new FlexTable();
		upper_table.setWidget(0, 0, new Label("Budget Name:"));
		TextBox budget_name=new TextBox();
		upper_table.setWidget(0, 1, budget_name);
		upper_table.setWidget(0, 2, add_row);
		
		upper_table.getColumnFormatter().setWidth(0, "100px");
		upper_table.getColumnFormatter().setWidth(1, "100px");
		upper_table.getColumnFormatter().setWidth(2, "100px");
		
		if (flag==0)
		{
			upper_table.setWidget(1, 0, new Label("Event List:"));
			ListBox list=new ListBox();
			list.addItem("Event 1 From Server");
			list.addItem("Event 2 from Server");
			upper_table.setWidget(1, 1, list);
		}
		
		Button close_d=new Button("Close");
		panel.add(close_d);
		panel.add(upper_table);

		final FlexTable table=new FlexTable();
		
		table.setText(0, 0, "Description");
		table.getCellFormatter().setWidth(0, 0, "500px");
		table.setText(0, 1, "Category");
		table.getCellFormatter().setWidth(0, 1, "300px");
		table.setText(0, 2, "Amount");
		table.getCellFormatter().setWidth(0, 2, "100px");
		table.setText(0, 3, "Delete");
		table.getCellFormatter().setWidth(0, 3, "100px");
		
//		TextBox tb1=new TextBox();
//		tb1.setWidth("200px");
//		table.setWidget(1, 0, tb1);
//		ListBox lb=new ListBox();
//		lb.addItem("1000");
//		lb.addItem("2000");
//		table.setWidget(1, 1, lb);
//		TextBox tb2=new TextBox();
//		tb2.setWidth("20px");
//		table.setWidget(1, 2, tb2);
//		table.setWidget(1, 3, new Button("Delete"));
		
		
		Button save_d=new Button("Save");

		panel.add(table);
		panel.add(save_d);
		
		d.setWidget(panel);
		d.setAnimationEnabled(true);
		
		d.center();
		d.show();
		
		add_row.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				final int row=table.getRowCount();
				table.setWidget(row, 0, new TextBox());
				ListBox lb_temp=new ListBox();
				lb_temp.addItem("1000");
				lb_temp.addItem("2000");
				table.setWidget(row, 1, lb_temp);
				TextBox tb_temp=new TextBox();
				tb_temp.setWidth("20px");
				table.setWidget(row, 2, tb_temp);
				final Button del=new Button("Delete");
				table.setWidget(row, 3, del);
				
				del.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event)
					{
						//table.removeRow(row);
						int temp_row=1;
						int total_row=table.getRowCount();
						for (int i=1;i<total_row;i++)
						{
							if (table.getWidget(i, 3).equals(del))
							{
								temp_row=i;
								break;
							}
						}
						table.removeRow(temp_row);
					}
				});
			}
		});
		
		
		close_d.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event)
			{
				d.hide();
			}
		});
		save_d.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				d.hide();
			}
		});
		
	}
	

}
