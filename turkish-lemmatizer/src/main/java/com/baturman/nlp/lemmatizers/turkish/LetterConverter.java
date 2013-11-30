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

import java.util.Vector;

/**
 * {@link LetterConverter} is used to replace non-latin characters found in Turkish words.
 * {@link Rule} class is used to create rules for replacement task.
 * 
 * @author Baturman SEN
 *
 */
public class LetterConverter {
	
	private Vector<LetterConverter.Rule> rules = new Vector<LetterConverter.Rule>();

	/**
	 * Creates {@link LetterConverter} object.
	 * 
	 * @param loadDefaultRules pass <code>true</code> for loading default rules, otherwise you should create rules and pass to class.
	 */
	public LetterConverter(boolean loadDefaultRules){
		if (loadDefaultRules){
			this.loadDefaultRules();
		}
	}
	
	/**
	 * Converts non-latin character with acceptable character by validating rules.
	 * 
	 * @param l character
	 * @return Turkish latin replacement of given character. If replacement fails, returns same character.
	 */
	public char convertCharacter(char l){
		char result = l;
		for (LetterConverter.Rule r : rules) {
			int [] characters = r.getCharacters();
			for (int i = 0; i < characters.length; i++) {
				if ((int)l == characters[i]){
					result = (char) r.getConvertTo();
				}
			}
		}
		return result;
	}
	
	/**
	 * Replaces all non-latin characters with corresponding replacements. 
	 * 
	 * @param s Input string
	 * @return replaced string.
	 */
	public String convertString(String s){
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < s.length(); i++) {
			sb.replace(i, i+1, ""+convertCharacter(s.charAt(i)));
		}
		return sb.toString();
	}
	
	/**
	 * Adds new rule.
	 * @param r {@link Rule}
	 */
	public void addRule(LetterConverter.Rule r){
		this.rules.add(r);
	}
	
	/**
	 * Creates and loads default rules.<br/>
	 * Default Rules:
	 * <ol>
	 * <li>Converts â,ã,ä to a</li>
	 * <li>Converts Â,Ã,Ä to A</li>
	 * <li>Converts é,ê,ë to e</li>
	 * <li>Converts É,Ê,Ë to E</li>
	 * <li>Converts ú,û,ü to u</li>
	 * <li>Converts Ú,Û,Ü to U</li>
	 * <li>Converts Í,Î,Ï to I</li>
	 * <li>Converts í,î,ï to i</li>
	 * <li>Converts ò,ó,ô to o</li>
	 * <li>Converts Ò,Ó,Ô to O</li>
	 * </ol>
	 */
	private void loadDefaultRules(){
		rules.add(new Rule(new int[] {226,227,228}, 97));  // Converts â,ã,ä to a respectively.
		rules.add(new Rule(new int[] {194,195,196}, 65));  // Converts Â,Ã,Ä to A respectively.
		rules.add(new Rule(new int[] {233,234,235}, 101)); // Converts é,ê,ë to e respectively.
		rules.add(new Rule(new int[] {201,202,203}, 69));  // Converts É,Ê,Ë to E respectively.
		rules.add(new Rule(new int[] {250,251}, 117)); 	   // Converts ú,û to u respectively.
		rules.add(new Rule(new int[] {218,219}, 85));      // Converts Ú,Û to U respectively.
		rules.add(new Rule(new int[] {205,206,207}, 73));  // Converts Í,Î,Ï to I respectively.
		rules.add(new Rule(new int[] {237,238,239}, 105)); // Converts í,î,ï to i respectively.
		rules.add(new Rule(new int[] {242,243,244}, 111)); // Converts ò,ó,ô to o respectively.
		rules.add(new Rule(new int[] {210,211,212}, 79));  // Converts Ò,Ó,Ô to O respectively.
	}
	
	/**
	 * Defines character replacement rule.
	 * 
	 * @author Baturman SEN
	 * @version 1
	 *
	 */
	public class Rule{
		private int [] characters;
		private int convertTo;
		
		/**
		 * Creates a rule with given parameters. All characters that is passed in <code>characters</code> array will be converted to <code>convertTo</code>
		 * @param characters Characters to be replaced
		 * @param convertTo Characters will be replaced to.
		 */
		public Rule(int[] characters, int convertTo) {
			this.characters = characters;
			this.convertTo = convertTo;
		}

		/**
		 * @return List of characters to be replaced.
		 */
		public int[] getCharacters() {
			return characters;
		}

		/**
		 * @return character will be replaced.
		 */
		public int getConvertTo() {
			return convertTo;
		}
	}
	
}
