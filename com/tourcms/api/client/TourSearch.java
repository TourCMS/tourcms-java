package com.tourcms.api.client;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class TourSearch {

   private static final Logger logger = Logger.getLogger(TourSearch.class);

   private String path = "/p/tours/search.xml";

   private String marketplaceId = "";
   private String channelId = "";
   private String apiKey = "";

   private SignatureGenerator sg = new SignatureGenerator();

   // Constructor

   TourSearch (String mId, String cId, String aKey) {
     marketplaceId = mId;
     channelId = cId;
     apiKey = aKey;

     if(channelId != "0") {
       path = "/c/tours/search.xml";
     }
   }

   /**
    * Call the Tour CMS Search Tours API http://www.tourcms.com/support/api/mp/tour_search.php
    * @param qParams NameValuePair of parameters to send to the API
    * @return XML response from TourCMS.
    * @throws Exception
    */


   public String searchTours(List<NameValuePair> qParams)throws Exception {

      String body = null;

      // Add our query string to our base path
      URIBuilder uriBuilder = new URIBuilder(path);
      uriBuilder.addParameters(qParams);
      URI uri = uriBuilder.build();

      logger.info(uri);

      ApiRequester tourcms = new ApiRequester(marketplaceId, channelId, apiKey);

      try {
         body = tourcms.apiRequest(uri.toString(), "GET", null);
      }
      catch (IOException ioe) {
         //logger.error("error sending request: " + ioe.getMessage());
         throw new Exception(ioe.getMessage(), ioe);
      }

      return body;
   }

   /**
    * test only
    * @param args
    */
   public static void main(String[] args) {

     String marketplaceId = "";
     String channelId = "";
     String apiKey = "";

     String response = "";

      // Query string params
        List<NameValuePair> qParams = new ArrayList<NameValuePair>();
        // Keyword "hiking"
        qParams.add(new BasicNameValuePair("k", "rafting"));
        // Limit to 1 result per page
        qParams.add(new BasicNameValuePair("per_page", "1"));

      TourSearch tourcms = new TourSearch(marketplaceId, channelId, apiKey);

      try {
         response = tourcms.searchTours(qParams);
      } catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println(response);
   }

}
