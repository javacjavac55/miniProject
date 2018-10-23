package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class AddPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		String buyerId=request.getParameter("buyerId");
		
		ProductService prodService = new ProductServiceImpl();
		Product purchaseProd = prodService.getProduct(prodNo);
		UserService userService = new UserServiceImpl();
		User buyer = userService.getUser(buyerId);
		
		Purchase purchase = new Purchase();
		purchase.setPurchaseProd(purchaseProd);
		purchase.setBuyer(buyer);
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("receiverDate"));
		//for status
		purchase.setTranCode("구매완료");
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		purchaseService.addPurchase(purchase);
		
		purchase = purchaseService.getPurchase2(prodNo);
		
		System.out.println("purchase at addPurchaseAction: "+purchase);
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/confirmPurchase.jsp";
	}

}
