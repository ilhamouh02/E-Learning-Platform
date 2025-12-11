$(document).ready(function () {
    $('#registration-form').submit(function (event) {
        event.preventDefault();

        var userDto = {
            name: $('#name').val(),
            surname: $('#surname').val(),
            email: $('#email').val(),
            imgUrl: $('#imgUrl').val(),
            username: $('#username').val(),
            password: $('#password').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/auth/register',
            data: JSON.stringify(userDto),
            contentType: 'application/json',
            success: function () {
                window.location.href = '/login.html?registered';
            },
            error: function (xhr) {
                var error = xhr.responseJSON;
                if (error && error.message) {
                    alert(error.message);
                } else {
                    alert('An error occurred during registration.');
                }
            }
        });
    });

    $('#login-form').submit(function (event) {
        event.preventDefault();

        var userDto = {
            email: $('#username').val(),
            password: $('#password').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/auth/login',
            data: JSON.stringify(userDto),
            contentType: 'application/json',
            success: function (data) {
                localStorage.setItem('token', data.token);
                window.location.href = '/discover.html';
            },
            error: function (xhr) {
                var error = xhr.responseJSON;
                if (error && error.message) {
                    alert(error.message);
                } else {
                    alert('An error occurred during login.');
                }
            }
        });
    });
});
