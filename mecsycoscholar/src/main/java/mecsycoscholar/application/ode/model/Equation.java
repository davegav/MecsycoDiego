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

import java.util.HashMap;
import java.util.Map;

public abstract class Equation {

	protected Map<String, Double> state_variables;

	protected Map<String, Double> parameters;

	protected double time;

	protected double time_step;

	public Equation (String[] var_names, double[] var_init_values, String[] param, double[] param_values, double step) {
		time = 0;
		time_step = step;
		state_variables = new HashMap<String, Double>();
		for (int i = 0; i < var_names.length; i++) {
			state_variables.put(var_names[i], var_init_values[i]);
		}
		parameters = new HashMap<String, Double>();
		for (int i = 0; i < param.length; i++) {
			parameters.put(param[i], param_values[i]);
		}
	}

	public double getVariable (String var) {
		return state_variables.get(var);
	}

	public void setVariable (String var, double value) {
		state_variables.put(var, value);
	}

	public double getParameters (String var) {
		return parameters.get(var);
	}

	public void doStep () {
		dynamics();
		time += time_step;
	}

	public abstract void dynamics ();

	public double getTime () {
		return time;
	}

	public double getTimeStep () {
		return time_step;
	}

}
