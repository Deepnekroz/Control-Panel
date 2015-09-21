<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
  ${responseJson}
  <script>
      var a = ${responseJson};
      alert(a["parameter"]);
  </script>




  </body>
</html>
