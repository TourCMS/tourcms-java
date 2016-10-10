package com.tourcms.api.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class SignatureGenerator {

   //private static String Marketplace_Partner_ID = "126";
   //private static String API_Private_Key = "5aed2d3d69ea";

   public String encode(String marketplaceId, String key, String path, String verb, String channel, long time) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
      String toSign = new String(channel+"/"+marketplaceId+"/"+verb+"/"+(String.valueOf(time/1000))+path);
      Mac mac = Mac.getInstance("HmacSha256");
      SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSha256");
      mac.init(secret);
      byte[] shaDigest = mac.doFinal(toSign.getBytes("UTF-8"));
      String result =  new String(Base64.encodeBase64(shaDigest));
      return URLEncoder.encode(result, "UTF-8");
   }

}
