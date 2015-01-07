/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* Serie.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.model;

import java.util.ArrayList;
import java.util.List;

public class Serie
{
	private String nom;
	private List<String> reponses;
	private int note;
	private boolean isFinished;
	
	public Serie()
	{
		note = 0;
		reponses = new ArrayList<String>();
		this.isFinished = false;
	}
	
	public Serie(String nom, List<String> reponses)
	{
		super();
		this.nom = nom;
		this.reponses = reponses;
		this.isFinished = false;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public List<String> getReponses() {
		return reponses;
	}
	
	public void setReponses(List<String> reponses) {
		this.reponses = reponses;
	}

	public int getNote()
	{
		return note;
	}

	public void setNote(int note)
	{
		this.note = note;
		isFinished = true;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
}