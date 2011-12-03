package budgeteventplanner.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Gauge;

public class SingleGaugeChart extends Composite {
	private final DecoratorPanel panelTop = new DecoratorPanel();
	private final VerticalPanel panel = new VerticalPanel();
	Gauge gaugeChart;

	public SingleGaugeChart(final String name, final Double actual, final Double limit, final Integer width, final Integer height) {
		super();
		panel.clear();
		final Button btnDraw = new Button("Draw SingleGaugeChart");
		btnDraw.setEnabled(true);
		panel.add(btnDraw);


		btnDraw.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Runnable onLoadCallback = new Runnable() {
					public void run() {
						try{
							panel.remove(gaugeChart);
						}catch(Exception e){}


						/*************************************************************/
						DataTable dataTable;

						Gauge.Options options = Gauge.Options.create();
						options.setWidth(width);
						options.setHeight(height);



						dataTable = DataTable.create();
						dataTable.addColumn(ColumnType.NUMBER, name);
						dataTable.addRow();
						dataTable.setValue(0, 0, actual);

						int max = limit.intValue();
						int red = Double.valueOf(max*0.9).intValue();
						int yellow = Double.valueOf(max*0.75).intValue();

						options.setGaugeRange(0, max);
						options.setGreenRange(0, yellow);
						options.setYellowRange(yellow, red);
						options.setRedRange(red, max);

						gaugeChart = new Gauge(dataTable, options);
						dataTable = null;
						panel.add(gaugeChart);

						/*************************************************************/
					}
				};
				VisualizationUtils.loadVisualizationApi(onLoadCallback, Gauge.PACKAGE);

			}
		});

		panelTop.add(panel);
		initWidget(panelTop);
	}

	private AbstractDataTable createTesetTable() {
		DataTable coreChartData = DataTable.create();
		coreChartData.addColumn(ColumnType.STRING, "Task");
		coreChartData.addColumn(ColumnType.NUMBER, "Hours per Day");
		coreChartData.addRows(2);
		coreChartData.setValue(0, 0, "Work");
		coreChartData.setValue(0, 1, 14);
		coreChartData.setValue(1, 0, "Sleep");
		coreChartData.setValue(1, 1, 10);
		return coreChartData;
	}

}
