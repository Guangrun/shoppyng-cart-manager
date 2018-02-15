package com.shoppyng.store.cart.rest;

import com.shoppyng.store.cart.domain.Product;
import com.shoppyng.store.cart.exception.ProductNotFoundException;
import com.shoppyng.store.cart.serviceapi.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Implementation of ProductResource.
 */
@Controller
public class ProductResource extends ProductResourceBase {

	@Autowired
	ProductService productService;
	
	public ProductResource() {
	}

	@RequestMapping(value = "/findProductById/{id}", method = RequestMethod.GET)
	public Product findProductById(@PathVariable("id") Long id) {
		try {
			return productService.findById(serviceContext(), id);
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
