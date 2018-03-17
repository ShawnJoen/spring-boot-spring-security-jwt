<%@ page language="java" contentType="text/html; charset=utf8"
         pageEncoding="utf8"%><!DOCTYPE html><html lang="zh-CN">
<head>
    <title>SpringBoot Jwt</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8"/>
    <script src="/static/js/jquery-1.12.4.min.js"></script>
</head>
<body>

<b>login</b>

<form>
    <input type="type" name="email" id="email"/></br>
    <input type="password" name="pw" id="pw"/></br>
    <input type="button" value="로그인" onclick="Login()"/>
</form>
<script>
    function Login() {
        var email = $("#email").val();
        var pw = $("#pw").val();
        $.post("/user/doLogin", {email: email, pw: pw}, function(result) {

            console.log(JSON.stringify(result));//{"status":0,"message":"success","data":{"id":16,"email":"qqqq","token":"eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1MjEyODM0OTMsInN1YiI6InFxcXEiLCJjcmVhdGVkIjoxNTIxMjgyNTkzNjUyfQ.UC0PcC8QlLgmpgeVUWjY9y6YUJ4Ml0IBc3lr_P9diOFPHUGC7IH66RKosU--dypNhrDTBb5OPvCJZlhk14P5Aw"}}

            if (result.data == null) {
                alert(result.message);
            } else {
                alert(result.data.token);
            }
        }, "json");
    }
</script>
</body>
</html>