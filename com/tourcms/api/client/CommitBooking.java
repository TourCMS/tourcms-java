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

public class CommitBooking {

   private static final Logger logger = Logger.getLogger(CommitBooking.class);

   private String path = "/c/booking/new/commit.xml";

   private String marketplaceId = "";
   private String channelId = "";
   private String apiKey = "";

   private SignatureGenerator sg = new SignatureGenerator();

   // Constructor

   CommitBooking (String mId, String cId, String aKey) {
     marketplaceId = mId;
     channelId = cId;
     apiKey = aKey;
   }

   /**
    * Call the TourCMS Commit Booking  API http://www.tourcms.com/support/api/mp/booking_start_new.php
    * @param xmlData String representation of XML containing details of booking to be committed
    * @return String XML response from TourCMS.
    * @throws Exception
    */

   public String commitBooking(String xmlData)throws Exception {

      String body = null;

      URIBuilder uriBuilder = new URIBuilder(path);
      URI uri = uriBuilder.build();

      logger.info(uri);

      ApiRequester tourcms = new ApiRequester(marketplaceId, channelId, apiKey);

      try {
         body = tourcms.apiRequest(uri.toString(), "POST", xmlData);
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

     // Booking ID to commit
     // Obtained in response to Start New Booking http://www.tourcms.com/support/api/mp/booking_start_new.php
    String bookingID = "";

     // Create the XML to post to TourCMS
     String xmlData = "<?xml version=\"1.0\"?>"
                      +  "<booking>"
                      +     "<booking_id>" + bookingID + "</booking_id>"
                      + "</booking>";


    System.out.println(xmlData);

     CommitBooking tourcms = new CommitBooking(marketplaceId, channelId, apiKey);

     try {
         response = tourcms.commitBooking(xmlData);
     } catch (Exception e) {
         e.printStackTrace();
     }
    System.out.println(response);
   }

}
