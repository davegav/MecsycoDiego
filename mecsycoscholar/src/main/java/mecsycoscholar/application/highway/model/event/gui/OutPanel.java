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
import java.awt.Graphics;

import javax.swing.JPanel;

import mecsycoscholar.application.highway.model.event.TrafficModel;
import mecsycoscholar.application.highway.type.VehiclesGroup;



public class OutPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private TrafficModel model;
	
	public OutPanel(TrafficModel m){
		model = m;
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		for(int i=0;i<model.getNumber_output_ports();i++){
			VehiclesGroup out_group = model.output(i);
			if(out_group!=null){
				g.setColor(out_group.getColor());
				g.fillRect(0,0,this.getHeight(),this.getHeight());
				g.setColor(Color.WHITE);
				g.drawString(String.format("%.0f", (double) out_group.getCount()), 0, 0+this.getHeight()/2);
			}
		}
	}
}
