<%@ page import="com.bank.lambton.vo.*, java.util.List"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="body-class">
<p>${customer.firstName}${customer.lastName}</p>
<div class="plogout">
<button class="logoutbtn">
	<a style="text-decoration:none;color:#fff;" href="./logout">Logout</a>
</button>
</div>


<br>
<hr>
<br>
<h2 class="bank-heading">Accounts</h2>

<%
for (Account ac : (List<Account>) request.getAttribute("accountsList")) {
	String accountType = ac.getType();

	float balance = ac.getBalance();
	out.print("<p class='account-info'>" + accountType + " (" + ac.getId() + ")" + " account  - $" + balance + "</p>");

}
%>
<br>
<hr><br>
<h2 class="bank-heading">Service</h2>

<h3><a href='./transfer/self'>Self transfer</a>
</h3>
<h3>
	<a href='./transfer/by-email'>Transfer by Email</a>
</h3>

<%
for (Utility uc : (List<Utility>) request.getAttribute("categoriesList")) {
     	String category = uc.getName();
    	String slug = String.join("-", category.split(" "));

    	out.print("<h3><a href='./categories/" + slug + "'>" + category + "</a></h3>");

}
%>

<br>
<br>
<br>

</div>