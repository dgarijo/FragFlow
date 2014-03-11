/**
 * created May 24, 2006
 * 
 * @by Marc Woerlein (woerlein@informatik.uni-erlangen.de)
 *
 * Copyright 2006 Marc Woerlein
 * 
 * This file is part of parsemis.
 *
 * Licence: 
 *  LGPL: http://www.gnu.org/licenses/lgpl.html
 *   EPL: http://www.eclipse.org/org/documents/epl-v10.php
 *   See the LICENSE file in the project's top-level directory for details.
 */
package de.parsemis.parsers;

/**
 * This class is the specialized label parser for Integers
 * 
 * @author Marc Woerlein (woerlein@informatik.uni-erlangen.de)
 * 
 */
public class IntLabelParser implements LabelParser<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.parsemis.parsers.LabelParser#parse(java.lang.String)
	 */
	public Integer parse(final String text) {
		return new Integer(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.parsemis.parsers.LabelParser#serialize(LabelType)
	 */
	public String serialize(final Integer label) {
		return label.toString();
	}

}
