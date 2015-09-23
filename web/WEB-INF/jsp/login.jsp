<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.sergeev.controlpanel.utils.Constants" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:tiles="http://www.thymeleaf.org">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <!-- Meta, title, CSS, favicons, etc. -->
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title><%= Constants.APP_NAME %> | </title>

  <!-- Bootstrap core CSS -->

  <link href="css/bootstrap.min.css" rel="stylesheet">

  <link href="fonts/css/font-awesome.min.css" rel="stylesheet">
  <link href="css/animate.min.css" rel="stylesheet">

  <!-- Custom styling plus plugins -->
  <link href="css/custom.css" rel="stylesheet">
  <link href="css/icheck/flat/green.css" rel="stylesheet">


  <script src="js/jquery.min.js"></script>

  <!--[if lt IE 9]>
  <script src="../assets/js/ie8-responsive-file-warning.js"></script>
  <![endif]-->

  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
    <script>
        $('#login-form').on('submit',function(){ console.log('login-form'); });
        $('#register-form').on('submit',function(){ console.log('register-form'); });
        console.log('test');
    </script>
</head>
<body style="background:#F7F7F7;">

<div class="">
  <a class="hiddenanchor" id="toregister"></a>
  <a class="hiddenanchor" id="tologin"></a>

  <div id="wrapper">
    <div id="login" class="animate form">
      <section class="login_content">

        <form th:action="@{/login}" method="post" id="login-form">
          <h1>Login Form</h1>
            <div style="display:none" id="login-alert" class="alert alert-danger col-sm-12">
                <c:if test="${not empty error}">
                    <script>$("#login-alert").show()</script>
                    <div class="error">${error}</div>
                </c:if>
                <c:if test="${not empty msg}">
                    <script>$("#login-alert").show()</script>
                    <div class="msg">${msg}</div>
                </c:if>
                <c:if test="${not empty registered}">
                    <script>$("#login-alert").removeClass("alert-danger").addClass("alert-success").show()</script>
                    <div class="msg">${registered}</div>
                </c:if>
            </div>
          <div>
            <input type="text" class="form-control" placeholder="Username" required="" name="username"  />
          </div>
          <div>
            <input type="password" class="form-control" placeholder="Password" required="" name="password" />
          </div>
          <div>
            <button class="btn btn-default submit" type="submit" form="login-form">Log in</button>
            <a class="reset_pass" href="#">Lost your password?</a>
          </div>
          <div class="clearfix"></div>
          <div class="separator">

            <p class="change_link">New to site?
              <a href="#toregister" class="to_register"> Create Account </a>
            </p>
            <div class="clearfix"></div>
            <br />
            <div>
              <h1><i class="fa fa-paw" style="font-size: 26px;"></i> <%= Constants.APP_NAME %></h1>

              <p>©2015 All Rights Reserved. <%= Constants.APP_NAME %> is a *NIX servers GUI managment system.</p>
            </div>
          </div>
        </form>
        <!-- form -->
      </section>
      <!-- content -->
    </div>
    <div id="register" class="animate form">
      <section class="login_content">
        <form action="/user" method="post" id="register-form">
          <h1>Create Account</h1>
            <div style="display:none" id="register-alert" class="alert alert-danger col-sm-12">
                <c:if test="${not empty param.error_register}">
                    <script>$("#register-alert").show()</script>
                    <div class="error">${param.error_register}</div>
                </c:if>
            </div>
          <div>
            <input type="text" class="form-control" placeholder="Username" required="" name="username" />
          </div>
          <div>
            <input type="password" class="form-control" placeholder="Password" required="" name="password" />
          </div>
          <div>
            <button class="btn btn-default submit" type="submit" form="register-form">Submit</button>
          </div>
          <div class="clearfix"></div>
          <div class="separator">

            <p class="change_link">Already a member ?
              <a href="#tologin" class="to_register"> Log in </a>
            </p>
            <div class="clearfix"></div>
            <br />
            <div>
              <h1><i class="fa fa-paw" style="font-size: 26px;"></i>  <%= Constants.APP_NAME %></h1>

              <p>©2015 All Rights Reserved. <%= Constants.APP_NAME %> is a *NIX servers GUI managment system.</p>
            </div>
          </div>
        </form>
        <!-- form -->
      </section>
      <!-- content -->
    </div>
  </div>
</div>

</body>
</html>

