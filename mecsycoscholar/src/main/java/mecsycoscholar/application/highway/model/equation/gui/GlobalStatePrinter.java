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

import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import mecsycoscholar.application.highway.model.equation.CellModelArtifact;


public class GlobalStatePrinter extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CellModelArtifact modelArt;
	
	private JLabel time;
	
	public GlobalStatePrinter(CellModelArtifact model){
		super();
		modelArt=model;
		time=new JLabel("simulated time: "+String.format("%.5f",modelArt.getLastEventTime())+" h");
		this.add(time);
		//this.setPreferredSize(new Dimension(50,50));
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		time.setText("Simulated time: "+String.format("%.5f",modelArt.getLastEventTime())+" h");
	}
	
}
