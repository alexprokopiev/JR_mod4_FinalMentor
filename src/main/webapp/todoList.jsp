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
<div class="container text-center" style="min-height: 85vh">
    <h1 id="tasksHeader" class="text-start ps-5 pt-5">Задачи</h1>
    <div class="row">
        <form class="col d-flex align-items-start flex-column mt-3 ms-5" action='<c:url value="/taskCreator.jsp"/>'>
            <button class="btn btn-info text-light" type="submit">Создать новую задачу</button>
        </form>
        <c:if test = "${applicationScope.User.role.label == 'Администратор'}">
            <form class="row col mt-3" action='<c:url value="/tasks/"/>'>
                <div class="col">
                    <button class="btn btn-info text-light" type="submit">Фильтровать по пользователю</button>
                </div>
                <div class="col" style="margin-top: -24px;">
                    <label for="taskFilterByUser"></label>
                    <select id="taskFilterByUser" class="form-select" name="userId">
                        <c:forEach var="user" items="${applicationScope.Users}">
                            <option value=${user.id}>${user.name} ${user.lastName}</option>
                        </c:forEach>
                    </select>
                </div>
            </form>
            <form class="col-1 mt-3" action='<c:url value="/tasks/"/>'>
                <button class="btn btn-info text-light" type="submit">Сброс</button>
            </form>
        </c:if>
    </div>
    <div class="row mt-5">
        <div class="col">
            <div class="d-flex align-items-start flex-column">
                <h2><span class="badge rounded-pill text-bg-secondary">Не начатые</span></h2>
            </div>
            <c:forEach var="task" items="${requestScope.tasks}">
                <c:if test = "${task.state.label == 'Не начатые'}">
                    <c:set var="editTaskId" value="${task.id}" scope="page" />
                    <div class="row row-cols-1">
                        <div class="col">
                            <div class="card text-start w-75 my-1">
                                <div class="card-header">${task.user.name} ${task.user.lastName}</div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col">
                                            <h5 class="card-title">${task.title}</h5>
                                        </div>
                                        <div class="col-2">
                                            <button type="button" class="btn-close" aria-label="Close" onclick="deleteTask('${editTaskId}')"></button>
                                        </div>
                                    </div>
                                    <div>
                                        <c:forEach var="tag" items="${task.tags}">
                                            <span class="badge" style="background-color: ${tag.color.label.substring(tag.color.label.indexOf('#'))}">${tag.title}</span>
                                        </c:forEach>
                                    </div>
                                    <div class="row justify-content-between">
                                        <div class="col btn-group mt-3">
                                            <button type="button" class="btn btn-secondary dropdown-toggle position-relative" data-bs-toggle="dropdown" aria-expanded="false">
                                                Комментарии
                                                <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                                ${task.commentsCount}
                                                <span class="visually-hidden">Количество комментариев</span>
                                            </span>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <c:forEach var="comment" items="${task.comments}">
                                                    <li class="dropdown-item">${comment.comment}</li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                        <div class="col-5 mt-3">
                                            <button class="btn btn-sm btn-secondary text-light" onclick="openTaskEditor('${editTaskId}')">Редактировать</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
        <div class="col">
            <div class="d-flex align-items-start flex-column">
                <h2><span class="badge rounded-pill text-bg-primary">В процессе</span></h2>
            </div>
            <c:forEach var="task" items="${requestScope.tasks}">
                <c:if test = "${task.state.label == 'В процессе'}">
                    <c:set var="editTaskId" value="${task.id}" scope="page" />
                    <div class="row row-cols-1">
                        <div class="col">
                            <div class="card border-primary text-start w-75 my-1">
                                <div class="card-header">${task.user.name} ${task.user.lastName}</div>
                                <div class="card-body text-primary">
                                    <div class="row">
                                        <div class="col">
                                            <h5 class="card-title">${task.title}</h5>
                                        </div>
                                        <div class="col-2">
                                            <button type="button" class="btn-close" aria-label="Close" onclick="deleteTask('${editTaskId}')"></button>
                                        </div>
                                    </div>
                                    <div>
                                        <c:forEach var="tag" items="${task.tags}">
                                            <span class="badge" style="background-color: ${tag.color.label.substring(tag.color.label.indexOf('#'))}">${tag.title}</span>
                                        </c:forEach>
                                    </div>
                                    <div class="row justify-content-between">
                                        <div class="col btn-group mt-3">
                                            <button type="button" class="btn btn-primary dropdown-toggle position-relative" data-bs-toggle="dropdown" aria-expanded="false">
                                                Комментарии
                                                <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                                ${task.commentsCount}
                                                <span class="visually-hidden">Количество комментариев</span>
                                            </span>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <c:forEach var="comment" items="${task.comments}">
                                                    <li class="dropdown-item">${comment.comment}</li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                        <div class="col-5 mt-3">
                                            <button class="btn btn-sm btn-primary text-light" onclick="openTaskEditor('${editTaskId}')">Редактировать</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
        <div class="col">
            <div class="d-flex align-items-start flex-column">
                <h2><span class="badge rounded-pill text-bg-success">Завершенные</span></h2>
            </div>
            <c:forEach var="task" items="${requestScope.tasks}">
                <c:if test = "${task.state.label == 'Завершенные'}">
                    <c:set var="editTaskId" value="${task.id}" scope="page" />
                    <div class="row row-cols-1">
                        <div class="col">
                            <div class="card border-success text-start w-75 my-1">
                                <div class="card-header">${task.user.name} ${task.user.lastName}</div>
                                <div class="card-body text-success">
                                    <div class="row">
                                        <div class="col">
                                            <h5 class="card-title">${task.title}</h5>
                                        </div>
                                        <div class="col-2">
                                            <button type="button" class="btn-close" aria-label="Close" onclick="deleteTask('${editTaskId}')"></button>
                                        </div>
                                    </div>
                                    <div>
                                        <c:forEach var="tag" items="${task.tags}">
                                            <span class="badge" style="background-color: ${tag.color.label.substring(tag.color.label.indexOf('#'))}">${tag.title}</span>
                                        </c:forEach>
                                    </div>
                                    <div class="row justify-content-between">
                                        <div class="col btn-group mt-3">
                                            <button type="button" class="btn btn-success dropdown-toggle position-relative" data-bs-toggle="dropdown" aria-expanded="false">
                                                Комментарии
                                                <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                                ${task.commentsCount}
                                                <span class="visually-hidden">Количество комментариев</span>
                                            </span>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <c:forEach var="comment" items="${task.comments}">
                                                    <li class="dropdown-item">${comment.comment}</li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                        <div class="col-5 mt-3">
                                            <button class="btn btn-sm btn-success text-light" onclick="openTaskEditor('${editTaskId}')">Редактировать</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
<script>
    function openTaskEditor(taskId) {
        let paramString;
        <c:forEach var="task" items="${requestScope.tasks}">
            if ('${task.id}' === taskId) {
                paramString = "?id=" + '${task.id}' + "&title=" + '${task.title}' + "&state=" + '${task.state.toString()}'
                        + "&userId=" + '${task.user.id}';
                <c:forEach var="tag" items="${task.tags}">
                    paramString += "&tagId=" + ${tag.id};
                </c:forEach>
                <c:forEach var="comment" items="${task.comments}">
                    paramString += "&comment=" + '${comment.comment}';
                </c:forEach>
            }
        </c:forEach>
        window.location.href = '<c:url value="/taskEditor.jsp"/>' + paramString;
    }

    function deleteTask(taskId) {
        let deleteRequest = new XMLHttpRequest();
        deleteRequest.open("DELETE", '<c:url value="/tasks/"/>' + taskId);
        deleteRequest.setRequestHeader("Content-type", "text/html;charset=UTF-8");
        deleteRequest.send();
        deleteRequest.onload = function () {
            if (deleteRequest.status === 204) {
                showMessage("Задача успешно удалена");
                setTimeout(() => {window.location.href = '<c:url value="/tasks/"/>';}, 2000);
            } else {
                showMessage("При удалении произошла ошибка");
            }
        }
    }

    function showMessage(message) {
        let header = document.getElementById("tasksHeader");
        let div = document.createElement("div");
        div.innerHTML = message;
        header.after(div);
        setTimeout(() => div.remove(), 5000);
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
