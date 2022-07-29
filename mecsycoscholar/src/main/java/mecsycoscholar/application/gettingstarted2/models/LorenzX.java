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

/**
 * {@link Equation} for Lorenz X.
 *
 */
public class LorenzX extends Equation {

	/**
	 *
	 * @param x
	 * @param a
	 */
	public LorenzX(double x, double a) {
		this(x, a, DEFAULT_TIME_STEP);
	}

	/**
	 *
	 * @param x
	 * @param a
	 * @param aTimeStep
	 */
	public LorenzX(double x, double a, double aTimeStep) {
		super("Xmodel", new String[] {"X", "Y"}, new double[] {x, 0}, new String[] {"A"}, new double[] {a}, aTimeStep);
	}

	@Override
	public void dynamics() {
		double dx = (getParameters("A") * (getVariable("Y") - getVariable("X")));
		setVariable("X", (getVariable("X") + dx * getTimeStep()));
	}

}
