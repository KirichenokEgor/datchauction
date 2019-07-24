<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
    <head>
        <title>Lot Info</title>
    </head>
    <body>

        <h2>Lot Information</h2>
        <table>
        <tr>
            <td>ID</td>
            <td>${ID}</td>
        </tr>
          <tr>
        <tr>
         <td>Price</td>
         <td>${price}</td>
        </tr>
        <tr>
            <td>Min price</td>
            <td>${min_price}</td>
        </tr>
        <tr>
            <td>Description</td>
            <td>${description}</td>
        </tr>
        </table>
    </body>
</html>