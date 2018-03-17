<%@ page language="java" contentType="text/html; charset=utf8"
pageEncoding="utf8"%><!DOCTYPE html><html lang="zh-CN">
<head>
    <title>SpringBoot Jwt</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8"/>
    <script src="/static/js/jquery-1.12.4.min.js"></script>
</head>
<body>
<b>register</b>
<form>
    <input type="type" name="email" id="email"/></br>
    <input type="type" name="pw" id="pw"/></br>
    <input type="button" value="신청하기" onclick="Register()"/>
</form>
<script>
function Register() {
    var email = $("#email").val();
    var pw = $("#pw").val();
    $.get("/user/doRegister", {email: email, pw: pw}, function(result) {

        console.log(JSON.stringify(result));//{"status":0,"message":"success","data":""}

        alert(result.message);
    }, "json");
}
</script>
</body>
</html>