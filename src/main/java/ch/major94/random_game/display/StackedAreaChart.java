package ch.major94.random_game.display;

import java.awt.Color;
import java.awt.Dimension;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.DefaultTableXYDataset;

public class StackedAreaChart extends ApplicationFrame {

	private DefaultTableXYDataset dataset;

	private static final long serialVersionUID = -7075059938932563617L;

	public StackedAreaChart(String applicationTitle , String chartTitle, String xAxis, String yAxis) {
		super(applicationTitle);
		dataset = new DefaultTableXYDataset();

		final JFreeChart chart = ChartFactory.createStackedXYAreaChart(
				chartTitle, xAxis, yAxis, dataset, PlotOrientation.VERTICAL, true, true, false);

		chart.setBackgroundPaint(Color.white);

		XYPlot plot = chart.getXYPlot();
		plot.setForegroundAlpha(0.5f);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);
	}

	public void addData(String[] types, int gen) {

		dataset.removeAllSeries();

		SeriesElement.reset();	
		for (String t : types) {
			SeriesElement.count(t);
		}	
		for (SeriesElement se : SeriesElement.list) {
			se.addToData(gen);
			dataset.addSeries(se);
		}
	}
}