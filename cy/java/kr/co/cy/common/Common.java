package kr.co.cy.common;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.util.logging.Commons;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class Common {

    final static String API_KEY  = "fLfrnDiVOeEqwXus6y";
    final static String API_SECRET  = "dCVX8PJYCwAIMv6QCitDaF1CtfTjL7iIR6m1";
    final static String TIMESTAMP = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
    final static String RECV_WINDOW = "5000";

	public String timestempToKst(String ms) {
        // Unix 타임스탬프 (밀리초)
        long timestamp = Long.parseLong(ms);
        // Instant로 변환 (UTC 기준)
        Instant instant = Instant.ofEpochMilli(timestamp);
        // 한국 시간대 (UTC+9) 적용
        ZonedDateTime kstTime = instant.atZone(ZoneId.of("Asia/Seoul"));
        // 포맷터로 보기 쉽게 출력
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return kstTime.format(formatter);
	}
	
	public String percent(String openPrice, String closePrice) {
		double dOpenPrice = Double.parseDouble(openPrice);
		double dClosePrice = Double.parseDouble(closePrice);
		
		DecimalFormat df = new DecimalFormat("0.00");
		double result = (dClosePrice - dOpenPrice) / dOpenPrice * 100;
		 return df.format(result);
	}
	
	public String usdToKrw(double usd, double cap) {
		double result = cap * usd;
		DecimalFormat df = new DecimalFormat("#,###");
		
		return df.format(result);
	}
	
	public Map<String, Object> getBybit(Map<String, Object> map, String url) throws InvalidKeyException, NoSuchAlgorithmException{
		String signature = genGetSign(map);
		StringBuilder sb = genQueryStr(map);

		 OkHttpClient client = new OkHttpClient().newBuilder().build();
	        Request request = new Request.Builder()
	                .url(url + sb)
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
	            
	            String responseBody = response.body().string();
	            
	            ObjectMapper objectMapper = new ObjectMapper();
	            return objectMapper.readValue(responseBody, Map.class);
	        }catch (IOException e){
	            e.printStackTrace();
	            return new HashMap<>();
	        }
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
