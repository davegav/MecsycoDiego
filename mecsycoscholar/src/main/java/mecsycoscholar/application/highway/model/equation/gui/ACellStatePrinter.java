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
import java.awt.Graphics;

import javax.swing.JPanel;

import mecsycoscholar.application.highway.model.equation.Cell;


public class ACellStatePrinter extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final float hue_max=90;		
	public static final float bright= 0.9f;
	public static final float saturation=0.8f;
	
	private Cell cell;
	
	private int cell_number;
	
	public ACellStatePrinter(int width, int length, Cell aCell, int number){
		super();
		this.setPreferredSize(new Dimension(width,length));
		
		cell=aCell;
		
		cell_number=number;
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		double max_density=cell.getCriticalDensity();
		double max_speed=cell.getFreeSpeed();
		
		double density=cell.getRho();
		double speed=cell.getV();
		
		double hue_max_ratio=hue_max/255;
		double hue=speed*hue_max_ratio/max_speed;
		hue=(hue-hue_max_ratio)*-1;
		
		int alpha = (int)(density*255/max_density);
				
		Color myRGBColor = Color.getHSBColor(((float)hue), saturation, bright);
		
		if(alpha>255)alpha=255;
		Color color = new Color(myRGBColor.getRed(),myRGBColor.getGreen(),myRGBColor.getBlue(), alpha);
		
		g.setColor(color);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("default", Font.BOLD, 12));
		g.drawString("Cell "+cell_number,5,this.getHeight()/2-20);
		g.setFont(new Font("default", Font.PLAIN, 10));

		g.drawString(String.format("%06.2f",density)+ " veh/km/lane",5,this.getHeight()/2);
		g.drawString(String.format("%06.2f",speed)+ " km/h",5,this.getHeight()/2+20);
		
		g.setColor(Color.BLACK);
		//g.drawRect(0,0,this.getWidth(),this.getHeight());
		
		
	}
	
}
