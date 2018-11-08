package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@Controller
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public PurchaseController() {
		System.out.println("PurchaseController default constructor call");
	}
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addPurchaseView( @RequestParam("prod_no") int prodNo) throws Exception {

		System.out.println("/addPurchaseView.do");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/addPurchaseView.jsp");
		modelAndView.addObject("purchaseProd", productService.getProduct(prodNo));
		
		return modelAndView;
	}
	
	@RequestMapping("/addPurchase.do")
	public ModelAndView addPurchase( @RequestParam("prodNo") int prodNo, @RequestParam("buyerId") String buyerId,
										@ModelAttribute("purchase") Purchase purchase ) throws Exception {

		System.out.println("/addPurchase.do");
		
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchase.setBuyer(userService.getUser(buyerId));
		purchase.setTranCode("1");
		
		purchaseService.addPurchase(purchase);
		
		purchase = purchaseService.getPurchase2(prodNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/confirmPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping("/getPurchase.do")
	public ModelAndView getPurchase( @RequestParam("tranNo") int tranNo ) throws Exception {
		
		System.out.println("/getPurchase.do");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase",purchaseService.getPurchase(tranNo));
		
		return modelAndView;
	}
	
	/*@RequestMapping("/updateProductView.do")
	public String updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		Product product=productService.getProduct(prodNo);
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct( @ModelAttribute("product") Product product , Model model) throws Exception{

		System.out.println("/updateProduct.do");
		productService.updateProduct(product);
		
		return "redirect:/getProduct.do?prodNo="+product.getProdNo()+"&menu=manage";
	}

	@RequestMapping("/listProduct.do")
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpSession session) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(	search.getCurrentPage()==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String , Object> map=productService.getProductList(search);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model °ú View ¿¬°á
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		String dest = "forward:/product/listProductUser.jsp";
		if (session.getAttribute("user")!=null && ((User)session.getAttribute("user")).getRole().equals("admin"))
			dest = "forward:/product/listProduct.jsp";
		
		return dest;
	}*/
	
	
}
