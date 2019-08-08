<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType = "text/html; charset = UTF-8" %>
<html>
   <head>
      <title>Hello List</title>
   </head>

   <body>
   <%--
      for(String item: list){
         out.println("<br>" + item);
         }
   --%>
   <%--
      for(int i = 1; i < 5; i++){
         out.println("<br>" + i);
      }
   --%>
   <c:forEach var="item" items="${list}" >
           <tr><td><c:out value="${item}"/></td></tr>
           <br>
    </c:forEach>
   </body>
</html>