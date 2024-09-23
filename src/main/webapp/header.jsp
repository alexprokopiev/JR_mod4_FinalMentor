<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<header class="mw-100 bg-info row" style="height: 10vh">
    <nav class="row align-items-center">
        <div class="col"></div>
        <div class="col align-items-center">
            <c:if test = "${applicationScope.User_authenticated == 'false'}">
                <a href="${pageContext.request.contextPath}/" class="text-decoration-none text-light fs-2"> ToDo App</a>
            </c:if>
            <c:if test = "${applicationScope.User_authenticated == 'true'}">
                <a href="${pageContext.request.contextPath}/todoList.jsp" class="text-decoration-none text-light fs-2"> ToDo App</a>
            </c:if>
        </div>
        <div class="col"></div>
        <div class="col-4 align-items-center row">
            <c:if test = "${applicationScope.User_authenticated == 'false'}">
                <form class="col d-flex align-items-end flex-column" action='<c:url value="/login.jsp"/>'>
                    <button class="btn btn-link text-decoration-none text-light" type="submit">Войти в аккаунт</button>
                </form>
            </c:if>
            <c:if test = "${applicationScope.User.role.label == 'Администратор'}">
                <form class="col d-flex align-items-end flex-column" action='<c:url value="/register.jsp"/>'>
                    <button class="btn btn-link text-decoration-none text-light" type="submit">Создать пользователя</button>
                </form>
            </c:if>
            <c:if test = "${applicationScope.User_authenticated == 'true'}">
                <form class="col d-flex align-items-start flex-column" action='<c:url value="/login/"/>'>
                    <button class="btn btn-link text-decoration-none text-light" type="submit">Выйти</button>
                </form>
            </c:if>
        </div>
        <div class="col"></div>
    </nav>
</header>
