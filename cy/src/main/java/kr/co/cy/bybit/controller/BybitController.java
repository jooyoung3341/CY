package kr.co.cy.bybit.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.cy.bybit.model.Ticker;
import kr.co.cy.bybit.service.BybitService;

@Controller
public class BybitController {

	@Autowired
	BybitService b;

	@RequestMapping(value="/bybit", method=RequestMethod.GET)
	public String index() throws InvalidKeyException, NoSuchAlgorithmException {
		Map<String, Object> tickerData = b.getKline();  // REST API로 데이터 가져오기
		System.out.println("b : " + b);
		return "bybit/bybit";
	}
	
}
