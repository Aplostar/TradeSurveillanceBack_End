package com.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.detection.DetectionAlgo;
import com.example.demo.TradeSurveillanceDatabaseApplication;
import com.export.FileWriter;
import com.generator.GenerateTradeList;
import com.pojo.Market;
import com.pojo.Trade;
import com.repository.MarketRepository;
import com.repository.TradeRepository;
import com.service.TradeService;

@RestController
public class TradeController {
	 @Autowired

	 TradeRepository dao;

	 @Autowired
	 MarketRepository market;
	 
	private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

	// @GetMapping("/trades")

	@PostMapping("/trades/add")
	public ResponseEntity<Trade> saveTrade(@RequestBody Trade trade) {

		Trade added = dao.save(trade);

		logger.info("trade added :" + trade);
		// System.out.println(trade);
		ResponseEntity<Trade> response = new ResponseEntity<Trade>(added, HttpStatus.CREATED);

		return response;
	}

	@GetMapping("/generate")
	public ResponseEntity<List<Trade>> generateTrades() {
		logger.info("Iside generate method");

		GenerateTradeList g = new GenerateTradeList();
	    List[] a = g.Generate();
	   
		
		 for (int i = 0;i<300;i++) { 
			
			  dao.save((Trade) a[0].get(i));
			  //System.out.println(a[0].get(i).toString());	
		 
			 market.save((Market) a[1].get(i));
		 }
			 
		 System.out.println("Table generated");
		 List<Trade> trade = dao.findAll();

			if (!trade.isEmpty()) {
				ResponseEntity<List<Trade>> response = new ResponseEntity<List<Trade>>(trade, HttpStatus.FOUND);
				logger.info("trades found are :" + response);
				return response;
			} else {
				ResponseEntity<List<Trade>> response = new ResponseEntity<List<Trade>>(trade, HttpStatus.NOT_FOUND);
				logger.info("trades found are :" + response);
				return response;

			}

	 }
	
		@GetMapping("/trades")
		public ResponseEntity<List<Trade>> findAllTrades() {

			List<Trade> trade = dao.findAll();

			if (!trade.isEmpty()) {
				ResponseEntity<List<Trade>> response = new ResponseEntity<List<Trade>>(trade, HttpStatus.FOUND);
				logger.info("trades found are :" + response);
				return response;
			} else {
				ResponseEntity<List<Trade>> response = new ResponseEntity<List<Trade>>(trade, HttpStatus.NOT_FOUND);
				logger.info("trades found are :" + response);
				return response;

			}

		}
		
		@GetMapping("/detection")
		public Void  findFrontRunning(){
			
			List<Trade> trade = dao.findAll();
			DetectionAlgo d=new DetectionAlgo();
			ArrayList<ArrayList<Trade>> arr =d.DetectionAl(trade);
			FileWriter f = new FileWriter();
			f.CreateTable(arr);
//			 for(int i=0;i<arr.size();i++) {
//				 
//				 for(int j=0;j<arr.get(i).size();j++) {
//					 
//					 dao.setIsChecked(arr.get(i).get(j).getTradeId());
//				 }
//			 }
			//TradeSurveillanceDatabaseApplication f = new TradeSurveillanceDatabaseApplication();
			
			
			System.out.println(arr.size());
	
			return null;
		}
	
		
		

}
