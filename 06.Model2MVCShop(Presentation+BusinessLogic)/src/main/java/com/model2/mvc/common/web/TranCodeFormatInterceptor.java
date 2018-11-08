package com.model2.mvc.common.web;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.model2.mvc.service.domain.Product;

public class TranCodeFormatInterceptor extends HandlerInterceptorAdapter {
	private static Properties wording;
	public static void setProperty(Properties wordingProp) {
		wording=wordingProp;
	}
	
	public TranCodeFormatInterceptor(){
		System.out.println("\nCommon :: "+this.getClass()+"\n");		
	}
	
	public boolean postHandle(	HttpServletRequest request,
									HttpServletResponse response, 
										Object handler) throws Exception {
		System.out.println("postHandle start");
		
		List<Product> products = (List<Product>) request.getAttribute("list");
		
		for(Product product:products) {
			if (product!=null) {
				System.out.println("getProTranCode: " + product.getProTranCode().trim());
				System.out.println("wording: "+wording.getProperty(product.getProTranCode().trim()));
				product.setProTranCode(wording.getProperty(product.getProTranCode().trim()));
			}
		}		
		
		return true;
	}
	
}//end of class