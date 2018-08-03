package com.rme.jwt.endpoint;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/floor1")
public class FirstFloorEndpoint {
	
	@GetMapping("office1")
	public ResponseEntity<?> enteroffice1(){
		return new ResponseEntity<>("inside office 1", HttpStatus.OK);
	}
	@GetMapping("office2")
	public ResponseEntity<?> enteroffice2(){
		return new ResponseEntity<>("inside office 2", HttpStatus.OK);
	}
}
