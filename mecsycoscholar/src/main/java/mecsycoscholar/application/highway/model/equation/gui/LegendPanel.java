/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta, M. Urbanski Copyright
(c) Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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

package mecsycoscholar.application.highway.model.equation.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JPanel;

import mecsycoscholar.application.highway.model.equation.CellModelArtifact;


public class LegendPanel extends JPanel{
	
// Version
	private static final long serialVersionUID = 1L;

// Implementation
	private final CellModelArtifact model;

// Creation
	public LegendPanel(CellModelArtifact aCellModel){
		super();
		this.setPreferredSize(new Dimension(200,100));
		model = aCellModel;
	}

// Other
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		Graphics2D g2=(Graphics2D)g;
		
		int x=10;
		int y=10;
		
		
		// CAPTION
		
		g.setColor(Color.BLACK);
		//g.setFont(g.getFont().deriveFont(Font.BOLD));
		g.setFont(new Font("default", Font.BOLD, 14));
		g.drawString("Caption:",x,y);
		
		
		// SPEED
		y+=20;
		
		// Text
		g.setFont(new Font("default", Font.PLAIN, 12));
		g.drawString("Avg speed",x,y);

		x+=80;
		y-=10;
		// Gradient
		float hue_max_ratio=ACellStatePrinter.hue_max/255;
		
		Color color1 = Color.getHSBColor(0, ACellStatePrinter.saturation, ACellStatePrinter.bright);
		Color middle_color = Color.getHSBColor(hue_max_ratio/2, ACellStatePrinter.saturation, ACellStatePrinter.bright);
		Color color2 = Color.getHSBColor(hue_max_ratio, ACellStatePrinter.saturation, ACellStatePrinter.bright);
				
	    Paint gradientPaint = new GradientPaint(x, 0, color2, x+200, 0, middle_color);
	    g2.setPaint(gradientPaint);
	    g2.fillRect(x, y, 200, 10);
	        
	    gradientPaint = new GradientPaint(x+200, 0, middle_color, x+400, 0, color1);
	    g2.setPaint(gradientPaint);
	    g2.fillRect(x+200, y, 200, 10);
	    
	    // Graduations
	    
	    g.setColor(Color.BLACK);
	    g.drawLine(x,y,x,y+15);
	    g.drawString("0 km/h",x-2,y+15+12);
	    
	    g.drawLine(x+400,y,x+400,y+15);
	    g.drawString(model.getCell(0).getFreeSpeed()+" km/h",x-2+400,y+15+12);
	    
	    g.drawLine(x+200,y,x+200,y+15);
	    g.drawString(model.getCell(0).getFreeSpeed()/2+" km/h",x-2+200,y+15+12);
	    
	    
	    // DENSITY
	    
		y+=50;
		x=10;
		
		// Text
		g.drawString("Density",x,y);

		x+=80;
		y-=10;
		
		// Gradient
		
		color1 = Color.ORANGE;
		color2 = new Color(color1.getRed(),color1.getGreen(),color2.getBlue(),0);
				
	    gradientPaint = new GradientPaint(x, 0, color2, x+400, 0, color1);
	    g2.setPaint(gradientPaint);
	    g2.fillRect(x, y, 400, 10);
	    
	    // Graduation
	    
	    g.setColor(Color.BLACK);
	    
	    g.setColor(Color.BLACK);
	    g.drawLine(x,y,x,y+15);
	    g.drawString("0 veh/km/lane",x-2,y+15+12);
	    
	    g.drawLine(x+400,y,x+400,y+15);
	    g.drawString(model.getCell(0).getCriticalDensity()+" veh/km/lane",x-2+400,y+15+12);
	    
	    g.drawLine(x+200,y,x+200,y+15);
	    g.drawString(model.getCell(0).getCriticalDensity()/2+" veh/km/lane",x-2+200,y+15+12);
	    
	}
}
