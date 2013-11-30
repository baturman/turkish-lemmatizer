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
 * {@link Tracer} class for lemmatization controls.
 * 
 * @author Baturman SEN
 *
 */
public class Tracer {
	private int control;
	private boolean result;
	private String [] names = {
			"EN_UZUN_KOK_KONTROLU", 
			"UNSUZ_YUMUSAMA_KONTROLU", 
			"UNLU_DARALMASI_KONTROLU", 
			"UNLU_DUSMESI_KONTROLU",
			"UNSUZ_DUSMESI_KONTROLU",
			"PEKISTIRME_KONTROLU"
	};
	
	/**
	 * Creates {@link Tracer} object with given values.
	 * 
	 * @param control Control item
	 * @param result Result of control process.
	 */
	public Tracer(int control, boolean result) {
		this.control = control;
		this.result = result;
	}

	/**
	 * @return control result.
	 */
	public boolean getResult(){
		return this.result;
	}

	/**
	 * Sets result for control.
	 * @param result
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return control value.
	 */
	public int getStep() {
		return control;
	}

	/**
	 * Sets control value.
	 * 
	 * @param v
	 */
	public void setControl(int v) {
		this.control = v;
	}

	/**
	 * @return name of control.
	 */
	public String getControlName(){
		return names[control];
	}


}
