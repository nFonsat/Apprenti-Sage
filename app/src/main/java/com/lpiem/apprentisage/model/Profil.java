/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* Profil.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.model;

public class Profil {

	private String nom;
	private String prenom;
	private String classe;
	private int avatar;
	
	public Profil()
	{
	}
	
	public Profil(String nom, String prenom, String classe, int avatar){
		this.nom = nom;
		this.prenom = prenom;
		this.classe = classe;
		this.avatar = avatar;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	
	
}
