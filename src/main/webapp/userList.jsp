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
<div class="container text-center"  style="min-height: 85vh">
    <h1 id="userHeader" class="text-start ps-5 pt-5">Список пользователей</h1>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Фамилия</th>
            <th>Имя</th>
            <th>Роль</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${applicationScope.Users}">
            <c:set var="userId" value="${user.id}" scope="page" />
            <tr>
                <td>${user.lastName}</td>
                <td>${user.name}</td>
                <td>${user.role}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="row justify-content-center my-3">
        <div class="col-3">
            <a class="btn btn-info btn-lg text-light w-100 fs-4 text-decoration-none" href="${pageContext.request.contextPath}/tasks/">В список задач</a>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>

