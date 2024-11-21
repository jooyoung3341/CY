package kr.co.cy.gecko.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.cy.common.Common;
import kr.co.cy.gecko.model.Coin;
import kr.co.cy.gecko.service.GeckoService;

@Controller
public class GeckoController {

	@Autowired
	private GeckoService service;
	
	@Autowired
	private Common c;
	
	@ResponseBody
	@RequestMapping(value="/gecko", method=RequestMethod.GET)
	public Coin bybit(HttpServletRequest request) throws InvalidKeyException, NoSuchAlgorithmException {
		String symbol = request.getParameter("symbol");
		System.out.println(symbol);
		List<String> map = service.getCoinBySymbol(symbol);
        List<String> id = map.stream()
                .filter(str -> !str.contains("-"))  // "-" 미포함 문자열만 필터링
                .collect(Collectors.toList());
        System.out.println(id);
        System.out.println("size : "  + id.size());
        if(id.size() > 0) {
	        String coidId = id.get(0);
	        List<String> category = service.getCategories(coidId);
	        
	        String cap = c.usdToKrw(1400.0, service.getCoinMarketCap(coidId));
	        Coin c = new Coin();
	        c.setId(coidId);
	        c.setMeta(category);
	        c.setMarketCap(cap);
	        c.setSymbol(symbol);
	        
	
			return c;
        }
        Coin c1 = new Coin();
    	c1.setError("E");
    	return c1;
	}
}
