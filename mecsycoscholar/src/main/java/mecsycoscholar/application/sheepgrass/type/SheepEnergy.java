/*
V. Chevrier, L. Ciarletta, J. Siebert,
Copyright (c) Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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

package mecsycoscholar.application.sheepgrass.type;

import java.io.Serializable;

/**
 * represents the energy of sheep.
 * 
 * 
 */
public class SheepEnergy implements Serializable {

// Constant
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

// Creation
	/**
	 * @param aId
	 *        - {@link #getId()}
	 * @param aEnergy
	 *        - {@link #getEnergy()}
	 */
	public SheepEnergy (long aId, double aEnergy) {
		id = aId;
		energy = aEnergy;
	}

// Access
	public long getId () {
		return id;
	}

	public double getEnergy () {
		return energy;
	}

// Implementation
	/**
	 * {@link #getId()}
	 */
	protected long id;

	/**
	 * {@link #getEnergy()}
	 */
	protected double energy;

}
