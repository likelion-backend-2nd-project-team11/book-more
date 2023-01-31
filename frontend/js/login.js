const BASE_URL = 'http://api.bookmore.site';

/**
 *  1. password 틀림
 *  2. email 존재하지 않음
 *  3. 로그인 성공 시 -> 검색 페이지?
 */



function login() {

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    // request api
    fetch(`${BASE_URL}/api/v1/users/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email, password,
        }),
    }).then((response) => response.json())
        .then((response) => {
            console.log(response);
            if (response.resultCode === 'SUCCESS') {
                window.localStorage.setItem("token", response.result.jwt);
                alert("로그인 완료")
                window.location.href = "index.html";
            } else if (response.resultCode === 'ERROR') {
                alert(response.result.message);
            }
        });

}

function submitLoginHandler(e) {
    let key = e.key || e.keyCode;

    if (key === 'Enter' || key === 13) {
        login();
    }
}