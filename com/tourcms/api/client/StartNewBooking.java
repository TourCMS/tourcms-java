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

public class StartNewBooking {

   private static final Logger logger = Logger.getLogger(TourSearch.class);

   private String path = "/c/booking/new/start.xml";

   private String marketplaceId = "";
   private String channelId = "";
   private String apiKey = "";

   private SignatureGenerator sg = new SignatureGenerator();

   // Constructor

   StartNewBooking (String mId, String cId, String aKey) {
     marketplaceId = mId;
     channelId = cId;
     apiKey = aKey;
   }

   /**
    * Call the TourCMS Start New Booking  API http://www.tourcms.com/support/api/mp/booking_start_new.php
    * @param xmlData String representation of XML containing details of booking to be created
    * @return String XML response from TourCMS.
    * @throws Exception
    */

   public String startNewBooking(String xmlData)throws Exception {

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

     String marketplaceId = "126";
     String channelId = "3930";
     String apiKey = "5aed2d3d69ea";

     String response = "";

     // Obtain a Component Key via a call to Check Availability: http://www.tourcms.com/support/api/mp/tour_checkavail.php
    String componentKey = "ElYPB+38q1lGvKZU2nolOYEZQQqSAzOaAcoXidc6gJPsGT0DIZ1YBlptJuuo8BCd";

     // Marketplace agents do not need to supply a Booking Key, Tour Operators do
     // Tour Operators follow the guide here to generate a Booking Key: http://www.tourcms.com/support/api/mp/booking_getkey.php
     String bookingKey = "";

     String totalCustomers = "1";

     // Create the XML to post to TourCMS
     String xmlData = "<?xml version=\"1.0\"?>"
                      +  "<booking>"
                      +     "<total_customers>" + totalCustomers + "</total_customers>";

                      // Booking Key (if there is one)
                      // Tour Operators will have generated one
                      // Marketplace Agents will skip this step
                      if(bookingKey != "") {
                        xmlData = xmlData + "<booking_key>" + bookingKey + "</booking_key>";
                      }

     xmlData = xmlData  + "<components>"
                        +   "<component>"
                        +        "<component_key>" + componentKey + "</component_key>"
                        +   "</component>"
                        + "</components>"
                        + "</booking>";


    System.out.println(xmlData);

     StartNewBooking tourcms = new StartNewBooking(marketplaceId, channelId, apiKey);

     try {
         response = tourcms.startNewBooking(xmlData);
     } catch (Exception e) {
         e.printStackTrace();
     }
    System.out.println(response);
   }

}
