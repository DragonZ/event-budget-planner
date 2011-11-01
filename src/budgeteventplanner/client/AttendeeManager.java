package budgeteventplanner.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class AttendeeManager implements EntryPoint {

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		
		
		HorizontalPanel hPanel=new HorizontalPanel();
		hPanel.setSpacing(50);
		RootPanel.get("attendees").add(hPanel);
		hPanel.setSize("100%", "100%");
		

		
		TextArea infoBox=new TextArea();
		infoBox.setSize("500px","500px");
		infoBox.setReadOnly(true);
		infoBox.setText("here");
		
		//TreeItem hTree =new TreeItem();
		TreeItem root=new TreeItem("Attendees");
		Label [] attendees= new Label[5];
		for(int i=0;i<5;i++)
		{
			attendees[i]=new Label("attendees"+i);
			root.addItem(attendees[i]);
			
		}
		
		Tree hTree=new Tree();
		hTree.addItem(root);
		
		hPanel.	add(hTree);
		hPanel.	add(infoBox);

		
		

		
		
		
		
	}

}
