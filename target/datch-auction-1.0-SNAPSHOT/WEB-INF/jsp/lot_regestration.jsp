<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page contentType = "text/html; charset = UTF-8" %>
<html>
   <head>
      <title>Lot regestration</title>
   </head>

   <body>
   <h2>Enter lot information</h2>
   <form:form method="post" action="addLot">
     <table>
       <tr>
         <td><form:label path="ID">ID</form:label></td>
         <td><form:input path="ID" /></td>
       </tr>
       <tr>
         <td><form:label path="price">Price</form:label></td>
         <td><form:input path="price" /></td>
       </tr>
       <tr>
         <td><form:label path="min_price">Min price</form:label></td>
         <td><form:input path="min_price" /></td>
       </tr>
       <tr>
         <td><form:label path="description">Description</form:label></td>
         <td><form:input path="description" /></td>
       </tr>
       <tr>
         <td colspan="2">
           <input type="submit" value="Submit"/>
         </td>
       </tr>
     </table>
   </form:form>
   </body>
</html>