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

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BasicStatePanel extends JPanel{

	private static final long serialVersionUID = 1L;

	JLabel next_event_time;
	JLabel last_event_time;
	JLabel total_car_number;
	String total_number="Total number of cars : ";
	String last="Time of the last event : ";
	String next="Time of the next event : ";
	
	public BasicStatePanel(){
		super();
		super.setLayout(new GridLayout(1,3));
		next_event_time=new JLabel(next+String.format("%12s","+\u221e     "));
		last_event_time=new JLabel(last+String.format("%12.5f",0d));
		next_event_time.setHorizontalAlignment(SwingConstants.CENTER);
		total_car_number=new JLabel(total_number+String.format("%7.3f",0d));
		total_car_number.setHorizontalAlignment(SwingConstants.RIGHT);
		
		this.add(last_event_time);
		this.add(next_event_time);
		this.add(total_car_number);
	}
	
	
	/*public void paint(Graphics g){
		g.drawString("Time of the last event : "+last_event_time,0,this.getHeight());
		g.drawString("Time of the next event : "+next_event_time,this.getWidth()/2,this.getHeight());
		g.drawString("Total number of cars : "+total_car_number,this.getWidth(),this.getHeight());
	}*/


	public void setNext_event_time(double next_e) {
		if(next_e==Double.MAX_VALUE){
			this.next_event_time.setText(next+String.format("%12s","+\u221e     "));
		}else{
			this.next_event_time.setText(next+String.format("%12.5f",next_e));
		}
	}


	public void setLast_event_time(double last_event_time) {
		this.last_event_time.setText(last+String.format("%12.5f",last_event_time));
	}

	public void setTotal_car_number(double total_car_number) {
		this.total_car_number.setText(total_number+String.format("%7.3f",total_car_number));
	}
}
