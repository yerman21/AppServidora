package com.webservice.appservidora.controller;

import java.util.HashMap;
import java.util.Map;

//Autor: Yerry Aguirre :)
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.webservice.appservidora.util.CodeQR;

@RestController
@RequestMapping("/webserver")
@CrossOrigin(origins = "http://localhost", maxAge=10)
//@WebService(serviceName="TestWebService")
public class WebServerController {
	
//	@WebMethod
//	@WebResult(name="imageQR")
	@GetMapping("/v1/toQR")
	public ResponseEntity<String> convertToQR(/*@WebParam(name="value")*/ @RequestParam("value") String value) {
		if(value == null || value.isEmpty())
			return new ResponseEntity("el value es requerido para generar el codigo QR", HttpStatus.NO_CONTENT);

		String bytes64 = CodeQR.getBase64QR(value);

		if(bytes64 == null || bytes64.isEmpty())
			return new ResponseEntity("no se pudo generar el codigo QR", HttpStatus.CONFLICT);
		
		HashMap<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("msg", "Conversion a QR fue exitosa");
		respuesta.put("data", bytes64);
		
		return ResponseEntity.ok().body( new Gson().toJson(respuesta));
	}
	
	@PostMapping("/v1/decodeQR")
	public ResponseEntity<String> decodeQR(@RequestBody byte[] jsonData) {
		System.out.println(jsonData);
		
		if(jsonData == null || jsonData.length == 0)
			return new ResponseEntity("{'msg': 'el imgBytes es requerido para decodificar el codigo QR'}", HttpStatus.NO_CONTENT);
		
		//Map<String, Object> map = new Gson().fromJson(jsonData, Map.class);		
		String infoQR = CodeQR.decodeQR(jsonData);//(byte[])map.get("imgBytes"));
		
		if(infoQR == null || infoQR.isEmpty())
			return new ResponseEntity("{'msg': 'no se pudo decodifiar el codigo QR'}", HttpStatus.CONFLICT);
		
		HashMap<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("msg", "Decodificacion de QR exitosa");
		respuesta.put("data", infoQR);
		System.out.println(new Gson().toJson(respuesta));
		return ResponseEntity.ok().body( new Gson().toJson(respuesta));
	}
}
