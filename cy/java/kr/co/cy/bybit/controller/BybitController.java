package kr.co.cy.bybit.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.cy.bybit.model.Ticker;
import kr.co.cy.bybit.service.BybitService;
import kr.co.cy.common.Common;

@Controller
public class BybitController {

	@Autowired
	BybitService b;
	@Autowired
	Common c;
	
	@RequestMapping(value="/bybit", method=RequestMethod.GET)
	public String bybit(Model model) throws InvalidKeyException, NoSuchAlgorithmException {

		return "bybit/bybit";
	}
	
	@ResponseBody
	@RequestMapping(value="/getTicker", method=RequestMethod.GET)
	public Map<String, Object> ticker() throws InvalidKeyException, NoSuchAlgorithmException{
		Map<String, Object> tickerMap = getTicker();
		Map<String, Object> map = new HashMap<>();
		map.put("volumeTickerList", (List<Ticker>) tickerMap.get("volumeTickerList"));
		map.put("oiTickerList", (List<Ticker>) tickerMap.get("oiTickerList"));
		
		return map;
	};
	
	public Map<String, Object> getTicker() throws InvalidKeyException, NoSuchAlgorithmException{
		Map<String, Object> result = new HashMap<>();
		
		Map<String, Object> allTickers = b.getAllTickers();
		Map<String, Object> tickers= (Map<String, Object>) b.getAllTickers().get("result");
		List<Map<String, Object>> tickerList = (List<Map<String, Object>>) tickers.get("list");
		
		List<Map<String, Object>> volumeTicker = tickerList.stream()
				.filter(entry -> {
		            String symbol = (String) entry.get("symbol");
		            // '10000' 또는 '100000'이 포함되어 있으면 제외
		            return !symbol.contains("10000") && !symbol.contains("100000");
		        })
	            .map(entry -> Map.of(
	                "symbol", entry.get("symbol"),
	                "volume24h", entry.get("volume24h")
	            ))
	            .collect(Collectors.toList());
		List<Map<String, Object>> oiTicker = tickerList.stream()
				.filter(entry -> {
		            String symbol = (String) entry.get("symbol");
		            // '10000' 또는 '100000'이 포함되어 있으면 제외
		            return !symbol.contains("10000") && !symbol.contains("100000");
		        })
	            .map(entry -> Map.of(
	                "symbol", entry.get("symbol"),
	                "openInterest", entry.get("openInterest"),
	                "openInterestValue", entry.get("openInterestValue")
	            ))
	            .collect(Collectors.toList());
		
	    // volume24h를 Long으로 정렬
	    volumeTicker = volumeTicker.stream()
	            .sorted((map1, map2) -> Long.compare(
	                    (long) Double.parseDouble((String) map2.get("volume24h")), // String을 Double로 변환 후 long으로 변환
	                    (long) Double.parseDouble((String) map1.get("volume24h"))  // String을 Double로 변환 후 long으로 변환
	            ))
	            .limit(30)
	            .collect(Collectors.toList());

	    // openInterestValue를 Long으로 정렬
	    oiTicker = oiTicker.stream()
	            .sorted((map1, map2) -> Long.compare(
	                    (long) (Double.parseDouble((String) map2.get("openInterestValue")) + Double.parseDouble((String) map2.get("openInterest"))),
	                    (long) (Double.parseDouble((String) map1.get("openInterestValue")) + Double.parseDouble((String) map1.get("openInterest")))
	            ))
	            .limit(30)
	            .collect(Collectors.toList());

		List<Ticker> vList = new ArrayList();
		int index = 0;
		for (Map<String, Object> m : volumeTicker) {
			String symbol = (String)m.get("symbol");
			Map<String, Object> kMap = b.getKline(symbol, "D", 1);
			

			Map<String, Object> rm = (Map<String, Object>) kMap.get("result");
			List<List<Object>> l = (List<List<Object>>) rm.get("list");
			
			String percent = c.percent((String)l.get(0).get(1), (String)l.get(0).get(4));
			Ticker t = new Ticker();
			t.setSymbol((String)m.get("symbol"));
			t.setVolume24h((String)m.get("volume24h"));
			t.setPercent(percent);
			vList.add(index, t);
			index += 1;
		}
		
		index = 0;
		List<Ticker> oList = new ArrayList();
		for (Map<String, Object> m : oiTicker) {
			String symbol = (String)m.get("symbol");
			Map<String, Object> kMap = b.getKline(symbol, "D", 1);
			Map<String, Object> rm = (Map<String, Object>) kMap.get("result");
			List<List<Object>> l = (List<List<Object>>) rm.get("list");

			String percent = c.percent((String)l.get(0).get(1), (String)l.get(0).get(4));
			Ticker t = new Ticker();
			t.setSymbol((String)m.get("symbol"));
			t.setOpenInterest((String)m.get("openInterest"));
			t.setOpenInterestValue((String)m.get("openInterestValue"));
			t.setPercent(percent);
			oList.add(index, t);
			index += 1;
		}
		
		result.put("volumeTickerList", vList);
		result.put("oiTickerList", oList);
		return result;
	}
	
}
