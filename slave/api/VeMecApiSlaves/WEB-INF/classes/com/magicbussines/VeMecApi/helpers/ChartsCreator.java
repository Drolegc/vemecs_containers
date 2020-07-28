package com.magicbussines.VeMecApi.helpers;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.magicbussines.VeMecApi.VeMecApiApplication;

public class ChartsCreator {
	
	public void OneLineChart(String docuId, String title,String xType,String yType,List lineone, int start, int end) throws IOException {
		XYSeries set1 = new XYSeries(title);
		for (int i = 0; i < lineone.size(); i++) {
			set1.add(i, (double) lineone.get(i));			
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(set1);
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
		chart.setBorderVisible(true);
		chart.setBorderPaint(Color.black); 	
		
		TextTitle title1 = new TextTitle();
		title1.setFont( new java.awt.Font( "SansSerif", java.awt.Font.BOLD, 12 ) );
		chart.setTitle( title );
		
		XYPlot plot = (XYPlot) chart.getPlot();  
	    //To change the lower bound of X-axis
	    NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
	    xAxis.setRange(start, end);    
	   
		
		int width = 580; /* Width of the image */
		int height = 200; /* Height of the image */
		
		File lineChart = new File(VeMecApiApplication.getChartPath()+docuId+"-"+title+"start"+start+"-end"+end+"-Graph.jpeg");
		ChartUtils.saveChartAsJPEG(lineChart ,chart, width ,height);
	}

	public void TwoLineChart(String docuId, String title,String xType,String yType,List lineone,List linetwo, int start, int end) throws IOException {
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
		chart.setBorderVisible(true);
		chart.setBorderPaint(Color.black); 
		
		TextTitle title1 = new TextTitle();
		title1.setFont( new java.awt.Font( "SansSerif", java.awt.Font.BOLD, 12 ) );
		chart.setTitle( title );
		
		XYPlot plot = (XYPlot) chart.getPlot(); 
	    //To change the lower bound of X-axis
	    NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
	    xAxis.setRange(start, end);
		int width = 580; /* Width of the image */
		int height = 200; /* Height of the image */
		File lineChart = new File(VeMecApiApplication.getChartPath()+docuId+"-"+title+"start"+start+"-end"+end+"-Graph.jpeg");
		ChartUtils.saveChartAsJPEG(lineChart ,chart, width ,height);
		
		
		}
	}

