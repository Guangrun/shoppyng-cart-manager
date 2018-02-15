package com.shoppyng.store.cart.serviceimpl;

import java.util.stream.Collectors;

import org.sculptor.framework.context.ServiceContext;
import org.sculptor.framework.errorhandling.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoppyng.store.cart.domain.Cart;
import com.shoppyng.store.cart.domain.Item;
import com.shoppyng.store.cart.domain.ItemRepository;
import com.shoppyng.store.cart.domain.Product;
import com.shoppyng.store.cart.exception.CartNotFoundException;
import com.shoppyng.store.cart.exception.ProductNotFoundException;
import com.shoppyng.store.cart.serviceapi.ProductService;

/**
 * Implementation of CartService.
 */
@Service("cartService")
public class CartServiceImpl extends CartServiceImplBase {

	@Autowired
	ProductService productService;
	
	@Autowired
	ItemRepository itemRepository;

	public CartServiceImpl() {
	}

	@Transactional(readOnly = false, rollbackFor = ApplicationException.class)
	public void addProduct(ServiceContext ctx, Long cartId, Long productId, Long quatity) {
		try {
			Cart cart = findById(ctx, cartId);
			Product product = productService.findById(ctx, productId);
			if (containsProduct(cart, product)) {
				for (Item item : cart.getItems()) {
					if (item.getProduct().getId().equals(productId)) {
						item.setQuantity(item.getQuantity() + quatity);
					}
				}

			} else {
				Item item = new Item();
				item.setCart(cart);
				item.setProduct(product);
				item.setQuantity(quatity);
				cart.addItem(item);
			}

		} catch (CartNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean containsProduct(Cart cart, Product product) {
		return cart.getItems().stream().map(i -> i.getProduct().getId()).collect(Collectors.toList())
				.contains(product.getId());
	}

	public void addProduct(ServiceContext ctx, Long cartId, Long productId) {
		addProduct(ctx, cartId, productId, 1L);
	}

	@Transactional(readOnly = false, rollbackFor = ApplicationException.class)
	public void removeProduct(ServiceContext ctx, Long cartId, Long productId) {

		Cart cart;
		try {
			cart = findById(ctx, cartId);
			Item  itemSelected = null;
			for (Item item : cart.getItems()) {
				if (item.getProduct().getId().equals(productId))
					itemSelected = item;					
			}
			if(itemSelected!=null){
				cart.removeItem(itemSelected);
				itemRepository.delete(itemSelected);
			}
		} catch (CartNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Transactional(readOnly = false, rollbackFor = ApplicationException.class)
	public Float totalPrice(ServiceContext ctx, Long cartId) {
		try {
			Cart cart = findById(ctx, cartId);
			Float total = new Float(0);
			for (Item item : cart.getItems()) {
				total = total + item.getQuantity() * item.getProduct().getPrice();
			}
			return total;
		} catch (CartNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
