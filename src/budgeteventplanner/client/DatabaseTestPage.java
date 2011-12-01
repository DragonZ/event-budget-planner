package budgeteventplanner.client;

import java.util.List;

import budgeteventplanner.shared.Pair;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

public class DatabaseTestPage implements EntryPoint {

  private final DataServiceAsync dataService = GWT.create(DataService.class);

  // private final UserServiceAsync userService = GWT.create(UserService.class);

  private final EventServiceAsync eventService = GWT.create(EventService.class);

  private final BudgetServiceAsync budgetService = GWT.create(BudgetService.class);

  private final List<String> categoryName = Lists.newArrayList();

  private final List<Double> limitList = Lists.newArrayList();

  final Boolean loaded = new Boolean(true);
  @Override
  public void onModuleLoad() {
    // Create the popup dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Remote Procedure Call");
    dialogBox.setAnimationEnabled(true);
    final Button closeButton = new Button("Close");
    // We can set the id of a widget by accessing its Element
    closeButton.getElement().setId("closeButton");
    final Label textToServerLabel = new Label();
    final HTML serverResponseLabel = new HTML();
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.addStyleName("dialogVPanel");
    dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
    dialogVPanel.add(textToServerLabel);
    dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
    dialogVPanel.add(serverResponseLabel);
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);

    closeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        dialogBox.hide();
      }
    });

    Runnable onLoadCallback = new Runnable() {
      public void run() {
        Panel panel = RootPanel.get();

        // Create a pie chart visualization.
        PieChart pie = new PieChart(createTable(), createOptions());

        pie.addSelectHandler(createSelectHandler(pie));
        panel.add(pie);
      }
    };

    // Load the visualization api, passing the onLoadCallback to be called
    // when loading is done.
    VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);
  }

  private Options createOptions() {
    Options options = Options.create();
    options.setWidth(400);
    options.setHeight(240);
    options.setTitle("My Daily Activities");
    return options;
  }

  private SelectHandler createSelectHandler(final PieChart chart) {
    return new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        String message = "";

        // May be multiple selections.
        JsArray<Selection> selections = chart.getSelections();

        for (int i = 0; i < selections.length(); i++) {
          // add a new line for each selection
          message += i == 0 ? "" : "\n";

          Selection selection = selections.get(i);

          if (selection.isCell()) {
            // isCell() returns true if a cell has been selected.

            // getRow() returns the row number of the selected cell.
            int row = selection.getRow();
            // getColumn() returns the column number of the selected cell.
            int column = selection.getColumn();
            message += "cell " + row + ":" + column + " selected";
          } else if (selection.isRow()) {
            // isRow() returns true if an entire row has been selected.

            // getRow() returns the row number of the selected row.
            int row = selection.getRow();
            message += "row " + row + " selected";
          } else {
            // unreachable
            message += "Pie chart selections should be either row selections or cell selections.";
            message += "  Other visualizations support column selections as well.";
          }
        }

        Window.alert(message);
      }
    };
  }

  private AbstractDataTable createTable() {
    
    DataTable data = DataTable.create();
    budgetService.getLimitsByBudgetId("7C1A90FE-A1A2-41A5-8A48-AB7746201829",
        new AsyncCallback<List<Pair<String, Double>>>() {
          @Override
          public void onFailure(Throwable caught) {
          }

          @Override
          public void onSuccess(List<Pair<String, Double>> result) {
            for (Pair<String, Double> pair : result) {
              Window.alert("categoryName:" + pair.getA());
              Window.alert("limit:" + pair.getB());
              categoryName.add(pair.getA());
              limitList.add(pair.getB());
            }
          }
        });
    
      data.addColumn(ColumnType.STRING, "Category");
      data.addColumn(ColumnType.NUMBER, "Limit");
    
      Window.alert("categoryNameSize:" + categoryName.size());
      data.addRows(categoryName.size());
      
      for (int i = 0; i < categoryName.size(); i++) {
        //Window.alert("categoryName:" + categoryName.get(i));
        //Window.alert("limit:" + limitList.get(i));
        data.setValue(i, 0, categoryName.get(i));
        data.setValue(i, 1, limitList.get(i));
      }
    return data;
  }
}
