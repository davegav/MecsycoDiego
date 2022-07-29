/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta, V. Elvinger, T. Paris, Y. Presse,
J. Vaubourg, B.Vouillaume, Copyright (c) Université de Lorraine &
INRIA, Affero GPL license v3, 2014-2015

This software was developed with the collaboration of EDF

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package mecsycoscholar.observing.base.logging;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.BasicStroke;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.fasterxml.jackson.core.JsonProcessingException;

import au.com.bytecode.opencsv.CSVReader;


public class PostMortemCsvGraph extends JFrame {
	
	
	public PostMortemCsvGraph(double runs, String basePath, int indicator, String[] indicators) {
        initUI(runs, basePath, indicator, indicators);
    }

    private void initUI(double runs, String basePath, int indicator, String[] indicators) {

        XYDataset dataset = createDataset(runs, basePath, indicator);
        //XYDataset datasets[] = createDataset(runs, basePath, indicator);
        //String[] indicators = new String[]{"Comfort", "Consum"};
        
        JFreeChart chart = createChart(dataset, indicators[indicator]);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        
        add(chartPanel);

        pack();
        setTitle("Comparison cases");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private XYDataset createDataset(double runs, String basePath, int indicator) {
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	//XYDataset[] datasets = new XYDataset[2];
    	//XYSeriesCollection datasetComfort = new XYSeriesCollection();
    	//XYSeriesCollection datasetConsum = new XYSeriesCollection();
    	for (double i=0.0; i<runs; i++) {
    		try {
				 String csvFilename = basePath + Double.toString(i+1.0) + ".csv";
			     CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
			     String[] headers = csvReader.readNext();
			     String[] col = null;
			     XYSeries seriesNew = new XYSeries("Case "+ Double.toString(i+ 1.0));
			     //XYSeries seriesComfort = new XYSeries("Case "+ Double.toString(i+ 1.0));
			     //XYSeries seriesConsum = new XYSeries("Case "+ Double.toString(i+ 1.0));
			     while ((col = csvReader.readNext()) != null) 
			     {
			    	 String[] row = col[0].split(";");
			    	 if (row.length == 3) {  // This shouldn't be necessary but if the code is run without debug it fails 
			    		 seriesNew.add(Double.parseDouble(row[0]), Double.parseDouble(row[indicator+1]));
				         //seriesComfort.add(Double.parseDouble(row[0]), Double.parseDouble(row[1]));
				         //seriesConsum.add(Double.parseDouble(row[0]), Double.parseDouble(row[2]));
			    	 }else {
			    		 System.out.println("Case "+ Double.toString(i+ 1.0));
			    		 System.out.println(col[0]);
			    		 System.out.println(row.length);
			    	 }
			     }
			     csvReader.close();
			     dataset.addSeries(seriesNew);
			     //datasetComfort.addSeries(seriesComfort);
			     //datasetConsum.addSeries(seriesConsum);
		         
			 }
			 catch(ArrayIndexOutOfBoundsException ae)
			 {
			     System.out.println(ae+" : error here");
			     ae.printStackTrace();
			 }catch (FileNotFoundException e) 
			 {
			     System.out.println("asd");
			     e.printStackTrace();
			 } catch (IOException e) {
			     System.out.println("");
			     e.printStackTrace();
			 }
	    }
	    //datasets[0] = datasetComfort;
	    //datasets[1] = datasetConsum;
        //return datasets;
    	return dataset;
    }

    private JFreeChart createChart(final XYDataset dataset, String indicator) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                indicator,
                "Time",
                indicator,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        

        XYPlot plot = chart.getXYPlot();

//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//
//        renderer.setSeriesPaint(0, Color.RED);
//        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
//        renderer.setSeriesPaint(1, Color.BLUE);
//        renderer.setSeriesStroke(1, new BasicStroke(1.0f));
//
//        plot.setRenderer(renderer);

        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Comparison "+ indicator + " cases")
        );

        return chart;
    }


}
