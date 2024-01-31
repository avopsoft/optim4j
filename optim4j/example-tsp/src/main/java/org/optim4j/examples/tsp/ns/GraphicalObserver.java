package org.optim4j.examples.tsp.ns;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.optim4j.ns.Agent;
import org.optim4j.ns.Observer;

public class GraphicalObserver extends JFrame implements Observer {

	private JFreeChart chart;

	private XYSeriesCollection dataset = new XYSeriesCollection();

	public GraphicalObserver(String plotSubject, String xAxisLabel, String yAxisLabel) {
		super(plotSubject);

		JPanel chartPanel = createChartPanel(plotSubject, xAxisLabel, yAxisLabel);
		add(chartPanel, BorderLayout.CENTER);

		setSize(640, 480);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setVisible(true);
	}

	private static class Point {

		private double x;

		private double y;

		public Point(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + " )";
		}
	}

	public void addDataPoint(String graphName, int generation, double fitness) {
		addDataPoint(graphName, new Point(generation, fitness));
		setVisible(true);
	}

	public void saveAsImage(String filePath) {
		try {
			ChartUtilities.saveChartAsJPEG(new File(filePath), chart, 1920, 1080);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addDataPoint(String graphName, Point p) {
		XYSeries series = null;
		try {
			series = dataset.getSeries(graphName);
		} catch (Exception e) {
			series = new XYSeries(graphName);
			dataset.addSeries(series);
		}
		series.add(p.x, p.y);
	}

	private JPanel createChartPanel(String chartTitle, String xAxisLabel, String yAxisLabel) {

		boolean showLegend = true;
		boolean createURL = false;
		boolean createTooltip = false;

		chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL,
				showLegend, createTooltip, createURL);
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		plot.setRenderer(renderer);

		return new ChartPanel(chart);

	}

	@Override
	public void notify(Agent agent, int generation) {
		addDataPoint("TSP Optimization", new Point(generation, Math.abs(agent.evaluate())));
		setVisible(true);
	}

}
