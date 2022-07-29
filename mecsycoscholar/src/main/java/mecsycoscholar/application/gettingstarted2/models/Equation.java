package mecsycoscholar.application.gettingstarted2.models;
/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta, Copyright (c) Université
Copyrights de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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

import java.util.HashMap;
import java.util.Map;

public abstract class Equation {
	/**
	 * Name of the equation.
	 */
	private String name;

	/**
	 * Map of state variables.
	 */
	private Map<String, Double> stateVariables;

	/**
	 * Parameters of the equation.
	 */
	private Map<String, Double> parameters;

	/**
	 * Current time.
	 */
	private double time;

	/**
	 * Tie step used when solving the equation.
	 */
	private double timeStep;

	/**
	 * Log or not result in the console.
	 */
	private boolean trace = true;

	/**
	 * Default time step.
	 */
	public static final double DEFAULT_TIME_STEP = 0.01;

	/**
	 *
	 * @param aName
	 * @param varNames
	 * @param varInitValues
	 * @param param
	 * @param paramValues
	 * @param aTimeStep
	 */
	public Equation(String aName, String[] varNames, double[] varInitValues, String[] param, double[] paramValues,
			double aTimeStep) {
		name = aName;
		time = 0;
		timeStep = aTimeStep;
		stateVariables = new HashMap<String, Double>();
		for (int i = 0; i < varNames.length; i++) {
			stateVariables.put(varNames[i], varInitValues[i]);
		}
		parameters = new HashMap<String, Double>();
		for (int i = 0; i < param.length; i++) {
			parameters.put(param[i], paramValues[i]);
		}
	}

	/**
	 *
	 * @return {@link #name}.
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return Is trace enabled (print results in console) or not.
	 */
	public boolean isTrace() {
		return trace;
	}

	/**
	 *
	 * @param b True to enable, false to disable.
	 */
	public void setTrace(boolean b) {
		trace = b;
	}

	/**
	 *
	 * @param var State variable name.
	 * @return Its value.
	 */
	public double getVariable(String var) {
		return stateVariables.get(var);
	}

	/**
	 *
	 * @param var   State variable name.
	 * @param value New value.
	 */
	public void setVariable(String var, double value) {
		stateVariables.put(var, value);
	}

	/**
	 *
	 * @param var Parameter name.
	 * @return Its value.
	 */
	public double getParameters(String var) {
		return parameters.get(var);
	}

	/**
	 * Compute the dynamics and move the time one time step ahead.
	 */
	public void doStep() {
		dynamics();
		time += timeStep;
		if (trace) {
			System.out.println("" + time + "  " + name + " " + stateVariables);
		}
	}

	/**
	 * Method where to define te dynamic part of the equation.
	 */
	public abstract void dynamics();

	/**
	 *
	 * @return {@link #time}
	 */
	public double getTime() {
		return time;
	}

	/**
	 *
	 * @return {@link #timeStep}
	 */
	public double getTimeStep() {
		return timeStep;
	}

}
