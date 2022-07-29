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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mecsyco.observing.swing.util.InitJFrame;
import mecsycoscholar.application.highway.model.event.TrafficModel;


public class EventTrafficModelGUI extends JFrame implements ChangeListener{

	private static final long serialVersionUID = 1L;

	private TrafficModel model;
	private TrafficView traffic_view;
	private OutPanel out_panel;
	private EventStack stack;
	private double time;
	private long refresh=40;
	private double number_img;
	private BasicStatePanel basic;
	private boolean is_model_empty;
	private JLabel refresh_rate;
	private String refresh_rate_string="Refresh rate: ";
	private JSlider slider;
	private JCheckBox animation;
	
	public EventTrafficModelGUI(TrafficModel m){
		super(m.getName());
		//Follow the same behavior as observing artifact
		this.setDefaultCloseOperation(InitJFrame.getDefaultCloseOperationJFrame());
		model = m;
		
		is_model_empty=false;
		if(model.getS().size()==0)is_model_empty=true;
		
		traffic_view=new TrafficView(m);
		JPanel main_pane=new JPanel(new BorderLayout());		
		
		out_panel=new OutPanel(model);
		out_panel.setPreferredSize(new Dimension(30,30));
		animation=new JCheckBox("Animation",false);
		stack=new EventStack(model);
		
		basic = new BasicStatePanel();
		
		main_pane.add(traffic_view,BorderLayout.NORTH);
		main_pane.add(out_panel,BorderLayout.EAST);
		main_pane.add(stack,BorderLayout.CENTER);
		
		JPanel south_pane=new JPanel(new BorderLayout());		
		south_pane.add(basic,BorderLayout.SOUTH);
		
		slider = new JSlider(1,10,1);
		slider.setValue(2);
		slider.addChangeListener(this);
		number_img = ((double)slider.getValue())/10;
		south_pane.add(slider,BorderLayout.CENTER);
		south_pane.add(animation,BorderLayout.WEST);
		refresh_rate=new JLabel(refresh_rate_string+String.format("%1.1f",((double)slider.getValue())/10));
		south_pane.add(refresh_rate,BorderLayout.EAST);
		
		main_pane.add(south_pane,BorderLayout.SOUTH);
		
		this.setContentPane(main_pane);
		this.pack();
		this.setVisible(true);
		
		time = 0;
	}
	
	
	public synchronized void refreshModel(){
		double refresh_to_time = model.getLast_Event_Time();
		
		basic.setTotal_car_number(model.totalNumberOfCars());
		basic.setLast_event_time(refresh_to_time);
		basic.setNext_event_time(model.ta());
		this.repaint();
		
		double number=number_img;
		
		if(number!=0){
			if((animation.isSelected())&&(!is_model_empty)){
	
				time = Math.ceil(time);
				for(double i=time;i <= Math.floor(refresh_to_time);i+=number){
					if(animation.isSelected()){
						traffic_view.setTime(i);
						this.repaint();
						try {
							wait(refresh);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}	
			time = refresh_to_time;
			traffic_view.setTime(time);
			this.repaint();
		}
		
		is_model_empty=false;
		if(model.getS().size()==0)is_model_empty=true;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		refresh_rate.setText(refresh_rate_string+String.format("%1.1f",((double)slider.getValue())/10));
		number_img=(double)slider.getValue()/10;
	}
	
	
	
	
}
