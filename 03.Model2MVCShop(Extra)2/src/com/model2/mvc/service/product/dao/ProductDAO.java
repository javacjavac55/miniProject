package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;

public class ProductDAO {
	
	public ProductDAO() {
	}
	
	public void insertProduct(Product product) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "insert into PRODUCT values (seq_product_prod_no.nextval,?,?,?,?,?,SYSDATE)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate().replace("-", ""));
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		stmt.executeUpdate();
		
		con.close();
	}
	
	public Product findProduct(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM product WHERE prod_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();

		Product product = null;
		while (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));
			product.setProdName(rs.getString("PROD_NAME"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setFileName(rs.getString("IMAGE_FILE"));
			product.setRegDate(rs.getDate("REG_DATE"));
		}
		con.close();
		return product;
	}
	
	public Map<String,Object> getProductList(Search search) throws Exception {
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT p.prod_no AS p_prod_no,p.prod_name,p.prod_detail,p.manufacture_day,p.price,p.image_file,p.reg_date AS p_reg_date,t.tran_status_code"
				+ " FROM product p,transaction t"
				+ " WHERE p.prod_no=t.prod_no(+)";
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0")) {
				sql += " AND p.prod_no LIKE '%'||'" + search.getSearchKeyword() + "'||'%'";
			} else if (search.getSearchCondition().equals("1")) {
				sql += " AND prod_name LIKE '%'||'" + search.getSearchKeyword() + "'||'%'";
			} else if (search.getSearchCondition().equals("2")) {
				sql += " AND price BETWEEN " + search.getSearchKeyword().split("-")[0] + " AND " + search.getSearchKeyword().split("-")[1];
			}
		}
		System.out.println("search.getFilterCondition(): "+search.getFilterCondition());
		if (search.getFilterCondition() != null) {
			if (search.getFilterCondition().equals("0")) {
				sql += " AND tran_status_code IS NULL";
			} else if (search.getFilterCondition().equals("4")){
				//no filter
			} else {
				sql += " AND tran_status_code='"+search.getFilterCondition()+"'";
			}
		}
		if (search.getSortCondition() != null) {
			if (search.getSortCondition().equals("0")) {
				sql += " ORDER BY p.prod_no DESC";
			} else if (search.getSortCondition().equals("1")) {
				sql += " ORDER BY price";
			} else if (search.getSortCondition().equals("2")) {
				sql += " ORDER BY price DESC";
			}
		} else {
			sql += " ORDER BY p.prod_no DESC";
		}
		System.out.println("ProductDAO::Original SQL :: "+sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO::totalCount :: "+totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			Product product = new Product();
			int prodNo = rs.getInt("P_PROD_NO");
			product.setProdNo(prodNo);
			product.setProdName(rs.getString("PROD_NAME"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setFileName(rs.getString("IMAGE_FILE"));
			product.setRegDate(rs.getDate("P_REG_DATE"));
			if (rs.getString("tran_status_code")!=null)
				product.setProTranCode(getTranCodeStr(rs.getString("tran_status_code")));
			else
				product.setProTranCode(getTranCodeStr("0"));
			
			list.add(product);
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("totalCount", new Integer(totalCount));
		map.put("list", list);
		
		rs.close();
		stmt.close();
		con.close();
		
		return map;
	}

	public void updateProduct(Product product) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "update PRODUCT set PROD_NAME=?,PROD_DETAIL=?,MANUFACTURE_DAY=?,PRICE=?,IMAGE_FILE=? where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate().replace("-", ""));
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		stmt.setInt(6, product.getProdNo());
		stmt.executeUpdate();
		
		con.close();
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
		
		System.out.println("ProductDAO :: make SQL :: "+ sql);	
		
		return sql;
	}
	
	//for status
	private String getTranCodeStr(String tranCodeInt) {
		tranCodeInt=tranCodeInt.trim();
		if (tranCodeInt!=null) {
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
	//
}
