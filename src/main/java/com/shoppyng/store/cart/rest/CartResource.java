package com.shoppyng.store.cart.rest;

import com.shoppyng.store.cart.domain.Cart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Implementation of CartResource.
 */
@Controller
public class CartResource extends CartResourceBase {

	public CartResource() {
	}

	@RequestMapping(value = "/cart/form", method = RequestMethod.GET)
	public String createForm(ModelMap modelMap) {
		Cart entity = new Cart();
		modelMap.addAttribute("entity", entity);
		return "cart/create";
	}

}
