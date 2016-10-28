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

public class TourCheckAvail {

   private static final Logger logger = Logger.getLogger(TourSearch.class);

   private String path = "/c/tour/datesprices/checkavail.xml";

   private String marketplaceId = "";
   private String channelId = "";
   private String apiKey = "";

   private SignatureGenerator sg = new SignatureGenerator();

   // Constructor

   TourCheckAvail (String mId, String cId, String aKey) {
     marketplaceId = mId;
     channelId = cId;
     apiKey = aKey;
   }

   /**
    * Call the Tour CMS Check Tour Availability  API http://www.tourcms.com/support/api/mp/tour_checkavail.php
    * @param qParams NameValuePair of parameters to send to the API
    * @return XML response from TourCMS.
    * @throws Exception
    */

   public String checkAvailability(List<NameValuePair> qParams)throws Exception {

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

      // Tour ID
        // To obtain a list of tours on a given Channel call "List Tours"
        // http://www.tourcms.com/support/api/mp/tours_list.php
        // Or for a similar list, but with more info on each tour, good for building isting web pages, call "Search Tours"
        // http://www.tourcms.com/support/api/mp/tour_search.php
        qParams.add(new BasicNameValuePair("id", "1"));

      // Date
        // For a list of dates for a given tour call "Dates and Deals"
        // http://www.tourcms.com/support/api/mp/tour_datesdeals_show.php
        qParams.add(new BasicNameValuePair("date", "2016-11-02"));

      // Rates
        // To obtain a list of rates for a given tour, call "Show Tour"
        // Here just 1 of Rate 1 (r1)
        qParams.add(new BasicNameValuePair("r1", "1"));

      TourCheckAvail tourcms = new TourCheckAvail(marketplaceId, channelId, apiKey);

      try {
         response = tourcms.checkAvailability(qParams);
      } catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println(response);
   }

}
