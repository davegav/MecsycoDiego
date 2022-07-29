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

public class LorenzY extends Equation {

	public LorenzY (double Y, double B, double step) {
		super(new String[] { "X", "Y", "Z" }, new double[] { 0, Y, 0 }, new String[] { "B" }, new double[] { B }, step);
	}

	@Override
	public void dynamics () {
		double dy = (getVariable("X") * (getParameters("B") - getVariable("Z")) - getVariable("Y"));
		setVariable("Y", getVariable("Y") + dy * time_step);
	}

}
