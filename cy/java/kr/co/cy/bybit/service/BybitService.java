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

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import kr.co.cy.bybit.model.Ticker;
import kr.co.cy.common.Common;

@Service
public class BybitService {
	
	@Autowired
	Common c;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getKline(String symbol, String interval, int limit) throws InvalidKeyException, NoSuchAlgorithmException{

		Map<String, Object> map = new HashMap<>();
		map.put("category", "linear");
		map.put("symbol", symbol);
		map.put("interval", interval);
		//정수
		map.put("limit", limit);
		String url = "https://api.bybit.com/v5/market/kline?";
		return c.getBybit(map, url);
	}

	public Map<String, Object> getAllTickers() throws InvalidKeyException, NoSuchAlgorithmException{
		Map<String, Object> map = new HashMap<>();
		map.put("category", "linear");
		String url = "https://api.bybit.com/v5/market/tickers?";
		return c.getBybit(map, url);
	}
}
