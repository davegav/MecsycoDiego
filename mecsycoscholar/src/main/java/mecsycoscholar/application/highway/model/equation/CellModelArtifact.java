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

package mecsycoscholar.application.highway.model.equation;

import java.util.Map;

import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;
import mecsyco.core.type.Tuple3;
import mecsycoscholar.application.highway.model.equation.gui.CellModelGUI;

public class CellModelArtifact extends ModelArtifact {

	protected Cell[] myCell;

	protected double time_step;

	protected CellModelGUI gui;

	protected int delay;

	/**
	 * Automate the closure of the GUI at the end of the simulation.
	 */
	private boolean autoCloseGui;

	public CellModelArtifact(String name, double length, double time_step, double density, double speed,
			double free_speed, double capacity, double critical_density, double nb_voie, double tau, double kappa,
			double nu, int nbCell) {

		super("cell_model_2");
		this.time_step = time_step;
		myCell = new Cell[nbCell];

		for (int i = 0; i < myCell.length; i++) {
			myCell[i] = new Cell(name + i, length, time_step, density, speed, free_speed, capacity, critical_density,
					nb_voie, tau, kappa, nu);
		}
		UpdateInternalCellsPorts();

		gui = new CellModelGUI(this);
		delay = 0;
	}

	public CellModelArtifact(String name, double length, double time_step, double density, double speed,
			double free_speed, double capacity, double critical_density, double nb_voie, double tau, double kappa,
			double nu, int nbCell, int delay) {
		this(name, length, time_step, density, speed, free_speed, capacity, critical_density, nb_voie, tau, kappa, nu,
				nbCell);
		this.delay = delay;
	}

	/**
	 * Constructor with a name and a map containing the different parameters.
	 *
	 * @param name
	 * @param parameters Map of parameters, the keys are the name of the attributes.
	 */
	public CellModelArtifact(String name, Map<String, Object> parameters) {
		this(name, parameters.get("length") != null ? (Double) parameters.get("length") : 0.2,
				parameters.get("time_step") != null ? (Double) parameters.get("time_step") : 0.0001,
				parameters.get("density") != null ? (Double) parameters.get("density") : 30.,
				parameters.get("speed") != null ? (Double) parameters.get("speed") : 70.,
				parameters.get("free_speed") != null ? (Double) parameters.get("free_speed") : 100.,
				parameters.get("capacity") != null ? (Double) parameters.get("capacity") : 1800.,
				parameters.get("critical_density") != null ? (Double) parameters.get("critical_density") : 45.,
				parameters.get("nb_voie") != null ? (Integer) parameters.get("nb_voie") : 3,
				parameters.get("tau") != null ? (Double) parameters.get("tau") : 1.,
				parameters.get("kappa") != null ? (Double) parameters.get("kappa") : 1.,
				parameters.get("nu") != null ? (Double) parameters.get("nu") : 1.,
				parameters.get("nbCells") != null ? (Integer) parameters.get("nbCells") : 5);
		this.delay = parameters.get("delay") != null ? (Integer) parameters.get("delay") : 1;
		autoCloseGui = parameters.get("autoCloseGui") != null ? (Boolean) parameters.get("autoCloseGui") : false;
	}

	private void UpdateInternalCellsPorts() {
		for (int i = 0; i < myCell.length; i++) {
			if (i != 0) {
				if (Double.isNaN(myCell[i - 1].getV())) {
					System.out.println("Cell " + (i - 1) + "'s V is NaN");
				}
				myCell[i].setInput_v(myCell[i - 1].getV());
				myCell[i].setInput_q(myCell[i - 1].getQ());
			}
			if (i != myCell.length - 1) {
				// System.out.println("Rho Cell "+i+" "+myCell[i + 1].getRho());
				myCell[i].setInput_rho(myCell[i + 1].getRho());
			}
		}
	}

	@Override
	public void processInternalEvent(double time) {
		for (int i = 0; i < myCell.length; i++) {
			myCell[i].doStep();
		}
		UpdateInternalCellsPorts();
		gui.repaint();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getNextInternalEventTime() {
		if (gui.isDisplayable()) {
			return myCell[0].getTime() + time_step;
		} else {
			return Double.MAX_VALUE;
		}
	}

	@Override
	public double getLastEventTime() {
		return myCell[0].getTime();
	}

	@Override
	public void processExternalInputEvent(SimulEvent event, String port) {

		Tuple3<?, ?, ?> data = (Tuple3<?, ?, ?>) event.getData();
		switch (port) {
		// if data come from upstream
		case "upstream":
			// System.out.println("upstream " + data.getFlowRate() + "veh/km " +
			// data.getSpeed() + "km/h");
			myCell[0].setInput_q((Double) data.getItem1()); // Flowrate
			myCell[0].setInput_v((Double) data.getItem2()); // Speed
			break;
		// if data come from downstream
		case "downstream":
			// System.out.println("downstream" + data.getDensity());
			myCell[myCell.length - 1].setInput_rho((Double) data.getItem3()); // density
			break;
		default:
			System.out.println("Error : Cells of " + myCell[0].getName() + " doesn't know the port " + port);
		}
	}

	@Override
	public SimulEvent getExternalOutputEvent(String port) {
		SimulEvent ret = null;

		if (port.equals("observation")) {
			double sum_rho = 0;
			for (int i = 0; i < myCell.length; i++) {
				sum_rho += myCell[i].getRho();
			}
			ret = new SimulEvent(new Tuple1<Double>(sum_rho / myCell.length), getLastEventTime());
		} else if (port.equals("upstream")) {
			ret = new SimulEvent(new Tuple3<Double, Double, Double>(0., 0., myCell[0].getRho()), myCell[0].getTime());
		} else if (port.equals("downstream")) {
			int i = myCell.length - 1;
			ret = new SimulEvent(new Tuple3<Double, Double, Double>(myCell[i].getQ(), myCell[i].getV(), 0.),
					myCell[i].getTime());
		}

		return ret;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishSimulation() {
		if (autoCloseGui) {
			gui.closeWindows();
		}
		getLogger().info("Cells of " + myCell[0].getName() + " finish simulation at time " + myCell[0].getTime());
	}

	@Override
	public void setInitialParameters(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getNbCell() {
		return myCell.length;
	}

	public Cell getCell(int i) {
		return myCell[i];
	}

}
