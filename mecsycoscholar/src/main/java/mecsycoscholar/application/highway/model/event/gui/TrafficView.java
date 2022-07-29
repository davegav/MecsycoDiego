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

package mecsycoscholar.application.highway.model.event.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JPanel;

import mecsycoscholar.application.highway.model.event.TrafficModel;
import mecsycoscholar.application.highway.type.VehiclesGroup;

public class TrafficView extends JPanel{

	private static final long serialVersionUID = 1L;

	private TrafficModel model;
	
	private double time;
		
	public TrafficView(TrafficModel m){
		super();
		model = m;
		time = 0;
		this.setPreferredSize(new Dimension(600,30));
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(), this.getHeight());
		synchronized(model.getS()){
			Iterator<Double> ite=model.getS().keySet().iterator();
			while(ite.hasNext()){
				double arrival_time = ite.next();
				VehiclesGroup group = model.getS().get(arrival_time);
				
				double delay = arrival_time - time;		
				double speed = group.getSpeed();
				double length = model.getLength();
				
				double position = length - (delay * speed);
				
				int Xcoord = Math.round( (float)(position * this.getWidth() / length));
				
				g.setColor(group.getColor());
				g.fillRect(Xcoord,0,this.getHeight(),this.getHeight());
				g.setColor(Color.WHITE);
				g.drawString(String.format("%.0f",(double) group.getCount()), Xcoord, 0+this.getHeight()/2);
			}
			
			double arrival_time = model.getLast_Event_Time();
			for(int i=0;i< model.getNumber_output_ports();i++){
				VehiclesGroup out_group=model.output(i);
				if((time < arrival_time)&&(out_group!=null)){
					double delay = arrival_time - time;		
					double speed = out_group.getSpeed();
					double length = model.getLength();
					
					double position = length - (delay * speed);
					
					int Xcoord = Math.round( (float)(position * this.getWidth() / length));
					
					g.setColor(out_group.getColor());
					g.fillRect(Xcoord,0,this.getHeight(),this.getHeight());
					g.setColor(Color.WHITE);
					g.drawString(out_group.getCount()+"", Xcoord, 0+this.getHeight()/2);
				}
			}
		}
	}

	public TrafficModel getModel() {
		return model;
	}

	public void setModel(TrafficModel model) {
		this.model = model;
	}	
	
	public void setTime(double t){
		time=t;
	}
	
	
}
