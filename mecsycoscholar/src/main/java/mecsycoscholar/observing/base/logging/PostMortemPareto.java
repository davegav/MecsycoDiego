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


public class PostMortemPareto extends JFrame {
	
	
	public PostMortemPareto(double runs, String basePath) {
        initUI(runs, basePath);
    }

    private void initUI(double runs, String basePath) {

        XYDataset dataset = createDataset(runs, basePath);
        String[] indicators = new String[]{"Comfort", "Consum"};
        
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        
        add(chartPanel);

        pack();
        setTitle("Comparison cases");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private XYDataset createDataset(double runs, String basePath) {
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	for (double i=0.0; i<runs; i++) {
    		try {
				 String csvFilename = basePath + Double.toString(i+1.0) + ".csv";
			     CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
			     String[] headers = csvReader.readNext();
			     String[] col = null;
			     XYSeries seriesCombine = new XYSeries("Case "+ Double.toString(i+ 1.0));
			     double lastComfort = 0.0;
			     double lastConsum = 0.0;
			     
			     while ((col = csvReader.readNext()) != null) 
			     {
			    	 String[] row = col[0].split(";");
			    	 lastComfort = Double.parseDouble(row[1]);
			    	 lastConsum = Double.parseDouble(row[2]);
			     }
			     seriesCombine.add(lastComfort, lastConsum);
		         csvReader.close();
		         dataset.addSeries(seriesCombine);		         
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
        return dataset;
    }

    private JFreeChart createChart(final XYDataset dataset) {
    	//createScatterPlot(String title, String xAxisLabel, String yAxisLabel, XYDataset dataset)

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Pareto",
                "Comfort",
                "Consum",
                dataset
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

        chart.setTitle(new TextTitle("Pareto chart")
        );

        return chart;
    }


}
