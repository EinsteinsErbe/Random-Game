package ch.major94.random_game.display;

import org.jfree.chart.ChartPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChart extends ApplicationFrame {

	private static final long serialVersionUID = -5943181720402926737L;
	
	private XYSeries series;

	public LineChart( String applicationTitle , String chartTitle, String xAxis, String yAxis) {
		super(applicationTitle);

		series = new XYSeries("Data");
		series.add(0, 0);
		XYSeriesCollection dataset = new XYSeriesCollection(series);

		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxis,
				yAxis, dataset, PlotOrientation.VERTICAL, false, false, false);
		chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		chart.getXYPlot().getRangeAxis().setRange(0.0, 4.0);
		ChartPanel chartPanel = new ChartPanel( chart );
		chartPanel.setPreferredSize( new Dimension( 560 , 367 ) );
		setContentPane( chartPanel );
	}
	
	public void addData(double x, double y) {
		series.add(x, y);
	}
}