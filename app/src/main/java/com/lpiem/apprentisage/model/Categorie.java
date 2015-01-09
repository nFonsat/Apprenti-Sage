/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* Categorie.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.model;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Color;

public class Categorie
{
	private String nom;						// Nom
	private int color;						// Couleur
	private List<Categorie> subCategorie;	// Sous Categories
	private int successSerie = 0; 			// Series reussi
	private String mode; 					// Mode
	private List<Serie> serieList;			// Liste de series
	private int pourcentage;
	private int nbSerieDone = 0;
	
	public Categorie()
	{
		nom = "";
		color = Color.BLUE;
		subCategorie = new ArrayList<Categorie>();
		serieList = new ArrayList<Serie>();
	}
	
	public Categorie(String nom, int color)
	{
		this.nom = nom;
		this.color = color;
		subCategorie = new ArrayList<Categorie>();
		serieList = new ArrayList<Serie>();
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public List<Categorie> getSubCategorie()
	{
		return subCategorie;
	}

	public void setSubCategorie(List<Categorie> subCategorie)
	{
		this.subCategorie = subCategorie;
	}

	public int getSerieReussi()
	{
		return successSerie;
	}

	public void setSerieReussi(int successSerie)
	{
		this.successSerie = successSerie;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<Serie> getSerieList() {
		return serieList;
	}

	public void setSerieList(List<Serie> serieList) {
		this.serieList = serieList;
	}

	public int getPourcentage()
	{
		return pourcentage;
	}

	public void setPourcentage(int pourcentage)
	{
		this.pourcentage = pourcentage;
	}

	public int getNbSerieDone()
	{
		return nbSerieDone;
	}

	public void setNbSerieDone(int nbSerieDone)
	{
		this.nbSerieDone = nbSerieDone;
	}
}