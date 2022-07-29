/*
V. Chevrier, L. Ciarletta, V. Elvinger, Copyright (c) Université de
Lorraine & INRIA, Affero GPL license v3, 2014-2015

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

package mecsycoscholar.common.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import mecsyco.core.type.SimulData;
import mecsyco.core.type.Type;

/**
 * Structure counting the number of item occurrences.
 *
 * 
 * 
 * @param <K> No-variant and nullable generic
 */
public class Bag<K extends Serializable> implements SimulData {

// Version
	private static final long serialVersionUID = 1L;

// COnstant
	private final static Type BaseType = new Type(Bag.class);

// Static
	public static Type of (Type aParameter) {
		return BaseType.parametrized(aParameter);
	}

	public static Type of (Class<?> aParameter) {
		return BaseType.parametrized(aParameter);
	}

// Creation
	/**
	 * Create an empty bag.
	 */
	public Bag () {
		itemCounts = new HashMap<>();

		assert isEmpty(): "ensure: is empty.";
	}

// Access
	/**
	 *
	 * @param aItem
	 * @return Number of occurrences of `aItem'.
	 */
	public long countOf (Object aItem) {
		long result;

		if (itemCounts.containsKey(aItem)) {
			result = itemCounts.get(aItem);
		}
		else {
			result = 0;
		}

		assert result >= 0: "ensure: result is a natural";

		return result;
	}

	/**
	 * @return Occurring items.
	 */
	public Set<K> items () {
		return itemCounts.keySet();
	}

	@Override
	public int hashCode () {
		return itemCounts.hashCode();
	}

// Status
	/**
	 * @return Is there no items?
	 */
	public boolean isEmpty () {
		return itemCounts.isEmpty();
	}

	@Override
	public boolean equals (@Nullable Object aOther) {
		return aOther instanceof Bag && itemCounts.equals(((Bag<?>) aOther).itemCounts);
	}

	/**
	 * 
	 * @return Number of unique item. (Set count).
	 */
	public int uniqueItemCount () {
		return itemCounts.size();
	}

// Debugging
	@Override
	public String toString () {
		return String.format(ToStringMapTemplate, toStringPairs());
	}

	/**
	 * @return Pairs debugging representation.
	 */
	protected String toStringPairs () {
		final StringBuilder builder = new StringBuilder(60);

		for (Map.Entry<K, Long> e : itemCounts.entrySet()) {
			builder.append(String.format(ToStringPairTemplate, e.getKey(), e.getValue()));
		}

		return builder.toString();
	}

// Extension
	/**
	 * Add an occurrence of `aItem'.
	 *
	 * @param aItem
	 */
	public void extend (K aItem) {
		long oldCount = countOf(aItem);

		itemCounts.put(aItem, oldCount + 1);

		assert countOf(aItem) == (oldCount + 1): "ensure: one extra occurrence of `aItem'.";
	}

// Implementation
	/**
	 * {1}: body
	 */
	protected final static String ToStringMapTemplate = "Bag{\n%s}";

	/**
	 * {1}: item {2}: occurrences of item
	 */
	protected final static String ToStringPairTemplate = "\t%s: %s\n";

	/**
	 * key: item value: number of occurrences of the associated item (key)
	 */
	protected final Map<K, Long> itemCounts;

}
