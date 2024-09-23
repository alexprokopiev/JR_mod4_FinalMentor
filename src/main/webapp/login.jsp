<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ToDo App</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container text-center" style="height: 85vh">
    <h1 id="loginHeader" class="text-start ps-5 pt-5">Вход в приложение</h1>
    <form class="mt-5" onsubmit="loginUser();return false" name="createUserForm">
        <div class="row justify-content-start my-3">
            <div class="col-1"></div>
            <label for="userLoginForAuth" class="col-2 col-form-label text-start fs-4">Логин</label>
            <div class="col-3">
                <input id="userLoginForAuth" class="form-control-lg w-100" name="login" type="text" value="">
            </div>
        </div>
        <div class="row justify-content-start my-3">
            <div class="col-1"></div>
            <label for="userPasswordForAuth" class="col-2 col-form-label text-start fs-4">Пароль</label>
            <div class="col-3">
                <input id="userPasswordForAuth" class="form-control-lg w-100" name="password" type="password" value="">
            </div>
        </div>
        <div class="row justify-content-center my-5">
            <div class="col-3">
                <button class="btn btn-info btn-lg text-light w-100 fs-4" type="submit">Войти</button>
            </div>
        </div>
    </form>
</div>
<jsp:include page="footer.jsp"/>
<script>
    function loginUser() {
        let authLogin = document.getElementById("userLoginForAuth").value;
        let authPassword = document.getElementById("userPasswordForAuth").value;

        let createData = {
            login: authLogin,
            password: authPassword
        }
        let body = JSON.stringify(createData);
        let createRequest = new XMLHttpRequest();
        createRequest.open("POST", '<c:url value="/login/"/>');
        createRequest.setRequestHeader("Content-type", "application/json");
        createRequest.send(body);
        createRequest.onload = function () {
            if (createRequest.status === 200) {
                window.location.href = '<c:url value="/tasks/"/>';
            } else if (createRequest.status === 401) {
                showMessage("Неверный логин или пароль");
            }
        }
    }

    function showMessage(message) {
        let header = document.getElementById("loginHeader");
        let div = document.createElement("div");
        div.innerHTML = message;
        header.after(div);
        setTimeout(() => div.remove(), 5000);
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
