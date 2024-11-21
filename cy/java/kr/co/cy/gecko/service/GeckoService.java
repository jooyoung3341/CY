package kr.co.cy.gecko.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.cy.gecko.model.Coin;

@Service
public class GeckoService {
	private final String BASE_URL = "https://api.coingecko.com/api/v3";
    String API_URL_LIST = "https://api.coingecko.com/api/v3/coins/list";
    
    final RestTemplate restTemplate = new RestTemplate();
    final ObjectMapper objectMapper = new ObjectMapper();
    
    //코인메타 가져오기
    public List<String> getCategories(String coinId) {
        String url = BASE_URL + "/coins/" + coinId;

        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("categories")) {
                return (List<String>) response.get("categories");
            } else {
                return List.of("No categories found");
            }
        } catch (Exception e) {
            return List.of("Error: " + e.getMessage());
        }
    }
    
    //시가총액 가져오기
    public Double getCoinMarketCap(String coinId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "/coins/" + coinId;

        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        if (response != null && response.has("market_data")) {
            return response.get("market_data").get("market_cap").get("usd").asDouble();
        }

        throw new RuntimeException("Failed to fetch coin data for ID: " + coinId);
    }
    
    
    // Symbol로 코인 데이터 가져오기
    public List<String> getCoinBySymbol(String symbol) {
        List<Map<String, String>> coinList = getAllCoins();
        
        // Symbol로 ID 찾기
        List<String> matchingIds = coinList.stream()
                .filter(coin -> coin.get("symbol").equalsIgnoreCase(symbol))
                .map(coin -> coin.get("id"))
                .collect(Collectors.toList());

        if (!matchingIds.isEmpty()) {
            return matchingIds;
        } else {
            throw new RuntimeException("Coin with symbol " + symbol + " not found.");
        }
    }

    // 전체 코인 목록 가져오기
    private List<Map<String, String>> getAllCoins() {
        String response = restTemplate.getForObject(BASE_URL+"/coins/list", String.class);
        try {
            return objectMapper.readValue(response, new TypeReference<List<Map<String, String>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch coin list", e);
        }
    }
    
    
    public Map<String, Object> getCoinPrice(String coinId) {
        String url = BASE_URL + "/simple/price?ids=" + coinId + "&vs_currencies=usd";

        
        //RestTemplate restTemplate = new RestTemplate();
        try {
            // API 호출
            Map<String, Object> response = (Map<String, Object>) restTemplate.getForObject(url, HashMap.class);
            return response != null ? response : Map.of("error", "No data found");
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
    
    public List<Map<String, Object>> getMarketData(List<String> coinIds) {
        String ids = String.join(",", coinIds); // 코인 ID 리스트를 콤마로 연결
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + ids
                     + "&order=market_cap_desc&per_page=10&page=1&sparkline=false";

        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(url, List.class);
        } catch (Exception e) {
            return List.of(Map.of("error", e.getMessage()));
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
