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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mecsyco.observing.swing.util.InitJFrame;
import mecsycoscholar.application.highway.model.equation.CellModelArtifact;

public class CellModelGUI extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JPanel cell_panel;

	private CellModelArtifact cell;

	private final static int width = 140;

	public CellModelGUI(CellModelArtifact cellModel) {
		super("Macro Equational Model of Traffic Flow");
		cell = cellModel;
		cell_panel = new JPanel(new GridLayout(1, cell.getNbCell()));

		for (int i = 0; i < cell.getNbCell(); i++) {
			cell_panel.add(new ACellStatePrinter(width, width, cell.getCell(i), i + 1));
		}

		JPanel main_panel = new JPanel(new BorderLayout());
		main_panel.add(cell_panel, BorderLayout.CENTER);

		JPanel south_panel = new JPanel(new BorderLayout());
		south_panel.add(new GlobalStatePrinter(cell), BorderLayout.CENTER);
		south_panel.add(new LegendPanel(cell), BorderLayout.SOUTH);

		main_panel.add(south_panel, BorderLayout.SOUTH);
		this.setContentPane(main_panel);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(InitJFrame.getDefaultCloseOperationJFrame());
	}

	/**
	 * Method to close a frame, it is used to end JUnit test of this model artifact.
	 * Send a window closing event to the frame.
	 */
	public void closeWindows() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
