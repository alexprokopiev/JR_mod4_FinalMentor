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
    <h1 id="registerHeader" class="text-start ps-5 pt-5">Регистрация нового пользователя</h1>
    <form class="mt-5" onsubmit="createUser();return false" name="createUserForm">
        <div class="row justify-content-start my-3">
            <div class="col-1"></div>
            <label for="userNameForCreate" class="col-2 col-form-label text-start fs-4">Имя</label>
            <div class="col-3">
                <input id="userNameForCreate" class="form-control-lg w-100" name="name" type="text" value="">
            </div>
        </div>
        <div class="row justify-content-start my-3">
            <div class="col-1"></div>
            <label for="userLastNameForCreate" class="col-2 col-form-label text-start fs-4">Фамилия</label>
            <div class="col-3">
                <input id="userLastNameForCreate" class="form-control-lg w-100" name="lastName" type="text" value="">
            </div>
        </div>
        <div class="row justify-content-start my-3">
            <div class="col-1"></div>
            <label for="userRoleForCreate" class="col-2 col-form-label text-start fs-4">Роль</label>
            <div class="col-3">
                <select id="userRoleForCreate" class="form-select-lg w-100" name="role">
                    <c:forEach var="role" items="${applicationScope.Roles}">
                        <option value=${role}>${role.toString()}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="row justify-content-start my-3">
            <div class="col-1"></div>
            <label for="userLoginForCreate" class="col-2 col-form-label text-start fs-4">Логин</label>
            <div class="col-3">
                <input id="userLoginForCreate" class="form-control-lg w-100" name="login" type="text" value="">
            </div>
        </div>
        <div class="row justify-content-start my-3">
            <div class="col-1"></div>
            <label for="userPasswordForCreate" class="col-2 col-form-label text-start fs-4">Пароль</label>
            <div class="col-3">
                <input id="userPasswordForCreate" class="form-control-lg w-100" name="password" type="password" value="">
            </div>
        </div>
        <div class="row justify-content-center my-3">
            <div class="col-3">
                <button class="btn btn-info btn-lg text-light w-100 fs-4" type="submit">Создать пользователя</button>
            </div>
            <div class="col-3">
                <a class="btn btn-info btn-lg text-light w-100 fs-4 text-decoration-none" href="${pageContext.request.contextPath}/userList.jsp">В список пользователей</a>
            </div>
            <div class="col-3">
                <a class="btn btn-info btn-lg text-light w-100 fs-4 text-decoration-none" href="${pageContext.request.contextPath}/tasks/">В список задач</a>
            </div>
        </div>
    </form>
</div>
<jsp:include page="footer.jsp"/>
<script>
    function createUser() {
        let createName = document.getElementById("userNameForCreate").value;
        let createLastName = document.getElementById("userLastNameForCreate").value;
        let createRole = document.getElementById("userRoleForCreate").value;
        let createLogin = document.getElementById("userLoginForCreate").value;
        let createPassword = document.getElementById("userPasswordForCreate").value;

        let createData = {
            name: createName,
            lastName: createLastName,
            role: createRole,
            login: createLogin,
            password: createPassword
        }
        let body = JSON.stringify(createData);
        let createRequest = new XMLHttpRequest();
        createRequest.open("POST", '<c:url value="/users/"/>');
        createRequest.setRequestHeader("Content-type", "application/json");
        createRequest.send(body);
        createRequest.onload = function () {
            if (createRequest.status === 201) {
                showMessage("Пользователь успешно добавлен");
                clearFields();
            } else {
                showMessage("При создании произошла ошибка");
            }
        }
    }

    function showMessage(message) {
        let header = document.getElementById("registerHeader");
        let div = document.createElement("div");
        div.innerHTML = message;
        header.after(div);
        setTimeout(() => div.remove(), 5000);
    }

    function clearFields(){
        document.getElementById("userNameForCreate").value = "";
        document.getElementById("userLastNameForCreate").value = "";
        document.getElementById("userLoginForCreate").value = "";
        document.getElementById("userPasswordForCreate").value = "";
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>