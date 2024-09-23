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
<body onload="setFormData()">
<jsp:include page="header.jsp"/>
<div class="container text-center" style="min-height: 85vh">
    <h1 id="taskEditorHeader" class="text-start ps-5 pt-5">Редактировать задачу</h1>
    <form class="mt-5 row" onsubmit="editTask();return false" name="editTaskForm">
        <div class="col">
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskTitleForEdit" class="col-4 col-form-label text-start fs-4">Название</label>
                <div class="col-5">
                    <input id="taskTitleForEdit" class="form-control-lg w-100" name="title" type="text" value="${requestScope.title}">
                </div>
            </div>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskUserForEdit" class="col-4 col-form-label text-start fs-4">Исполнитель</label>
                <div class="col-5">
                    <select id="taskUserForEdit" class="form-select-lg w-100" name="user">
                        <c:forEach var="user" items="${applicationScope.Users}">
                            <option value=${user.id}>${user.name} ${user.lastName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskStateForEdit" class="col-4 col-form-label text-start fs-4">Статус</label>
                <div class="col-5">
                    <select id="taskStateForEdit"  class="form-select-lg w-100" name="state">
                        <c:forEach var="state" items="${applicationScope.States}">
                            <option value=${state}>${state.toString()}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <div class="col-4 col-form-label text-start fs-4">Комментарии</div>
                <div class="col-5">
                    <div id="taskComments" class="border rounded w-100 h-100"></div>
                </div>
            </div>
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskCommentForEdit" class="col-4 col-form-label text-start fs-5">Новый комментарий</label>
                <div class="col-5">
                    <input id="taskCommentForEdit" class="form-control-lg w-100" name="comment" type="text" value="">
                </div>
            </div>
            <div class="row justify-content-center my-3">
                <div class="col-5">
                    <button class="btn btn-info btn-lg text-light w-100 fs-4" type="submit">Редактировать</button>
                </div>
                <div class="col-5">
                    <a class="btn btn-info btn-lg text-light w-100 fs-4" href="${pageContext.request.contextPath}/tasks/">В список задач</a>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="row justify-content-start my-3">
                <div class="col-2"></div>
                <label for="taskTagForEdit" class="col-4 col-form-label text-start fs-4">Тэги</label>
                <div class="col-5">
                    <select id="taskTagForEdit" class="form-select-lg w-100" name="tags" multiple>
                        <c:forEach var="tag" items="${applicationScope.Tags}">
                            <option value=${tag.id}>${tag.title}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row justify-content-center my-3">
                <div class="col">
                    <a class="btn btn-info btn-lg text-light w-100 fs-6 text-decoration-none" href="${pageContext.request.contextPath}/taskCreator.jsp">Создать тэг</a>
                </div>
                <div class="col">
                    <button class="btn btn-info btn-lg text-light w-100 fs-6" data-bs-toggle="modal" data-bs-target="#exampleModal">Редактировать тэг</button>
                </div>
                <div class="col">
                    <button class="btn btn-info btn-lg text-light w-100 fs-6" onclick="deleteTag()">Удалить тэги</button>
                </div>
            </div>
        </div>
    </form>
</div>
<jsp:include page="footer.jsp"/>
<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="tagEditorHeader" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-3" id="tagEditorHeader">Редактировать тэг</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-header">
                <label for="tagForEdit" class="col-8 col-form-label text-start fs-5">
                    <select id="tagForEdit" class="form-select-lg w-100" name="tags">
                        <c:forEach var="tag" items="${applicationScope.Tags}">
                            <option value=${tag.id}>${tag.title}</option>
                        </c:forEach>
                    </select>
                </label>
            </div>
            <form class="col" onsubmit="editTag();return false" name="editTagForm">
                <div class="modal-body">
                    <div class="row justify-content-between my-3">
                        <label for="tagTitleForEdit" class="col col-form-label text-start fs-4">Поменять название</label>
                        <div class="col-6">
                            <input id="tagTitleForEdit" class="form-control-lg w-100" name="title" type="text" value="">
                        </div>
                    </div>
                    <div class="row justify-content-between my-3">
                        <label for="tagColorForEdit" class="col-5 col-form-label text-start fs-4">Выбрать цвет</label>
                        <div class="col-6">
                            <select id="tagColorForEdit"  class="form-select-lg w-100" name="color">
                                <c:forEach var="color" items="${applicationScope.Colors}">
                                    <option value=${color}>${color.toString().substring(0, color.toString().indexOf(':')) }</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="row justify-content-center my-3">
                        <button class="btn btn-info text-light w-100 fs-4" type="submit">Редактировать тэг</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    function setFormData() {
        const currentUrl = new URL(window.location.href);
        const params = new URLSearchParams(currentUrl.search);
        let editTitle = document.getElementById("taskTitleForEdit");
        let editUser = document.getElementById("taskUserForEdit");
        let editState = document.getElementById("taskStateForEdit");
        let editTags = document.getElementById("taskTagForEdit");
        let comments = document.getElementById("taskComments");

        editTitle.value = params.get("title");

        <c:forEach var="user" items="${applicationScope.Users}">
        if ('${user.id}' === params.get("userId")) {
            let searchText = '${user.name}' + " " + '${user.lastName}';
            let option;
            for (option of editUser.options) {
                if (option.text === searchText) {
                    option.selected = "true";
                }
            }
        }
        </c:forEach>

        <c:forEach var="state" items="${applicationScope.States}">
        if ('${state.toString()}' === params.get("state")) {
            let option;
            for (option of editState.options) {
                if (option.text === '${state.toString()}') {
                    option.selected = "true";
                }
            }
        }
        </c:forEach>

        let incomeComments = params.getAll("comment");
        let resultString = "";
        for (let x of incomeComments) {
            resultString += x + "; ";
        }
        comments.innerHTML = resultString;

        let incomeTags = params.getAll("tagId");
        for (let x of incomeTags) {
            <c:forEach var="tag" items="${applicationScope.Tags}">
            if ('${tag.id}' === x) {
                let option;
                for (option of editTags.options) {
                    if (option.text === '${tag.title}') {
                        option.selected = "true";
                    }
                }
            }
            </c:forEach>
        }
    }

    function editTask() {
        const currentUrl = new URL(window.location.href);
        const params = new URLSearchParams(currentUrl.search);
        let editId = params.get("id");
        let editTitle = document.getElementById("taskTitleForEdit").value;
        let editUserId = document.getElementById("taskUserForEdit").value;
        let editState = document.getElementById("taskStateForEdit").value;
        let checkedOptions = document.getElementById("taskTagForEdit").selectedOptions;
        let editTags = Array.from(checkedOptions).map(({ value }) => value);

        let paramString = "?title=" + editTitle + "&userId=" + editUserId + "&state=" + editState;
        for (let x of editTags) {
            paramString += "&tagId=" + x;
        }
        let editRequest = new XMLHttpRequest();
        editRequest.open("PUT", '<c:url value="/tasks/"/>' + editId + paramString);
        editRequest.setRequestHeader("Content-type", "text/html;charset=UTF-8");
        editRequest.send();
        editRequest.onload = function () {
            if (editRequest.status === 200) {
                showMessage("Задача успешно отредактирована", "taskEditorHeader");
                createComment();
            } else {
                showMessage("При редактировании произошла ошибка", "taskEditorHeader");
            }
        }
    }

    function createComment() {
        let createTitle = document.getElementById("taskTitleForEdit").value;
        let createUser = document.getElementById("taskUserForEdit").value;
        let createComment = document.getElementById("taskCommentForEdit").value;

        let createData = {
            title: createTitle,
            userId: createUser,
            comment: createComment
        }
        console.log("check");
        let body = JSON.stringify(createData);
        let createRequest = new XMLHttpRequest();
        createRequest.open("POST", '<c:url value="/comments/"/>');
        createRequest.setRequestHeader("Content-type", "application/json");
        createRequest.send(body);
        createRequest.onload = function () {
            if (createRequest.status === 201) {
                showMessage("Комментарий успешно добавлен", "taskEditorHeader");
                clearTaskFields();
            } else {
                showMessage("При создании комментария произошла ошибка", "taskEditorHeader");
            }
        }
    }

    function editTag() {
        let editId = document.getElementById("tagForEdit").value;
        let editTitle = document.getElementById("tagTitleForEdit").value;
        let editColor = document.getElementById("tagColorForEdit").value;

        let paramString = "?title=" + editTitle + "&color=" + editColor;
        let editRequest = new XMLHttpRequest();
        editRequest.open("PUT", '<c:url value="/tags/"/>' + editId + paramString);
        editRequest.setRequestHeader("Content-type", "text/html;charset=UTF-8");
        editRequest.send();
        editRequest.onload = function () {
            if (editRequest.status === 200) {
                showMessage("Тэг успешно отредактирован", "tagEditorHeader");
                setTimeout(() => location.reload(), 500);
            } else {
                showMessage("При редактировании произошла ошибка", "tagEditorHeader");
            }
        }
    }

    function deleteTag() {
        let checkedOptions = document.getElementById("taskTagForEdit").selectedOptions;
        let deleteTags = Array.from(checkedOptions).map(({ value }) => value);

        let paramString = "";
        for (let i = 0; i < deleteTags.length; i++) {
            if (i === 0) {
                paramString += "?tagId=" + deleteTags[i];
            } else {
                paramString += "&tagId=" + deleteTags[i];
            }
        }

        let deleteRequest = new XMLHttpRequest();
        deleteRequest.open("DELETE", '<c:url value="/tags/"/>' + paramString);
        deleteRequest.setRequestHeader("Content-type", "text/html;charset=UTF-8");
        deleteRequest.send();
        deleteRequest.onload = function () {
            if (deleteRequest.status === 204) {
                showMessage("Тэги успешно удалены", "taskEditorHeader");
                setTimeout(() => location.reload(), 2000);
            } else {
                showMessage("При удалении произошла ошибка", "taskEditorHeader");
            }
        }
    }

    function showMessage(message, anchorId) {
        let header = document.getElementById(anchorId);
        let div = document.createElement("div");
        div.innerHTML = message;
        header.after(div);
        setTimeout(() => div.remove(), 5000);
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>

