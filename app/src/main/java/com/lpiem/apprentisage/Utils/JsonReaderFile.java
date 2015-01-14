/**
 * Created by Nicolas on 13/01/2015.
 */
package com.lpiem.apprentisage.Utils;

import android.app.Activity;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.model.Categorie;
import com.lpiem.apprentisage.model.Serie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonReaderFile {
    private Activity mActivity;

    public JsonReaderFile(Activity activity){
        mActivity = activity;
    }
    private String getFileContent(String fileName){
        StringBuilder mLine = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mActivity.getAssets().open("datas/" + fileName), "UTF-8"));
            String str;

            while ((str = reader.readLine()) != null) {
                mLine.append(str);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mLine.toString();
    }

    public void loadJson(String fileName)
    {
        int[] colors = {
                mActivity.getResources().getColor(R.color.cat_blue),
                mActivity.getResources().getColor(R.color.cat_green),
                mActivity.getResources().getColor(R.color.cat_orange),
                mActivity.getResources().getColor(R.color.cat_red),
                mActivity.getResources().getColor(R.color.cat_violet)
        };

        String jsonCP = getFileContent(fileName);

        try {
            JSONArray categorieArray = new JSONArray(jsonCP);
            for (int i = 0; i < categorieArray.length(); i ++){
                JSONObject catJson = categorieArray.getJSONObject(i);

                // Initialisation categorie
                Categorie cat = new Categorie();
                cat.setNom(catJson.optString("name"));
                cat.setColor(colors[i]);

                // Initialisation sous categorie
                JSONArray subArray = catJson.optJSONArray("sub");
                for(int j = 0; j < subArray.length(); j++)
                {
                    JSONObject subJson = subArray.getJSONObject(j);
                    Categorie subCat = new Categorie();

                    subCat.setNom(subJson.optString("name"));
                    subCat.setMode(subJson.optString("mode"));

                    // Initialisation liste series
                    List<Serie> listSerie = new ArrayList<>();
                    JSONArray serieArray = subJson.optJSONArray("series");

                    for(int x = 0; x < serieArray.length(); x++)
                    {
                        JSONObject serieJson = serieArray.optJSONObject(x);
                        JSONArray reponseArray = serieJson.optJSONArray("content");
                        List<String> reponses = new ArrayList<>();

                        Serie serie = new Serie();
                        serie.setNom(serieJson.optString("name"));

                        for(int y = 0; y < reponseArray.length(); y++)
                            reponses.add(reponseArray.getString(y));

                        serie.setReponses(reponses);
                        listSerie.add(serie);
                    }

                    //subCat.setSerieList(listSerie);
                    cat.getSubCategorie().add(subCat);
                }

                if(fileName.equals("cp.json"))
                    Shared.getInstance().getCategorieListCp().add(cat);
                else if(fileName.equals("ce1.json"))
                    Shared.getInstance().getCategorieListCe1().add(cat);
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
}
