package com.tourcms.api.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;


public class Example {

   private static final Logger logger = Logger.getLogger(TourSearch.class);

   /**
    * Example calls TourCMS Tour Search
    * @param args
    */
   public static void main(String[] args) {

     // Load your API credentials
       // Tour Operators set this to 0 (zero)
       // Marketplace Agents have a Marketplace ID, and MUST enter it here
       String marketplaceId = "YOUR_MARKETPLACE_ID";
       // Tour Operators MUST set their Channel ID here
       // Marketplace Agents CAN optionally set a Channel here to search just in that channel, otherwise set to 0 (zero)
       String channelId = "CHANNEL_ID";
       // API Key (found via TourCMS Settings)
       String apiKey = "YOUR_API_KEY";

      // Optionally set some query string params
      // http://www.tourcms.com/support/api/mp/tour_search.php
        List<NameValuePair> qParams = new ArrayList<NameValuePair>();
        // Keyword "hiking"
          // qParams.add(new BasicNameValuePair("k", "rafting"));
        // Limit to 1 result per page
          // qParams.add(new BasicNameValuePair("per_page", "1"));


      // String to hold the response
        String response = "";

      TourSearch tourcms = new TourSearch(marketplaceId, channelId, apiKey);

      try {
         response = tourcms.searchTours(qParams);
      } catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println(response);
   }

}
