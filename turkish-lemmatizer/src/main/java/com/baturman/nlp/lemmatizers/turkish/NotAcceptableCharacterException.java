/**
 * 
 * Copyright 2011, 2013 Baturman SEN
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.baturman.nlp.lemmatizers.turkish;

/**
 * 
 * Exception thrown, if an unknown character is supplied.
 * 
 * @author Baturman SEN
 * 
 *
 */
public class NotAcceptableCharacterException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public NotAcceptableCharacterException() {

	}
	
	public NotAcceptableCharacterException(char c, String stem, int pos) {
		super("Character '" + c + "' at position " + (pos+1) + " \"" + stem + "\" is not an acceptable letter for Turkish Lemmatizer.");
	}
	
}
