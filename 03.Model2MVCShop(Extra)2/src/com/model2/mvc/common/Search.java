package com.model2.mvc.common;


public class Search {
	
	///Field
	private int curruntPage;
	private String searchCondition;
	private String searchKeyword;
	private int pageSize;
	private String sortCondition;
	private String filterCondition;
	
	///Constructor
	public Search() {
	}
	
	///Method
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int paseSize) {
		this.pageSize = paseSize;
	}
	
	public int getCurrentPage() {
		return curruntPage;
	}
	public void setCurrentPage(int curruntPage) {
		this.curruntPage = curruntPage;
	}

	public String getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSortCondition() {
		return sortCondition;
	}

	public void setSortCondition(String sortCondition) {
		this.sortCondition = sortCondition;
	}

	public String getFilterCondition() {
		return filterCondition;
	}

	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Search [curruntPage=");
		builder.append(curruntPage);
		builder.append(", searchCondition=");
		builder.append(searchCondition);
		builder.append(", searchKeyword=");
		builder.append(searchKeyword);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append(", sortCondition=");
		builder.append(sortCondition);
		builder.append(", filterCondition=");
		builder.append(filterCondition);
		builder.append("]");
		return builder.toString();
	}
}