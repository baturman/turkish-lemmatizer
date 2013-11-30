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
 * This class defines 29 letter Turkish Latin alphabet. Use {@link TurkishAlphabet}.display() to display letters. 
 * 
 * @see http://en.wikipedia.org/wiki/Turkish_alphabet
 * @author Baturman ŞEN
 *
 */
public class TurkishAlphabet {
	
	public final static int [] ALPHABET = {
		97,98,99,231,100,101,102,103,287,104,305,105,106,107,108,109,110,111,246,112,114,115,351,116,117,252,118,121,122
	};

	/**
	 * Displays turkish alphabet.
	 */
	public static void display(){
		for (int i = 0; i < ALPHABET.length; i++) {
			char t = (char)ALPHABET[i];
			System.out.println(ALPHABET[i] + " " + t);
			
		}
	}
	
	/**
	 * @param pos Position
	 * @return the character at given position
	 */
	public static char getLetter(int pos){
		return (char)ALPHABET[pos];
	}
	
	/**
	 * @param letter
	 * @return the position of given character. If return value is equal to <code>-1</code> that means, given character is not in Turkish Latin Alphabet
	 */
	public static int getPosition(char letter){
		int pos = -1;
		for (int i = 0; i < ALPHABET.length; i++) {
			if (ALPHABET[i] == (int)letter){
				pos = i;
				break;
			}
		}
		return pos;
	}
	
}