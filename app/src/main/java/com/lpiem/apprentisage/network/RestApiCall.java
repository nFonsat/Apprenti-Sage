/**
 * Created by Nicolas on 10/01/2015.
 */
package com.lpiem.apprentisage.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RestApiCall {

    public final static String ENCODAGE = "UTF-8";

    public enum RestApiCallMethod{
        GET,
        POST,
        PUT,
        DELETE
    }

    //Variables
    private ArrayList<NameValuePair> mParams;
    private ArrayList<NameValuePair> mHeaders;
    private String mUrl;
    private int mResponseCode;
    private String mErrorMessage;
    private String mResponse;

    public int getResponseCode() {
        return mResponseCode;
    }
    public String getErrorMessage() {
        return mErrorMessage;
    }
    public String getResponse() {
        return mResponse;
    }

    //Constructor
    public RestApiCall(String url){
        mUrl = url;
        mParams = new ArrayList<NameValuePair>();
        mHeaders = new ArrayList<NameValuePair>();
    }

    //Methods
    public void addParameter(NameValuePair param){
        mParams.add(param);
    }
    public void addHeader(NameValuePair header){
        mHeaders.add(header);
    }
    public void executeRequest(RestApiCallMethod restApiCallMethod){
        HttpUriRequest request;

        switch (restApiCallMethod){
            case GET:
                StringBuilder callParams =  new StringBuilder();
                if(!mParams.isEmpty()){
                    callParams.append("?");
                    for(NameValuePair param : mParams){
                        StringBuilder paramToString = new StringBuilder();
                        paramToString.append(paramToString);
                        paramToString.append("=");
                        try {
                            paramToString.append(URLEncoder.encode(param.getValue(), ENCODAGE));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        if(callParams.length() == 1)
                            callParams.append(paramToString.toString());
                        else
                            callParams.append("&").append(paramToString.toString());
                    }
                }
                request = new HttpGet(mUrl + callParams.toString());
                for(NameValuePair header : mHeaders){
                    request.addHeader(header.getName(), header.getValue());
                }

                launchRequest(request);
                break;
            case POST:
                request = new HttpPost(mUrl);
                for(NameValuePair header : mHeaders){
                    request.addHeader(header.getName(), header.getValue());
                }

                if(!mParams.isEmpty())
                    try {
                        ((HttpPost)request).setEntity(new UrlEncodedFormEntity(mParams, ENCODAGE));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                launchRequest(request);
                break;
            case PUT:
                request = new HttpPut(mUrl);
                for(NameValuePair header : mHeaders){
                    request.addHeader(header.getName(), header.getValue());
                }

                if(!mParams.isEmpty())
                    try {
                        ((HttpPut)request).setEntity(new UrlEncodedFormEntity(mParams, ENCODAGE));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                launchRequest(request);
                break;
            case DELETE:
                request = new HttpDelete(mUrl);
                for(NameValuePair header : mHeaders){
                    request.addHeader(header.getName(), header.getValue());
                }
                launchRequest(request);
                break;
        }
    }

    private void launchRequest(HttpUriRequest request){
        HttpClient client = new DefaultHttpClient();

        HttpResponse response;

        try {
            response = client.execute(request);
            mResponseCode = response.getStatusLine().getStatusCode();
            mErrorMessage = response.getStatusLine().getReasonPhrase();

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream stream = entity.getContent();
                mResponse = createResponse(stream);
                stream.close();
            }
        }
        catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    private String createResponse(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}