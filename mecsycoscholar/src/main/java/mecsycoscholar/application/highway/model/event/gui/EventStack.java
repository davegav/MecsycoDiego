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


public class EventStack extends JPanel{

	private static final long serialVersionUID = 1L;

	TrafficModel model;
	
	public EventStack(TrafficModel m){
		super();
		model=m;
		this.setPreferredSize(new Dimension(600,50));
	}
	
	@Override
	public void paint(Graphics g){
		g.drawLine(0,this.getHeight()/2,this.getWidth(), this.getHeight()/2);
		
		synchronized(model.getS()){
			Iterator<Double> ite=model.getS().navigableKeySet().descendingIterator();
			if(ite.hasNext()){
				double max_time_event = model.getS().lastKey();
				double min_time_event = model.getS().firstKey();
				int alternate = 15;
				
				while(ite.hasNext()){
					double arrival_time = ite.next();
					VehiclesGroup group = model.getS().get(arrival_time);	
					int X= (int)((arrival_time-min_time_event)*(this.getWidth()-25)/(max_time_event-min_time_event))+10 ;
					g.setColor(group.getColor());
					g.drawLine(X,this.getHeight()/2-10,X,this.getHeight()/2+10);
					g.setColor(Color.BLACK);
					g.drawString(""+(int)(Math.floor(arrival_time)),X-10,this.getHeight()/2+alternate);
					alternate*=-1;
				}
			}
		}
	}
}
