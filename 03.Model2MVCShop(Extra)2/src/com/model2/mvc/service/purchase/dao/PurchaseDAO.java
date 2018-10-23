package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

public class PurchaseDAO {
	
	public Purchase findPurchase(int tranNo) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM users u, product p, transaction t WHERE t.tran_no=? AND u.user_id=t.buyer_id AND p.prod_no=t.prod_no";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();
		
		Purchase purchase = null;
		while (rs.next()) {
			purchase = new Purchase();
			
			//product
			Product product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));
			product.setProdName(rs.getString("PROD_NAME"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setFileName(rs.getString("IMAGE_FILE"));
			product.setRegDate(rs.getDate("REG_DATE"));
			purchase.setPurchaseProd(product);
			
			//user
			User user = new User();
			user.setUserId(rs.getString("USER_ID"));
			user.setUserName(rs.getString("USER_NAME"));
			user.setPassword(rs.getString("PASSWORD"));
			user.setRole(rs.getString("ROLE"));
			user.setSsn(rs.getString("SSN"));
			user.setPhone(rs.getString("CELL_PHONE"));
			user.setAddr(rs.getString("ADDR"));
			user.setEmail(rs.getString("EMAIL"));
			user.setRegDate(rs.getDate("REG_DATE"));
			purchase.setBuyer(user);
			
			purchase.setTranNo(rs.getInt("TRAN_NO"));
			purchase.setPaymentOption(getPayOptStr(rs.getString("PAYMENT_OPTION")));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(getTranCodeStr(rs.getString("TRAN_STATUS_CODE")));
			purchase.setOrderDate(rs.getDate("ORDER_DATA"));
			Date date = rs.getDate("DLVY_DATE");
			if (date!=null)
				purchase.setDivyDate(date.toString());
			else
				purchase.setDivyDate("");
		}
		
		con.close();
		
		return purchase;
	}
	
	public Purchase findPurchase2(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM users u, product p, transaction t WHERE t.prod_no=? AND u.user_id=t.buyer_id AND p.prod_no=t.prod_no";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();
		
		Purchase purchase = null;
		while (rs.next()) {
			purchase = new Purchase();
			
			//product
			Product product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));
			product.setProdName(rs.getString("PROD_NAME"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setFileName(rs.getString("IMAGE_FILE"));
			product.setRegDate(rs.getDate("REG_DATE"));
			purchase.setPurchaseProd(product);
			
			//user
			User user = new User();
			user.setUserId(rs.getString("USER_ID"));
			user.setUserName(rs.getString("USER_NAME"));
			user.setPassword(rs.getString("PASSWORD"));
			user.setRole(rs.getString("ROLE"));
			user.setSsn(rs.getString("SSN"));
			user.setPhone(rs.getString("CELL_PHONE"));
			user.setAddr(rs.getString("ADDR"));
			user.setEmail(rs.getString("EMAIL"));
			user.setRegDate(rs.getDate("REG_DATE"));
			purchase.setBuyer(user);
			
			purchase.setTranNo(rs.getInt("TRAN_NO"));
			purchase.setPaymentOption(getPayOptStr(rs.getString("PAYMENT_OPTION")));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(getTranCodeStr(rs.getString("TRAN_STATUS_CODE")));
			purchase.setOrderDate(rs.getDate("ORDER_DATA"));
			Date date = rs.getDate("DLVY_DATE");
			if (date!=null)
				purchase.setDivyDate(date.toString());
			else
				purchase.setDivyDate("");
		}
		
		con.close();
		
		return purchase;
	}
	
	public Map<String,Object> getPurchaseList(Search search,String buyerId) throws Exception {
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT u.user_id,u.user_name,u.password,u.role,u.ssn,u.cell_phone,u.addr,u.email,u.reg_date AS u_reg_date," + 
				"p.prod_no AS p_prod_no,p.prod_name,p.prod_detail,p.manufacture_day,p.price,p.image_file,p.reg_date AS p_reg_date," + 
				"t.tran_no,t.prod_no AS t_prod_no,t.buyer_id,t.payment_option,t.receiver_name,t.receiver_phone,t.demailaddr,t.dlvy_request,t.tran_status_code,t.order_data,t.dlvy_date " + 
				"FROM users u, product p, transaction t";
		
		if (buyerId != null) {
			sql += " WHERE t.buyer_id='" + buyerId + "'";
		}
		sql += " AND u.user_id=t.buyer_id";
		sql += " AND t.prod_no=p.prod_no";
		sql += " ORDER BY tran_no";
		System.out.println("ProductDAO::Original SQL :: "+sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("PurchaseDAO::totalCount :: "+totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		ArrayList<Purchase> list = new ArrayList<Purchase>();
		while (rs.next()) {
			Purchase purchase = new Purchase();
				
			//product
			Product product = new Product();
			product.setProdNo(rs.getInt("P_PROD_NO"));
			product.setProdName(rs.getString("PROD_NAME"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setFileName(rs.getString("IMAGE_FILE"));
			product.setRegDate(rs.getDate("P_REG_DATE"));
			purchase.setPurchaseProd(product);
			
			//user
			User user = new User();
			user.setUserId(rs.getString("USER_ID"));
			user.setUserName(rs.getString("USER_NAME"));
			user.setPassword(rs.getString("PASSWORD"));
			user.setRole(rs.getString("ROLE"));
			user.setSsn(rs.getString("SSN"));
			user.setPhone(rs.getString("CELL_PHONE"));
			user.setAddr(rs.getString("ADDR"));
			user.setEmail(rs.getString("EMAIL"));
			user.setRegDate(rs.getDate("U_REG_DATE"));
			purchase.setBuyer(user);
			
			purchase.setTranNo(rs.getInt("TRAN_NO"));
			purchase.setPaymentOption(getPayOptStr(rs.getString("PAYMENT_OPTION")));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(getTranCodeStr(rs.getString("TRAN_STATUS_CODE")));
			purchase.setOrderDate(rs.getDate("ORDER_DATA"));
			Date date = rs.getDate("DLVY_DATE");
			if (date!=null)
				purchase.setDivyDate(date.toString());
			else
				purchase.setDivyDate("");

			list.add(purchase);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("totalCount", new Integer(totalCount));
		map.put("list", list);
		
		rs.close();
		stmt.close();
		con.close();

		return map;
	}
	
	public void insertPurchase(Purchase purchase) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "insert into TRANSACTION values (seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,?,SYSDATE,?)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchase.getPurchaseProd().getProdNo());
		stmt.setString(2, purchase.getBuyer().getUserId());
		stmt.setString(3, purchase.getPaymentOption());
		stmt.setString(4, purchase.getReceiverName());
		stmt.setString(5, purchase.getReceiverPhone());
		stmt.setString(6, purchase.getDivyAddr());
		stmt.setString(7, purchase.getDivyRequest());
		stmt.setString(8, getTranCodeInt(purchase.getTranCode()));

		if (purchase.getDivyDate()!=null && purchase.getDivyDate().length()>0)
			stmt.setDate(9, Date.valueOf(purchase.getDivyDate()));
		else
			stmt.setDate(9, null);
		stmt.executeUpdate();
		
		con.close();
	}

	public void updatePurchase(Purchase purchase) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "update TRANSACTION set PAYMENT_OPTION=?,RECEIVER_NAME=?,RECEIVER_PHONE=?,DEMAILADDR=?,DLVY_REQUEST=?,DLVY_DATE=? where TRAN_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchase.getPaymentOption());
		stmt.setString(2, purchase.getReceiverName());
		stmt.setString(3, purchase.getReceiverPhone());
		stmt.setString(4, purchase.getDivyAddr());
		stmt.setString(5, purchase.getDivyRequest());
		Date date = null;
		if (purchase.getDivyDate()!=null && purchase.getDivyDate().length()>0)
			date = Date.valueOf(purchase.getDivyDate());
		stmt.setDate(6, date);
		stmt.setInt(7, purchase.getTranNo());
		
		stmt.executeUpdate();
		
		con.close();
	}
	
	public void updateTranCode(Purchase purchase) throws Exception {
		Connection con = DBUtil.getConnection();
		
		String sql = "update TRANSACTION set TRAN_STATUS_CODE=? where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchase.getTranCode());
		stmt.setInt(2, purchase.getPurchaseProd().getProdNo());
		
		stmt.executeUpdate();
		
		con.close();
	}
	
	private String getTranCodeInt(String tranCodeStr) {
		System.out.println("tranCodeStr: "+tranCodeStr);
		if (tranCodeStr!=null) {
			if (tranCodeStr.equals("판매중"))
				return "0";
			else if (tranCodeStr.equals("구매완료"))
				return "1";
			else if (tranCodeStr.equals("배송중"))
				return "2";
			else if (tranCodeStr.equals("배송완료"))
				return "3";
			else
				return "-1";
		}
		return "-1";
	}
	
	private String getTranCodeStr(String tranCodeInt) {
		if (tranCodeInt!=null) {
			tranCodeInt=tranCodeInt.trim();
			if (tranCodeInt.equals("0"))
				return "판매중";
			else if (tranCodeInt.equals("1"))
				return "구매완료";
			else if (tranCodeInt.equals("2"))
				return "배송중";
			else if (tranCodeInt.equals("3"))
				return "배송완료";
			else
				return "알수없음";
		}
		return "알수없음";
	}
	
	private String getPayOptStr(String payOptStrInt) {
		if (payOptStrInt!=null) {
			payOptStrInt = payOptStrInt.trim();
			if (payOptStrInt.equals("1"))
				return "현금구매";
			else if (payOptStrInt.equals("2"))
				return "신용구매";
			else
				return "-1";
		}
		return "-1";
	}
	
	private int getTotalCount(String sql) throws Exception {
		sql = "SELECT COUNT(*) "+
		      "FROM ("+sql+") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if (rs.next()) {
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}

	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
				"FROM (SELECT inner_table.*, ROWNUM AS row_seq "+
						"FROM ("+sql+") inner_table "+
						"WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+") "+
				"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1)+" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("PurchaseDAO :: make SQL :: "+ sql);	
		
		return sql;
	}
}
