<%@ page contentType="text/html; charset=euc-kr"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
<title>���Ż�ǰ �����ȸ</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	function fncGetList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
		document.detailForm.submit();
	}
</script>


</head>

<body bgcolor="#ffffff" text="#000000">

	<div style="width: 98%; margin-left: 10px;">

		<form name="detailForm" action="/purchase/listSale?menu=${param.menu}"
			method="post">

			<table width="100%" height="37" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"
						width="15" height="37" /></td>
					<td background="/images/ct_ttl_img02.gif" width="100%"
						style="padding-left: 10px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
									<td width="93%" class="ct_ttl01">���Ż�ǰ �����ȸ</td>
							</tr>
						</table>
					</td>
					<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"
						width="12" height="37" /></td>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td align="right"><select name="searchCondition"
						class="ct_input_g" style="width: 80px">
							
						<option value="0" ${!empty search.searchCondition && search.searchCondition == "0" ? "selected" : ""} >��ǰ��ȣ</option>
						
						<option value="1" ${!empty search.searchCondition && search.searchCondition == "1" ? "selected" : ""} >��ǰ��</option>
					
						<option value="2" ${!empty search.searchCondition && search.searchCondition == "2" ? "selected" : ""} >��ǰ����</option>

					</select> <input type="text" name="searchKeyword"
						value="${searchKeyword}" class="ct_input_g"
						style="width: 200px; height: 19px" /></td>


					<!-- null ��ȿ��üũ�ϴ� �κ��� �޼ҵ�� ���� ������. -->
					<td align="right" width="70">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="17" height="23"><img
									src="/images/ct_btnbg01.gif" width="17" height="23"></td>
								<td background="/images/ct_btnbg02.gif" class="ct_btn01"
									style="padding-top: 3px;"><a
									href="javascript:fncGetList('1');">�˻�</a></td>
								<td width="14" height="23"><img
									src="/images/ct_btnbg03.gif" width="14" height="23"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>


			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td colspan="11">��ü ${resultPage.totalCount} �Ǽ�, ���� ${resultPage.currentPage} ������
					</td>
				</tr>
				<tr>
					<td class="ct_list_b" width="100">No</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">��ǰ��</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b" width="150">����</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">�����</td>
					<td class="ct_line02"></td>
					<td class="ct_list_b">�������</td>
				</tr>
				<tr>
					<td colspan="11" bgcolor="808285" height="1"></td>
				</tr>
				
				<c:set var="i" value="0" />
				<c:forEach var="purchase" items="${list}">
					<c:set var="i" value="${i + 1}" />
						<tr class="ct_list_pop">
						<td align="center">${i}</td>
						<td></td>
	
						<td align="left">
							<a
							href="/product/getProduct?prodNo=${purchase.purchaseProd.prodNo}&menu=${param.menu}">${purchase.purchaseProd.prodName}</a>
						</td>
						
						<!-- sale�� �ƴ϶��  -->
					
						<td></td>
						<td align="left">${purchase.purchaseProd.price}</td>
						<td></td>
						<td align="left">${purchase.purchaseProd.regDate}</td>
						<td></td>
						<td align="left">
						
						<!-- ������ ���� ���Ÿ� �������� ��ǰ�� ���� ��� -->
						<c:if test="${purchase.tranCode == 'sale'}">
							�Ǹ���
						</c:if>
						
						<!-- ������ ���Ÿ� �� user���� [����� ��������] ù��° -->
						<c:if test="${fn:trim(purchase.tranCode) == '1'}">
							���� �Ϸ� <a href="/purchase/updateTranCode?tranNo=${purchase.tranNo}&tranCode=2&currentPage=${resultPage.currentPage}"> ����ϱ� </a>
						</c:if>
						
						<!-- user���� ����� ���� �� [����Ȯ��]�� ��ٸ��� �� -->
						<c:if test="${fn:trim(purchase.tranCode) == '2'}">
							�����, ������ ���ɴ��
						</c:if>
						
						<!-- user�� [����Ȯ��]�� �������  -->
						<c:if test="${fn:trim(purchase.tranCode) == '3'}">
							���ſϷ�
						</c:if>
						
					</td>
				</tr>
				<tr>
					<td colspan="11" bgcolor="D6D7D6" height="1"></td>
				</tr>
				
			</c:forEach>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="margin-top: 10px;">
				<tr>
					<td align="center"><input type="hidden" id="currentPage"
						name="currentPage" value="" /> <!-- page navigation�� �ݺ����� c&p�� ������� ������ -->
						<jsp:include page="../common/pageNavigator.jsp" />
				</tr>
			</table>

		</form>

	</div>
</body>
</html>