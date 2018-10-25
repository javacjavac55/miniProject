<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>        

<html><head>

<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="/listProduct.do?menu=${param.menu}" method="post">

<table width="100%" height="37" border="0" cellpadding="0" cellspacing="0">
	<tbody><tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37">
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tbody><tr>
					<c:if test="${param.menu == 'manage'}">
						<td width="93%" class="ct_ttl01">상품 관리</td>
					</c:if>
					<c:if test="${param.menu == 'search'}">
						<td width="93%" class="ct_ttl01">상품 목록조회</td>
					</c:if>
				</tr>
			</tbody></table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37">
		</td>
	</tr>
</tbody></table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="right">
			<select name="filterCondition" class="ct_input_g" style="width:80px" onchange="javascript:fncGetItemList('1');">
				<option value="4" ${!empty search.filterCondition && search.filterCondition == "4" ? 'selected' : ''}>모든 상품</option>
				<option value="0" ${!empty search.filterCondition && search.filterCondition == "0" ? 'selected' : ''}>판매중</option>
				<option value="1" ${!empty search.filterCondition && search.filterCondition == "1" ? 'selected' : ''}>구매완료</option>
				<option value="2" ${!empty search.filterCondition && search.filterCondition == "2" ? 'selected' : ''}>배송중</option>
				<option value="3" ${!empty search.filterCondition && search.filterCondition == "3" ? 'selected' : ''}>배송완료</option>
			</select>
			<select name="searchCondition" class="ct_input_g" style="width:80px">
				<option value="0" ${!empty search.searchCondition && search.searchCondition == "0" ? 'selected' : ''}>상품번호</option>
				<option value="1" ${!empty search.searchCondition && search.searchCondition == "1" ? 'selected' : ''}>상품명</option>
				<option value="2" ${!empty search.searchCondition && search.searchCondition == "2" ? 'selected' : ''}>상품가격</option>
			</select>
			<input type="text" name="searchKeyword" value="${!empty search.searchKeyword ? search.searchKeyword : ''}" class="ct_input_g" style="width:200px; height:19px" />
		</td>
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetItemList('1');">검색</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11">전체  ${resultPage.totalCount} 건수, 현재 ${resultPage.currentPage} 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">등록일</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	<c:set var="i" value="0" />
	<c:forEach var="product" items="${list}">
		<c:set var="i" value="${i+1}" />
		<tr class="ct_list_pop">
			<td align="center">${i}</td>
			<td></td>
			<td align="left">
				<c:choose>
					<c:when test="${param.menu == 'manage'}">
					<a href="/updateProductView.do?prodNo=${product.prodNo}&menu=${param.menu}">${product.prodName}</a>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${user.role == 'admin' || product.proTranCode == '판매중'}">
								<a href="/getProduct.do?prodNo=${product.prodNo}&menu=${param.menu}">${product.prodName}</a>
							</c:when>
							<c:otherwise>
								${product.prodName}
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</td>
			<td></td>
			<td align="left">${product.price}</td>
			<td></td>
			<td align="left">${product.regDate}</td>
			<td></td>
			<td align="left">${product.proTranCode} 
				<c:if test="${param.menu == 'manage' && product.proTranCode == '구매완료'}">
					<a href="/updateTranCodeByProd.do?prodNo=${product.prodNo}&tranCode=2">배송하기</a>
				</c:if>
			</td>
		</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>	
	</c:forEach>
	</tbody>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
			<input type="hidden" id="currentPage" name="currentPage" value=""/>
			<jsp:include page="../common/pageNavigator.jsp"/>	
		</td>
	</tr>
</table>
<!--  페이지 Navigator 끝 -->

</form>

</div>

</body></html>