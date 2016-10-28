package com.tourcms.api.client;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ApiRequester {

   private static final Logger logger = Logger.getLogger(TourSearch.class);

   private String scheme = "https";
   private String host = "api.tourcms.com";

   private String marketplaceId = "";
   private String channelId = "";
   private String apiKey = "";

   private SignatureGenerator sg = new SignatureGenerator();

   // Constructor

   ApiRequester (String mId, String cId, String aKey) {
     marketplaceId = mId;
     channelId = cId;
     apiKey = aKey;
   }

   // Request the endpoint

   public String apiRequest(String path, String verb, String postData)throws Exception {

      String body = null;

      long time = System.currentTimeMillis();
      DateTime dt = new DateTime(time);
      DateTime dtGmt = dt.withZone(DateTimeZone.UTC);
      DateTimeFormatter fmt = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss zzz");
      String currDate = fmt.print(dtGmt);

      String uri = scheme + "://" + host + path;

      if(verb == "POST") {
        HttpPost httpDetails = new HttpPost(uri);
        HttpEntity postEntity = new ByteArrayEntity(postData.getBytes("UTF-8"));
        httpDetails.setEntity(postEntity);
        httpDetails.setHeader("x-tourcms-date", currDate);
        String sUri = httpDetails.getURI().toString();

        sUri = sUri.substring(23);
        logger.info(sUri);
        String authString = sg.encode(marketplaceId, apiKey, sUri, verb, channelId, time);
        logger.info(authString);
        httpDetails.setHeader("Authorization", "TourCMS " + channelId + ":"+marketplaceId+":"+authString);
        httpDetails.setHeader("Content-type", "text/xml;charset=\"utf-8\"");

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        try {
           response = httpclient.execute(httpDetails);
        }
        catch (IOException ioe) {
           //logger.error("error sending request: " + ioe.getMessage());
           throw new Exception(ioe.getMessage(), ioe);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
           try {
              body = EntityUtils.toString(entity);
              httpDetails.abort();
           }
           catch (IOException ioe2) {
  //            logger.error("error extracting entity from post response: " + ioe2.getMessage());

           }
        }

         return body;
      } else {
        HttpGet httpDetails = new HttpGet(uri);
        httpDetails.setHeader("x-tourcms-date", currDate);
        String sUri = httpDetails.getURI().toString();

        sUri = sUri.substring(23);
        logger.info(sUri);
        String authString = sg.encode(marketplaceId, apiKey, sUri, verb, channelId, time);
        logger.info(authString);
        httpDetails.setHeader("Authorization", "TourCMS " + channelId + ":"+marketplaceId+":"+authString);
        httpDetails.setHeader("Content-type", "text/xml;charset=\"utf-8\"");

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        try {
           response = httpclient.execute(httpDetails);
        }
        catch (IOException ioe) {
           //logger.error("error sending request: " + ioe.getMessage());
           throw new Exception(ioe.getMessage(), ioe);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
           try {
              body = EntityUtils.toString(entity);
              httpDetails.abort();
           }
           catch (IOException ioe2) {
  //            logger.error("error extracting entity from post response: " + ioe2.getMessage());

           }
        }

         return body;
      }


   }

   /**
    * test only
    * @param args
    */
   public static void main(String[] args) {

     String marketplaceId = "";
     String channelId = "0";
     String apiKey = "";

      String rsp = "";
      ApiRequester tourcms = new ApiRequester(marketplaceId, channelId, apiKey);
      try {
         rsp = tourcms.apiRequest("/p/tours/search.xml", "GET", null);

      } catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println(rsp);
   }

}
