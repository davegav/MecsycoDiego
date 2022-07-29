/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta Copyright(c)
Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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

package mecsycoscholar.application.ode.model;

import mecsyco.core.exception.UnexpectedTypeException;
import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;
import mecsyco.core.type.Tuple2;
import mecsyco.core.type.Tuple3;

/**
 * This class represents a model-artifact for managing an ODE NetLogo model with
 * 3 input ports 1, 2 and 3 used respectively for settings values of variables
 * called "X", "Y" and "Z" in the model. and 3 output ports 1, 2 and 3 used
 * respectively for gettings values of variables called "X", "Y" and "Z" in the
 * model. and having only one parameters time_discretization.
 *
 *
 *
 */
public class EquationModelArtifact extends ModelArtifact {

	// Implementation

	/**
	 * Equation to solve.
	 */
	private final Equation equation;

	// Creation
	/**
	 *
	 * @param name       Name of the model.
	 * @param anEquation {@link #equation}
	 */
	public EquationModelArtifact(String name, Equation anEquation) {
		super(name);
		this.equation = anEquation;
	}

	// Deferred initialization
	/**
	 * STEP0 :Create a simulator instance, load the model and initiate it.
	 */
	@Override
	public void initialize() {
	}

	/**
	 * STEP1 : Set the initial parameters.
	 *
	 * @param args the initial parameters.
	 */
	@Override
	public void setInitialParameters(String[] args) {
	}

	// Model
	/**
	 * @return Time of the last event
	 */
	@Override
	public double getLastEventTime() {
		return equation.getTime();
	}

	/**
	 *
	 * @return Time of the next event
	 */
	@Override
	public double getNextInternalEventTime() {
		return equation.getTime() + equation.getTimeStep();
	}

	/**
	 * Run one simulation step or event
	 */
	@Override
	public void processInternalEvent(double time) {
		// run the go procedure (here our policy is to run only one-by-one)
		equation.doStep();
	}

	/**
	 * Set the model input data into the corresponding port.
	 *
	 * @param event the external event
	 * @param port  the port where the event has to be send (here we assume that
	 *              there are ports 1,2 and 3 respectively correspond to the value
	 *              of variables "X","Y" and "Z")
	 */
	@Override
	public void processExternalInputEvent(SimulEvent event, String port) {
		final SimulData data = event.getData();

		if (data instanceof Tuple1) {
			final Object value = ((Tuple1<?>) event.getData()).getItem1();

			if (value instanceof Number) {
				equation.setVariable(port, ((Number) value).doubleValue());
			} else {
				getLogger().error("processExternalInputEvent",
						new UnexpectedTypeException(Tuple1.of(Number.class), Tuple1.of(value.getClass())));
			}
		} else {
			getLogger().error("processExternalInputEvent",
					new UnexpectedTypeException(Tuple1.of(Number.class), data.getClass()));
		}
	}

	/**
	 * Get the model output event (to exchange with another model)
	 *
	 * @param port the model output port where data has to be collected (here we
	 *             assume that there are ports 1,2 and 3 respectively correspond to
	 *             the value of variables "X","Y" and "Z")
	 * @return the model event for the port
	 */
	@Override
	public SimulEvent getExternalOutputEvent(String port) {
		final SimulEvent result;

		if ("obs".equalsIgnoreCase(port)) {
			final Double x = equation.getVariable("X");
			final Double y = equation.getVariable("Y");

			result = new SimulEvent(new Tuple2<>(x, y), equation.getTime());
		} else if ("obs3d".equalsIgnoreCase(port)) {
			final Double x = equation.getVariable("X");
			final Double y = equation.getVariable("Y");
			final Double z = equation.getVariable("Z");

			result = new SimulEvent(new Tuple3<>(x, y, z), equation.getTime());
		} else {
			SimulData data = new Tuple2<>(equation.getVariable(port), port);
			result = new SimulEvent(data, equation.getTime());
		}

		return result;
	}

	/**
	 * Do something when simulation is done
	 */
	@Override
	public void finishSimulation() {
		getLogger().info("end simulation at time {}", equation.getTime());
	}

}