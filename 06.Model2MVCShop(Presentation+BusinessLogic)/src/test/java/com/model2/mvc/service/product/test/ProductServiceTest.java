package com.model2.mvc.service.product.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

/*
 *	FileName :  UserServiceTest.java
 * ㅇ JUnit4 (Test Framework) 과 Spring Framework 통합 Test( Unit Test)
 * ㅇ Spring 은 JUnit 4를 위한 지원 클래스를 통해 스프링 기반 통합 테스트 코드를 작성 할 수 있다.
 * ㅇ @RunWith : Meta-data 를 통한 wiring(생성,DI) 할 객체 구현체 지정
 * ㅇ @ContextConfiguration : Meta-data location 지정
 * ㅇ @Test : 테스트 실행 소스 지정
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/context-common.xml",
									"classpath:config/context-aspect.xml",
									"classpath:config/context-mybatis.xml",
									"classpath:config/context-transaction.xml" })
public class ProductServiceTest {

	// ==>@RunWith,@ContextConfiguration 이용 Wiring, Test 할 instance DI
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	//@Test
	public void testAddProduct() throws Exception {

		Product product = new Product();
		product.setFileName("fileName");
		product.setManuDate("20181131");
		product.setPrice(12345);
		product.setProdDetail("prodDetail");
		product.setProdName("테스트");

		Assert.assertTrue(productService.addProduct(product)==1);

		product = productService.getProduct(10070);
		System.out.println(product);
		// ==> API 확인
		Assert.assertTrue(10070 == product.getProdNo());
		Assert.assertEquals("fileName", product.getFileName());
		Assert.assertEquals("20181131", product.getManuDate());
		Assert.assertTrue(12345 == product.getPrice());
		Assert.assertEquals("prodDetail", product.getProdDetail());
		Assert.assertEquals("테스트", product.getProdName());
	}

	//@Test
	public void testGetUser() throws Exception {

		Product product = productService.getProduct(10070);

		// ==> console 확인
		// System.out.println(user);

		// ==> API 확인
		Assert.assertNotNull(product);

		Assert.assertTrue(10070 == product.getProdNo());
		Assert.assertEquals("fileName", product.getFileName());
		Assert.assertEquals("20181131", product.getManuDate());
		Assert.assertTrue(12345 == product.getPrice());
		Assert.assertEquals("prodDetail", product.getProdDetail());
		Assert.assertEquals("테스트", product.getProdName());
	}

	//@Test
	public void testUpdateUser() throws Exception {

		Product product = productService.getProduct(10070);
		Assert.assertNotNull(product);

		Assert.assertEquals(10070, product.getProdNo());
		Assert.assertEquals("fileName", product.getFileName());
		Assert.assertEquals("20181131", product.getManuDate());
		Assert.assertEquals(12345, product.getPrice());
		Assert.assertEquals("prodDetail", product.getProdDetail());
		Assert.assertEquals("테스트", product.getProdName());

		product.setFileName("fileName2");
		product.setManuDate("20181132");
		product.setPrice(123456);
		product.setProdDetail("prodDetail2");
		product.setProdName("테스트2");

		productService.updateProduct(product);

		product = productService.getProduct(10070);
		Assert.assertNotNull(product);

		// ==> console 확인
		// System.out.println(user);

		// ==> API 확인
		Assert.assertEquals(10070, product.getProdNo());
		Assert.assertEquals("fileName2", product.getFileName());
		Assert.assertEquals("20181132", product.getManuDate());
		Assert.assertEquals(123456, product.getPrice());
		Assert.assertEquals("prodDetail2", product.getProdDetail());
		Assert.assertEquals("테스트2", product.getProdName());
	}

	// ==> 주석을 풀고 실행하면....
	@Test
	public void testGetProductListAll() throws Exception {

		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		Map<String, Object> map = productService.getProductList(search);

		List<Object> list = (List<Object>) map.get("list");
		Assert.assertEquals(3, list.size());

		Integer totalCount = (Integer) map.get("totalCount");
		Assert.assertTrue(65 == totalCount);

		System.out.println("=======================================");

		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSortCondition("2");
		search.setFilterCondition("2");
		map = productService.getProductList(search);

		list = (List<Object>) map.get("list");
		
		Assert.assertEquals(3, list.size());
		Assert.assertEquals(10002,((Product)list.get(0)).getProdNo());
		Assert.assertEquals(10005,((Product)list.get(1)).getProdNo());
		Assert.assertEquals(10007,((Product)list.get(2)).getProdNo());
		
		

		totalCount = (Integer) map.get("totalCount");
		Assert.assertTrue(64 == totalCount);
		
	}
	
	//@Test
	public void testGetRandomList() throws Exception {
		List<Product> list = productService.getRandomList(5);
		
		Assert.assertEquals(5, list.size());
	}

}