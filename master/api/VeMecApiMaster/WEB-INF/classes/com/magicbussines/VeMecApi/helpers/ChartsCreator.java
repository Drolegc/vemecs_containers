package com.magicbussines.VeMecApi.helpers;

import java.io.*;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartsCreator {

	public void TwoLineChart(String docuId, String title,String xType,String yType,List lineone,List linetwo) throws IOException {
		

		XYSeries set1 = new XYSeries(title+" Min");
		XYSeries set2 = new XYSeries(title+" Max");
		for (int i = 0; i < lineone.size(); i++) {
			set1.add(i, (double) lineone.get(i));
		}	
		
		for (int i = 0; i < linetwo.size(); i++) {
			set2.add(i, (double) linetwo.get(i));
		}	
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(set1);
		dataset.addSeries(set2);
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Grafico de "+title, // chart title
				xType, // x axis label
				yType, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL,
				true, // include legend
				true, // tooltips
				false // urls
				);
	
		int width = 580; /* Width of the image */
		int height = 400; /* Height of the image */
		File lineChart = new File("F:/CURE/JavaEEE/"+docuId+"-"+title+"-Graph.jpeg");
		ChartUtils.saveChartAsJPEG(lineChart ,chart, width ,height);
		
		
		}
	}

