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

package mecsycoscholar.application.highway.type;

import java.awt.Color;

import mecsyco.core.type.SimulData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VehiculesGroups is a Tuple1<AVG Speed, Number of vehicle, Color>
 *
 * 
 *
 */
@JsonIgnoreProperties({ "color" })
public class VehiclesGroup implements SimulData{

// Version
	private static final long serialVersionUID = 1L;

// Creation
	/**
	 *
	 * @param aSpeed
	 *        {@link #getSpeed()}
	 * @param aCount
	 *        {@link #getCount()}
	 * @param aColor
	 *        {@link #getColor()}
	 */
	public VehiclesGroup (@JsonProperty("speed") double aSpeed, @JsonProperty("count") int aCount, @JsonProperty("color") Color aColor) {
		color = aColor;
		count = aCount;
		speed = aSpeed;
	}

	/**
	 * Create with a random color.
	 *
	 * @param aSpeed
	 *        {@link #getSpeed()}
	 * @param aCount
	 *        {@link #getCount()}
	 */
	public VehiclesGroup (double aSpeed, int aCount) {
		this(aSpeed, aCount, new Color((int) (Math.random() * 255), (int) (Math.random() * 255),
				(int) (Math.random() * 255)));
	}

// Derivation
	public VehiclesGroup withSpeed (double aSpeed) {
		return new VehiclesGroup(aSpeed, getCount());
	}

	public VehiclesGroup withCount (int aCount) {
		return new VehiclesGroup(getSpeed(), aCount);
	}
	
	
	// Access
	
		public Color getColor () {
			return color;
		}
	
		public int getCount () {
			return count;
		}

		public double getSpeed () {
			return speed;
		}

	// Implementation
		/**
		 * {@link #getCount()}
		 */
		private final int count;

		/**
		 * {@link #getSpeed()}
		 */
		private final double speed;
		
		/**
		 * {@link #getColor()}
		 */
		private final Color color;

}