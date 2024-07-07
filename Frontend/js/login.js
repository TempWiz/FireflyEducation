// Login
$(function () {
    $("#login").click(function () {
        var email = $("#email").val();
        var password = $("#password").val();
        if (email == "" || password == "") {
            alert("Please fill all fields...!!!!!!");
        } else {
            if ((email == "admin@gmail.com") && (password == "password")) {
                $("form")[0].submit();
            }
            else {
                alert("Email or Password is incorrect...!!!!!!");
            }
        }
    }
    );
}
);