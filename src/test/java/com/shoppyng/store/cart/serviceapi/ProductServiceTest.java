package com.shoppyng.store.cart.serviceapi;

import com.shoppyng.store.cart.domain.Product;
import com.shoppyng.store.cart.serviceapi.ProductService;
import org.junit.Test;
import org.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

import java.util.List;

/**
 * Spring based transactional test with DbUnit support.
 */

/**
 * 
 * @author glin DataSet file: src/test/resources/dbunit/ProductServiceTest.xml
 *
 */
public class ProductServiceTest extends AbstractDbUnitJpaTests implements ProductServiceTestBase {

	@Autowired
	protected ProductService productService;

	@Test
	public void testFindById() throws Exception {
		Product product = productService.findById(getServiceContext(), 1L);
		assertTrue(product.getId() == 1L);
		assertEquals("Pen", product.getProductName());
		assertEquals(new Float(1.56), product.getPrice());

		product = productService.findById(getServiceContext(), 4L);
		assertTrue(product.getId() == 4L);
		assertEquals("SmartPhone", product.getProductName());
		assertEquals(new Float(998), product.getPrice());
	}

	@Test
	public void testFindAll() throws Exception {
		List<Product> all = productService.findAll(getServiceContext());
		assertEquals(4, all.size());
	}

	@Test
	public void testSave() throws Exception {
		assertEquals(4, countRowsInTable(Product.class));
		Product newProduct = new Product();
		newProduct.setProductName("newProduct");
		newProduct.setPrice(new Float(15.5));
		productService.save(getServiceContext(), newProduct);
		assertEquals(5, countRowsInTable(Product.class));
	}
}
