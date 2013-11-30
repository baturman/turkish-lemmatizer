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

import java.util.regex.Pattern;

/**
 * This class defines a {@link Stem}. 
 * 
 * @author Baturman SEN
 *
 */
public class Stem {
	private String stem;
	private String type = "N/A";

	/**
	 * Creates stem object with given string.
	 * @param stem 
	 */
	public Stem(String stem) {
		this.stem = stem;
	}


	/**
	 * Creates stem object with given parameters.
	 * @param stem Stem
	 * @param type Type
	 */
	public Stem(String stem, String type) {
		this.stem = stem;
		this.type = type;
	}

	/**
	 * @return stem.
	 */
	public String getStem() {
		return stem;
	}

	/**
	 * @return {@link Pattern} representation of stem.
	 */
	public Pattern getPattern(){
		return Pattern.compile(this.stem);
	}

	/**
	 * @return softened conversion of stem.
	 * eg. kitap -> kitab
	 */
	public String [] getSoftenedConversion(){
		String [] stemModified = new String[2];

		char lastLetter = this.stem.charAt(this.stem.length()-1);

		switch (lastLetter) {
		case 'p':
			stemModified[0] = this.stem.substring(0, this.stem.length()-1)+"b";
			break;
		case 'ç':
			stemModified[0] = stemModified[0] = this.stem.substring(0, this.stem.length()-1)+"c";
			break;
		case 't':
			stemModified[0] = this.stem.substring(0, this.stem.length()-1)+"d";
			break;
		case 'k':
			stemModified[0] = this.stem.substring(0, this.stem.length()-1)+"g";
			stemModified[1] = this.stem.substring(0, this.stem.length()-1)+"ğ";
			break;
		default:
			stemModified[0] = this.stem;
			break;
		}

		return stemModified;
	}

	/**
	 * @return daralmis versiyonu.
	 * bekl-e -> bekl-i
	 */
	public String [] getDaralmisHalleri(){
		String [] daralmis = new String[4];
		daralmis[0] = this.stem.substring(0, this.stem.length()-1)+"ı";
		daralmis[1] = this.stem.substring(0, this.stem.length()-1)+"i";
		daralmis[2] = this.stem.substring(0, this.stem.length()-1)+"u";
		daralmis[3] = this.stem.substring(0, this.stem.length()-1)+"ü";
		return daralmis;
	}

	/**
	 * @return ünlü düşmüş version.
	 * oğul -> oğl
	 */
	public String getUnluDusmusHali(){
		StringBuffer sb = new StringBuffer();

		char [] characters = this.stem.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			if (i >= characters.length-3){
				switch (characters[i]) {
				case 'ı':
					// Discard
					break;
				case 'i':
					// Discard
					break;
				case 'u':
					// Discard
					break;
				case 'ü':
					// Discard
					break;
				default:
					sb.append(characters[i]);
					break;
				}
			}else{
				sb.append(characters[i]);
			}
		}

		return sb.toString();
	}
	
	/**
	 * @return ünsüz düşmeli hali.
	 * Küçücük -> küçü
	 */
	public String getUnsuzDusmeliHali(){
		return this.stem.substring(0, stem.length()-1);
	}
	
	/**
	 * @return type of stem.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets type of stem.
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

}
