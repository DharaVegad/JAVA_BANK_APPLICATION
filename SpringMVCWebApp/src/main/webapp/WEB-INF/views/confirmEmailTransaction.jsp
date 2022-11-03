<%@ page import="com.bank.lambton.vo.*, com.bank.lambton.vo.Transaction.*,com.bank.lambton.service.*,org.springframework.beans.factory.annotation.Autowired, java.util.List"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Are the Below details correct?</h2>
<%
LambtonBankTransaction t = (LambtonBankTransaction)session.getAttribute("transaction");
  String redirect = (String)request.getAttribute("redirect");
  CustomerService cService= (CustomerService)request.getAttribute("cutomerServiceObject");
  
  Customer receiver = cService.getCustomerFromAccountId(t.getToAccountId());
  
  String name = receiver.getFirstName().toUpperCase() +" "+ receiver.getLastName().toUpperCase();
%>
  <table>
  <tr>
    <th>Reference Id</th><td><%=t.getId()%></td></tr>
    <tr><th>Amount</th><td><%=t.getAmount() %></td></tr>
    <tr><th>Remark</th><td><%=t.getRemark() %></td></tr>
    <tr><th>Receiver name</th><td><%=name %></td></tr>
    <tr><th>Receiver email</th><td><%=receiver.getEmail().toUpperCase()%></td></tr>
</table>
<button><a href="<%=redirect %>">confirm</a></button>