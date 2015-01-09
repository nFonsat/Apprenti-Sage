/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* Shared.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;

import com.lpiem.apprentisage.model.Categorie;
import com.lpiem.apprentisage.model.Profil;
import com.lpiem.apprentisage.model.Serie;

public class Shared
{
	private static Shared instance;
	
	private List<Categorie> catListCp;
	private List<Categorie> catListCe1;
	private List<Categorie> currentCatList;
	private Categorie currentCategorie;
	private Categorie currentSubCategorie;
	private List<Profil> listProfils;
	private Profil currentProfil;
	
	private Shared()
	{
		catListCp = new ArrayList<Categorie>();
		catListCe1 = new ArrayList<Categorie>();
		setCurrentCategorieList(new ArrayList<Categorie>());
		listProfils = new ArrayList<Profil>();
	}
	
	public static Shared getInstance()
	{
		if(instance == null)
			instance = new Shared();
		
		return instance;
	}

	public Categorie getCurrentCategorie()
	{
		return currentCategorie;
	}

	public void setCurrentCategorie(Categorie currentCategorie)
	{
		this.currentCategorie = currentCategorie;
	}
	
	public List<Profil> getListProfils() {
		return listProfils;
	}

	public void setListProfils(List<Profil> listProfils) {
		this.listProfils = listProfils;
	}

	public Profil getCurrentProfil() {
		return currentProfil;
	}

	public void setCurrentProfil(Profil currentProfil) {
		this.currentProfil = currentProfil;
	}

	public Categorie getCurrentSubCategorie() {
		return currentSubCategorie;
	}

	public void setCurrentSubCategorie(Categorie currentSubCategorie) {
		this.currentSubCategorie = currentSubCategorie;
	}

	public List<Categorie> getCategorieListCp() {
		return catListCp;
	}

	public void setCategorieListCp(List<Categorie> catListCp) {
		this.catListCp = catListCp;
	}

	public List<Categorie> getCategorieListCe1() {
		return catListCe1;
	}

	public void setCategorieListCe1(List<Categorie> catListCe1) {
		this.catListCe1 = catListCe1;
	}

	public List<Categorie> getCurrentCategorieList() {
		return currentCatList;
	}

	public void setCurrentCategorieList(List<Categorie> currentCatList) {
		this.currentCatList = currentCatList;
	}
	
	public void saveStats(){
		
		ArrayList<Categorie> catList = new ArrayList<Categorie>();
		
		if(currentProfil.getClasse().equals(Consts.CLASSE_CP)){
			catList.addAll(catListCp);
			
		}else if (currentProfil.getClasse().equals(Consts.CLASSE_CE1)){
			catList.addAll(catListCe1);
			
		}
		
		JSONObject mainJSONObject;
		JSONArray categorieArray;
		try {
			categorieArray = new JSONArray();
			mainJSONObject = new JSONObject();
			
			mainJSONObject.put("nom", currentProfil.getNom());
			mainJSONObject.put("prenom", currentProfil.getPrenom());
			mainJSONObject.put("classe", currentProfil.getClasse());
			mainJSONObject.put("categories", categorieArray);
		
		for(int i = 0; i < catList.size(); i++){
			JSONArray subCatList = new JSONArray();
			JSONObject cat = new JSONObject();
			cat.put("sub", subCatList);
			cat.put("name", catList.get(i).getNom());
			cat.put("pourcentage", catList.get(i).getPourcentage());
			cat.put("nbseriedone", catList.get(i).getNbSerieDone());
			
			for(int j = 0; j < catList.get(i).getSubCategorie().size(); j++){
				
				JSONObject subCat = new JSONObject();
				subCat.put("name", catList.get(i).getSubCategorie().get(j).getNom());
				subCatList.put(j, subCat);
				
				JSONArray seriesList = new JSONArray();
				subCat.put("series", seriesList);
				
				for (int x = 0; x < catList.get(i).getSubCategorie().get(j).getSerieList().size(); x++){
					
					JSONObject serie = new JSONObject();
					serie.put("name", catList.get(i).getSubCategorie().get(j).getSerieList().get(x).getNom());
					serie.put("note", catList.get(i).getSubCategorie().get(j).getSerieList().get(x).getNote());
					serie.put("finished", catList.get(i).getSubCategorie().get(j).getSerieList().get(x).isFinished());
					seriesList.put(x, serie);
				}
			}
			categorieArray.put(i, cat);
		}
		
		createExternalStorageSave(mainJSONObject.toString(), currentProfil.getPrenom() + currentProfil.getNom() + currentProfil.getClasse() +".json");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadStats(Profil profil){
		
		String jsonContent = getFileContent(profil.getPrenom() + profil.getNom() + profil.getClasse() + ".json");
		JSONObject mainObject;
		
		try {
			
			mainObject = new JSONObject(jsonContent);
			JSONArray catsArray = mainObject.optJSONArray("categories");
			
			for(int i = 0; i < catsArray.length(); i++){
				
				JSONObject catJSON = catsArray.optJSONObject(i);
				
				if (profil.getClasse().equals(Consts.CLASSE_CP)){
					catListCp.get(i).setPourcentage(catJSON.optInt("pourcentage"));
					catListCp.get(i).setNbSerieDone(catJSON.optInt("nbseriedone"));
				}else if (profil.getClasse().equals(Consts.CLASSE_CE1)){
					catListCe1.get(i).setPourcentage(catJSON.optInt("pourcentage"));
					catListCe1.get(i).setNbSerieDone(catJSON.optInt("nbseriedone"));
				}
		
				JSONArray subJSON = catJSON.optJSONArray("sub");
				
				for(int j = 0; j < subJSON.length(); j++){
					JSONObject subCatJSON = subJSON.optJSONObject(j);

					JSONArray seriesJSON = subCatJSON.optJSONArray("series");
					
					for (int x = 0; x < seriesJSON.length(); x++){
						JSONObject serieJSON = seriesJSON.optJSONObject(x);
						
						if (profil.getClasse().equals(Consts.CLASSE_CP)){
							catListCp.get(i).getSubCategorie().get(j).getSerieList().get(x).setNote(serieJSON.optInt("note"));
							catListCp.get(i).getSubCategorie().get(j).getSerieList().get(x).setFinished(serieJSON.optBoolean("finished"));
						}else if (profil.getClasse().equals(Consts.CLASSE_CE1)){
							catListCe1.get(i).getSubCategorie().get(j).getSerieList().get(x).setNote(serieJSON.optInt("note"));
							catListCe1.get(i).getSubCategorie().get(j).getSerieList().get(x).setFinished(serieJSON.optBoolean("finished"));
						}
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (profil.getClasse().equals(Consts.CLASSE_CP)){

		}else if (profil.getClasse().equals(Consts.CLASSE_CE1)){
			
		}
	}
	
	private String getFileContent(String fileName){
		StringBuilder textBuilder = new StringBuilder();

		File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.lpiem.apprentisage", fileName);

		if (file.exists()) {

			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;

				while ((line = br.readLine()) != null) {
					textBuilder.append(line);
				}
				br.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		

		return (textBuilder.toString());
	}
	
	private void createExternalStorageSave(String save, String fileName) {


			File folder = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.lpiem.apprentisage");

			if (!folder.exists()) {
				folder.mkdir();
			}

			File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.lpiem.apprentisage", fileName);

			try {

				FileWriter writer = new FileWriter(file, false);

				writer.write(save);

				writer.flush();
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void saveProfilList()
	{
		JSONArray profilArray = new JSONArray();
		
		for(Profil profil : listProfils)
		{
			JSONObject profilJson = new JSONObject(); 
			try
			{
				profilJson.put("nom",profil.getNom());
				profilJson.put("prenom",profil.getPrenom());
				profilJson.put("classe",profil.getClasse());
				profilJson.put("avatar",profil.getAvatar());
				
				profilArray.put(profilJson);
				createExternalStorageSave(profilArray.toString(), "profils.json");
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void loadProfilList()
	{
		listProfils.clear();
		
		try
		{
			JSONArray profilArray = new JSONArray(getFileContent("profils.json"));
			
			for(int i = 0; i < profilArray.length(); i++)
			{
				Profil profil = new Profil();
				JSONObject profilObj = profilArray.optJSONObject(i);
				
				profil.setNom(profilObj.optString("nom"));
				profil.setPrenom(profilObj.optString("prenom"));
				profil.setClasse(profilObj.optString("classe"));
				profil.setAvatar(profilObj.optInt("avatar"));
				
				listProfils.add(profil);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public void generatePoucentage()
	{
		Categorie categorie = currentCategorie;
		int nbSerieDone = 0;
		int moyenne = 0;
		
		for(Categorie subCat : categorie.getSubCategorie())
		{
			for(Serie serie : subCat.getSerieList())
			{
				if(serie.isFinished())
				{
					moyenne += serie.getNote();
					nbSerieDone++;
				}
			}
		}
		
		categorie.setPourcentage((moyenne/nbSerieDone)*10);
		categorie.setNbSerieDone(nbSerieDone);
	}
}