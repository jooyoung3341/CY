package kr.co.cy.bybit.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import org.springframework.http.HttpEntity;
import kr.co.cy.bybit.model.Ticker;

@Service
public class BybitService {
	
    final static String API_KEY  = "fLfrnDiVOeEqwXus6y";
    final static String API_SECRET  = "dCVX8PJYCwAIMv6QCitDaF1CtfTjL7iIR6m1";
    final static String TIMESTAMP = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
    final static String RECV_WINDOW = "5000";
 
	public Map<String, Object> getKline() throws InvalidKeyException, NoSuchAlgorithmException{
		System.out.println("들옴");
		System.out.println("샤ㅡㄷ : " + TIMESTAMP );
		
		Map<String, Object> map = new HashMap<>();
		map.put("category", "spot");
		map.put("symbol", "BTCUSDT");
		map.put("interval", "15");
		map.put("limit", 5);
		
		
		String signature = genGetSign(map);
		StringBuilder sb = genQueryStr(map);

		 OkHttpClient client = new OkHttpClient().newBuilder().build();
	        Request request = new Request.Builder()
	                .url("https://api-testnet.bybit.com/v5/market/kline?" + sb)
	                .get()
	                .addHeader("X-BAPI-API-KEY", API_KEY)
	                .addHeader("X-BAPI-SIGN", signature)
	                .addHeader("X-BAPI-SIGN-TYPE", "2")
	                .addHeader("X-BAPI-TIMESTAMP", TIMESTAMP)
	                .addHeader("X-BAPI-RECV-WINDOW", RECV_WINDOW)
	                .build();
	        Call call = client.newCall(request);
	        try {
	            Response response = call.execute();
	            assert response.body() != null;
	            System.out.println(response.body().string());
	        }catch (IOException e){
	            e.printStackTrace();
	        }
		return new HashMap<>();
	}

	   private static String genGetSign(Map<String, Object> params) throws NoSuchAlgorithmException, InvalidKeyException {
	        StringBuilder sb = genQueryStr(params);
	        String queryStr = TIMESTAMP + API_KEY + RECV_WINDOW + sb;

	        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	        SecretKeySpec secret_key = new SecretKeySpec(API_SECRET.getBytes(), "HmacSHA256");
	        sha256_HMAC.init(secret_key);
	        return bytesToHex(sha256_HMAC.doFinal(queryStr.getBytes()));
	    }
	   
	    private static StringBuilder genQueryStr(Map<String, Object> map) {
	        Set<String> keySet = map.keySet();
	        Iterator<String> iter = keySet.iterator();
	        StringBuilder sb = new StringBuilder();
	        while (iter.hasNext()) {
	            String key = iter.next();
	            sb.append(key)
	                    .append("=")
	                    .append(map.get(key))
	                    .append("&");
	        }
	        sb.deleteCharAt(sb.length() - 1);
	        return sb;
	    }
	    
	    private static String bytesToHex(byte[] hash) {
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : hash) {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }
	        return hexString.toString();
	    }

}
