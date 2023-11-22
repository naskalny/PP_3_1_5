async function loadusers(){
    let rows = $(".table #users-table")
    rows.empty()
    await fetch('/api/users')
        .then(response => response.json())
        .then(users => {
            console.log(users)
            const userTableBody = document.getElementById('users-table');
            users.forEach(user => {
                const row = userTableBody.insertRow();
                const cell1 = row.insertCell(0);
                const cell2 = row.insertCell(1);
                const cell3 = row.insertCell(2);
                const cell4 = row.insertCell(3);
                const cell5 = row.insertCell(4);
                const cell6 = row.insertCell(5);
                const cell7 = row.insertCell(6);
                const cell8 = row.insertCell(7);

                cell1.textContent = user.id;
                cell2.textContent = user.username;
                cell3.textContent = user.name;
                cell4.textContent = user.surname;
                cell5.textContent = user.age;
                var roles = "";
                user.roles.forEach(role => {
                    roles += role.name + "<br>";
                })
                cell6.innerHTML = roles;

                const editButton = document.createElement('button');
                editButton.className = "btn btn-info";
                editButton.textContent = 'Edit';
                editButton.value = user.id;
                editButton.setAttribute("data-bs-toggle", "modal");
                editButton.setAttribute("id", "editButton");
                editButton.addEventListener("click", function() {
                    editFunc(this.value);
                });
                cell7.appendChild(editButton);

                const deleteButton = document.createElement('button');
                deleteButton.className = "btn btn-danger";
                deleteButton.textContent = 'Delete';
                deleteButton.value = user.id;
                deleteButton.setAttribute("data-bs-toggle", "modal");
                deleteButton.setAttribute("id", "deleteButton");
                deleteButton.addEventListener("click", function() {
                    deleteFunc(this.value);
                });
                cell8.appendChild(deleteButton);
            });
        })
        .catch(error => console.error('Error fetching users:', error));
}

async function editFunc(value) {
    let editedUser = await fetch("api/users/" + value)
        .then(response => response.json())
        .then(user => {
            return user
        })
    let select = $("#edit-roless")
    select.empty()
    $("#edit-id").val(editedUser.id)
    $("#edit-username").val(editedUser.username)
    $("#edit-name").val(editedUser.name) // --------------------
    $("#edit-Surname").val(editedUser.surname) // --------------------
    $("#edit-Age").val(editedUser.age)
    let roles = await fetch("api/roles")
        .then(response => response.json())
        .then(rolesList => {
            return rolesList
        })

    for (let role of roles) {
        let option = $("<option/>")
        option.val(role.roleId)
        option.text(role.name)
        for (let userRole of editedUser.roles) {
            if (role.roleId === userRole.roleId) {
                option.attr("selected", true)
                break
            }
        }
        select.append(option)
    }

    $("#editModal").modal("show")
}

document.getElementById('submitEdit').addEventListener('click', async function() {
    var userId = document.getElementById('edit-id').value;
    // Сбор данных из полей модального окна
    var userData = {
        id: userId,
        username: document.getElementById('edit-username').value,
        name: document.getElementById('edit-name').value,
        surname: document.getElementById('edit-Surname').value,
        age: document.getElementById('edit-Age').value,
        password: document.getElementById('edit-Password').value,
        roles: []
    };
    for (let option of $("#edit-roless").children()) {
        if (option.selected) {
            let patchedRoleId = option.value
            await fetch("api/roles/" + patchedRoleId)
                .then(response => response.json())
                .then(role => {
                    userData.roles.push(role)
                })
        }
    }

    await updateUserData(userId, userData)
        .then(response => {
            console.log('Данные успешно обновлены:', response);
            loadusers();
            fillHeader();
            $("#editModal").modal("hide")

        })
        .catch(error => console.error('Ошибка обновления данных:', error));
});

async function updateUserData(userId, userData) {

    return await fetch("/api/users/" + userId, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
    });
}

async function deleteFunc(value) {
    let editedUser = await fetch("api/users/" + value)
        .then(response => response.json())
        .then(user => {
            return user
        })
    let select = $("#delete-roless")
    select.empty()
    $("#delete-id").val(editedUser.id)
    $("#delete-username").val(editedUser.username)
    $("#delete-name").val(editedUser.name) // --------------------
    $("#delete-Surname").val(editedUser.surname) // --------------------
    $("#delete-Age").val(editedUser.age)
    let roles = await fetch("api/roles")
        .then(response => response.json())
        .then(rolesList => {
            return rolesList
        })

    for (let role of roles) {
        let option = $("<option/>")
        option.val(role.roleId)
        option.text(role.name)
        for (let userRole of editedUser.roles) {
            if (role.roleId === userRole.roleId) {
                option.attr("selected", true)
                break
            }
        }
        select.append(option)
    }
    $("#deleteModal").modal("show")
}

document.getElementById('submitDelete').addEventListener('click', async function() {
    var userId = document.getElementById('delete-id').value;

    await deleteUser(userId)
        .then(response => {
            console.log('Пользователь успешно удалён');
            loadusers();
            $("#deleteModal").modal("hide")

        })
        .catch(error => console.error('Ошибка удаления данных:', error));
});

async function deleteUser(userId) {

    return await fetch("/api/users/" + userId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        }
    });
}

async function fillHeader() {
    let usernameSpan = document.getElementById("authHeader");
    let rolesSpan = document.getElementById("authRoles");

    fetch('api/users/current')
        .then(response => response.json())
        .then(data => {
            let username = data.username;
            let roles = data.roles;
            let rolesStr = "";
            roles.forEach(role =>{
                rolesStr += role.name.replace("ROLE_", "");
                rolesStr +=", "
            })
            usernameSpan.innerText = username;
            rolesSpan.innerText = rolesStr.slice(0, -2);

            const userTableBody = document.getElementById('currentUserInfo');
            let rows = $(".table #currentUserInfo")
            rows.empty()
            const row = userTableBody.insertRow();
            const cell1 = row.insertCell(0);
            const cell2 = row.insertCell(1);
            const cell3 = row.insertCell(2);
            const cell4 = row.insertCell(3);
            const cell5 = row.insertCell(4);
            const cell6 = row.insertCell(5);

            cell1.textContent = data.id;
            cell2.textContent = data.username;
            cell3.textContent = data.name;
            cell4.textContent = data.surname;
            cell5.textContent = data.age;

            cell6.innerHTML = rolesStr.slice(0, -2);
        })
        .catch(error => console.error('Ошибка запроса:', error));
}

async function fillRoles(){
    await fetch("api/roles")
        .then(response => response.json())
        .then(rolesList => {
            let select = $("#new_roles");
            rolesList.forEach(role=>{
                let option = $("<option/>")
                option.val(role.roleId)
                option.text(role.name)
                select.append(option)
            })
        })
}

document.getElementById('createUserButton').addEventListener('click', async function() {
    var userId = document.getElementById('delete-id').value;

    var userData = {
        id: userId,
        username: document.getElementById('new_username').value,
        name: document.getElementById('new_name').value,
        surname: document.getElementById('new_surname').value,
        age: document.getElementById('new_age').value,
        password: document.getElementById('new_password').value,
        roles: []
    };
    for (let option of $("#new_roles").children()) {
        if (option.selected) {
            let patchedRoleId = option.value
            await fetch("api/roles/" + patchedRoleId)
                .then(response => response.json())
                .then(role => {
                    userData.roles.push(role)
                })
        }
    }

    await createUser(userId, userData)
        .then(response => {
            console.log('Данные успешно обновлены:', response);
            loadusers();
            $("#myTabs a[href='#content1']").tab('show');
            $('.d-grid input:not(:last), .d-grid select').val('');
            $('.d-grid select').val('');
        })
        .catch(error => console.error('Ошибка обновления данных:', error));
});

async function createUser(userId, userData) {

    return await fetch("/api/users/" + userId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
    });
}