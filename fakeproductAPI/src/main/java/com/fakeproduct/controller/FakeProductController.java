package com.fakeproduct.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fakeproduct.exceptions.EmptyListException;
import com.fakeproduct.exceptions.ProductNotFoundException;
import com.fakeproduct.models.FakeProduct;

@RestController
@RequestMapping("/products")
public class FakeProductController {
	@Value("${fakestore.url}")
	private String fakeStoreUrl;
	private RestTemplate restTemplate;
	public FakeProductController(RestTemplate restTemplate) {
		this.restTemplate=restTemplate;
	}
//	@GetMapping
//	public String test() {
//		return "Hello!!! All is well!!!";
//	}
	@GetMapping("/{id}")
	public FakeProduct getProductById(@PathVariable("id")Integer id) throws ProductNotFoundException {
		ResponseEntity<FakeProduct> response=restTemplate.getForEntity(fakeStoreUrl+"/"+id, FakeProduct.class);
		FakeProduct fakeProduct=response.getBody();
		if(fakeProduct==null) {
			throw new ProductNotFoundException("No product available");
		}
		return fakeProduct;
	}
	@GetMapping
	public FakeProduct[] getAllProducts() throws EmptyListException {
		ResponseEntity<FakeProduct[]> response=restTemplate.getForEntity(fakeStoreUrl, FakeProduct[].class);
		FakeProduct[] list=response.getBody();
		if(list==null || list.length==0) {
			throw new EmptyListException("No product to show");
		}
		return list;
	}
	@PostMapping
	public FakeProduct createProduct(@RequestBody FakeProduct fakeProduct) {
		ResponseEntity<FakeProduct> response=restTemplate.postForEntity(fakeStoreUrl,fakeProduct, FakeProduct.class);
		FakeProduct newProduct=response.getBody();
		System.out.println(newProduct);
		return newProduct;
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id")Integer id) throws ProductNotFoundException {
		ResponseEntity<FakeProduct> response=restTemplate.getForEntity(fakeStoreUrl+"/"+id, FakeProduct.class);
		FakeProduct deletedProduct=response.getBody();
		if(deletedProduct==null) {
			throw new ProductNotFoundException("Product not available");
		}
		return new ResponseEntity("Product with id "+id+" Deleted",HttpStatus.OK);
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("id")Integer id, @RequestBody FakeProduct fakeProduct) {
		System.out.println("Received "+id+" product="+fakeProduct);
		
		HttpEntity<FakeProduct> entity=new HttpEntity<FakeProduct>(fakeProduct);
		ResponseEntity<FakeProduct> response=restTemplate.exchange(fakeStoreUrl+"/"+id,HttpMethod.PUT,entity,FakeProduct.class);
		//restTemplate.put(fakeStoreUrl+"/"+id, fakeProduct);
		System.out.println(response.getBody());
		Map<String,String> responseData=new HashMap<>();
		responseData.put("id", String.valueOf(id));
		responseData.put("msg", "Product Updated");
		
		return new ResponseEntity(responseData,HttpStatus.OK);
	}
}
