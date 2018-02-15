package com.shoppyng.store.cart.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.springframework.beans.factory.annotation.Autowired;

import com.shoppyng.store.cart.domain.Cart;
import com.shoppyng.store.cart.domain.Item;

/**
 * Spring based transactional test with DbUnit support.
 */
public class CartServiceTest extends AbstractDbUnitJpaTests implements CartServiceTestBase {

	@Autowired
	protected CartService cartService;

	@Test
	public void testFindById() throws Exception {
		Cart cart = cartService.findById(getServiceContext(), 1L);
		assertEquals(new Long(1L), cart.getId());
		assertEquals(new Long(15L), cart.getBuyerId());		
		cart = cartService.findById(getServiceContext(), 2L);
		assertEquals(new Long(2L), cart.getId());
		assertEquals(new Long(50L), cart.getBuyerId());	
	}

	@Test
	public void testFindAll() throws Exception {
		List<Cart> all = cartService.findAll(getServiceContext());
		assertEquals(2, all.size());
	}

	@Test
	public void testSave() throws Exception {
		int all = countRowsInTable(Cart.class);
		Cart cart = new Cart();
		cart.setBuyerId(80L);
		cartService.save(getServiceContext(), cart);		
		assertEquals(all+1, countRowsInTable(Cart.class));		
	}


	@Test
	public void testAddProduct() throws Exception {
		// Before add
		
		Long productId = 2L;
		
		Cart cart = cartService.findById(getServiceContext(), 1L);
		List<Long> productIds =cart.getItems().stream().map(i->i.getProduct().getId()).collect(Collectors.toList());
		
		assertTrue(!productIds.contains(new Long(productId)));
		
		
		// Case 1:  product not yet in the cart
		Long cartId = cart.getId();
		
		cartService.addProduct(getServiceContext(), cartId, productId, 1L);
		
		// After addition
		Cart cart1 = cartService.findById(getServiceContext(), 1L);
		assertTrue(cart1.getItems().stream().map(i->i.getProduct().getId()).collect(Collectors.toList()).contains(new Long(productId)));
		for(Item item: cart1.getItems()){
			if(item.getProduct().getId().equals(productId)){
				assertEquals(new Long(1L),  item.getQuantity());
			}
		}
		
	}
	
	
	@Test
	public void testAddProductHasExisted() throws Exception {
		// Before add
		
		Long productId = 1L;
		
		Cart cart = cartService.findById(getServiceContext(), 1L);
		List<Long> productIds =cart.getItems().stream().map(i->i.getProduct().getId()).collect(Collectors.toList());
		
		// Product has been in the cart
		assertTrue(productIds.contains(productId));
		
		Long originalQuantity = 0L;
		
		for(Item item : cart.getItems()){
			if(item.getProduct().getId().equals(productId)){
				originalQuantity=item.getQuantity();
			}
		}
		
		
		Long cartId = cart.getId();
		
		cartService.addProduct(getServiceContext(), cartId, productId, 3L);
		
		// After addition
		Cart cart1 = cartService.findById(getServiceContext(), 1L);
		assertTrue(cart1.getItems().stream().map(i->i.getProduct().getId()).collect(Collectors.toList()).contains(new Long(productId)));
		for(Item item: cart1.getItems()){
			if(item.getProduct().getId().equals(productId)){
				assertEquals(new Long(originalQuantity+3L),  item.getQuantity());
			}
		}
		
	}

	@Test
	public void testRemoveProduct() throws Exception {
		Long productId = 1L;
		Long cartId = 1L;
		Cart cart = cartService.findById(getServiceContext(), cartId);
		assertTrue(containsProduct(cart,productId));
		cartService.removeProduct(getServiceContext(), cartId, productId);
		
		cart = cartService.findById(getServiceContext(), cartId);
		assertTrue(!containsProduct(cart,productId));
	}

	private boolean containsProduct(Cart cart, Long productId) {
		// TODO Auto-generated method stub
		return cart.getItems().stream().filter(i->i.getProduct().getId().equals(productId)).count()>=1;
	}


	@Test
	public void testTotalPrice() throws Exception {
		Float totalPrice = cartService.totalPrice(getServiceContext(), 1L);
	    Float expected = new Float(1.56*3 + 998);
	    assertEquals(expected, totalPrice);
	}
}
