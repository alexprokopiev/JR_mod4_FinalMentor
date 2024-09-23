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
    <h1 id="taskCreatorHeader" class="text-start ps-5 pt-5">Добавить новую задачу</h1>
    <div class="mt-5 row">
        <form class="col" onsubmit="createTask();return false" name="createTaskForm">
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskTitleForCreate" class="col-4 col-form-label text-start fs-4">Название</label>
                <div class="col-5">
                    <input id="taskTitleForCreate" class="form-control-lg w-100" name="title" type="text" value="">
                </div>
            </div>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskUserForCreate" class="col-4 col-form-label text-start fs-4">Исполнитель</label>
                <div class="col-5">
                    <select id="taskUserForCreate" class="form-select-lg w-100" name="user">
                        <c:forEach var="user" items="${applicationScope.Users}">
                            <option value=${user.id}>${user.name} ${user.lastName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskTagForCreate" class="col-4 col-form-label text-start fs-4">Тэги</label>
                <div class="col-5">
                    <select id="taskTagForCreate" class="form-select-lg w-100" name="tags" multiple>
                        <c:forEach var="tag" items="${applicationScope.Tags}">
                            <option value=${tag.title}>${tag.title}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskCommentForCreate" class="col-4 col-form-label text-start fs-4">Комментарий</label>
                <div class="col-5">
                    <input id="taskCommentForCreate" class="form-control-lg w-100" name="comment" type="text" value="">
                </div>
            </div>
            <div class="row justify-content-center my-3">
                <div class="col-5">
                    <button class="btn btn-info btn-lg text-light w-100 fs-4" type="submit">Создать задачу</button>
                </div>
                <div class="col-5">
                    <a class="btn btn-info btn-lg text-light w-100 fs-4 text-decoration-none" href="${pageContext.request.contextPath}/tasks/">В список задач</a>
                </div>
            </div>
        </form>
        <form class="col" onsubmit="createTag();return false" name="createTagForm">
            <h2 id="tagCreatorHeader" class="text-start ps-5 pt-5 mb-5">Добавить новый тэг</h2>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="tagTitleForCreate" class="col-4 col-form-label text-start fs-4">Название</label>
                <div class="col-5">
                    <input id="tagTitleForCreate" class="form-control-lg w-100" name="title" type="text" value="">
                </div>
            </div>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="tagColorForCreate" class="col-4 col-form-label text-start fs-4">Цвет</label>
                <div class="col-5">
                    <select id="tagColorForCreate"  class="form-select-lg w-100" name="color">
                        <c:forEach var="color" items="${applicationScope.Colors}">
                            <option value=${color}>${color.toString().substring(0, color.toString().indexOf(':')) }</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row justify-content-center my-3">
                <div class="col-6">
                    <button class="btn btn-info btn-lg text-light w-100 fs-4" type="submit">Создать тэг</button>
                </div>
            </div>
        </form>
    </div>
</div>
<jsp:include page="footer.jsp"/>
<script>
    function createTag() {
        let createTitle = document.getElementById("tagTitleForCreate").value;
        let createColor = document.getElementById("tagColorForCreate").value;

        let createData = {
            title: createTitle,
            color: createColor,
        }
        let body = JSON.stringify(createData);
        let createRequest = new XMLHttpRequest();
        createRequest.open("POST", '<c:url value="/tags/"/>');
        createRequest.setRequestHeader("Content-type", "application/json");
        createRequest.send(body);
        createRequest.onload = function () {
            if (createRequest.status === 201) {
                showMessage("Тэг успешно добавлен");
                clearTagFields();
                location.reload();
            } else {
                showMessage("При создании произошла ошибка");
            }
        }
    }

    function createTask() {
        let createTitle = document.getElementById("taskTitleForCreate").value;
        let createUser = document.getElementById("taskUserForCreate").value;
        let checkedOptions = document.getElementById("taskTagForCreate").selectedOptions;
        let createTags = Array.from(checkedOptions).map(({ value }) => value);

        let createData = {
            title: createTitle,
            userId: createUser,
            tagTitles: createTags
        }
        let body = JSON.stringify(createData);
        let createRequest = new XMLHttpRequest();
        createRequest.open("POST", '<c:url value="/tasks/"/>');
        createRequest.setRequestHeader("Content-type", "application/json");
        createRequest.send(body);
        createRequest.onload = function () {
            if (createRequest.status === 201) {
                showMessage("Задача успешно добавлена");
                createComment();
            } else {
                showMessage("При создании задачи произошла ошибка");
            }
        }
    }

    function createComment() {
        let createTitle = document.getElementById("taskTitleForCreate").value;
        let createUser = document.getElementById("taskUserForCreate").value;
        let createComment = document.getElementById("taskCommentForCreate").value;

        let createData = {
            title: createTitle,
            userId: createUser,
            comment: createComment
        }
        let body = JSON.stringify(createData);
        let createRequest = new XMLHttpRequest();
        createRequest.open("POST", '<c:url value="/comments/"/>');
        createRequest.setRequestHeader("Content-type", "application/json");
        createRequest.send(body);
        createRequest.onload = function () {
            if (createRequest.status === 201) {
                showMessage("Комментарий успешно добавлен");
                clearTaskFields();
            } else {
                showMessage("При создании комментария произошла ошибка");
            }
        }
    }

    function showMessage(message) {
        let header = document.getElementById("taskCreatorHeader");
        let div = document.createElement("div");
        div.innerHTML = message;
        header.after(div);
        setTimeout(() => div.remove(), 5000);
    }

    function clearTagFields(){
        document.getElementById("tagTitleForCreate").value = "";
    }

    function clearTaskFields(){
        document.getElementById("taskTitleForCreate").value = "";
        document.getElementById("taskTagForCreate").selectedIndex = -1;
        document.getElementById("taskCommentForCreate").value = "";
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
