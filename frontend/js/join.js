/**
 *  1. 회원가입 시 비밀번호 더블 체크 에러
 *  2. 이메일 중복 에러
 *  3. 닉네임 중복 에러
 *  4. null 값 에러
 */

function join() {

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const passwordCheck = document.getElementById('password2').value;
    const nickname = document.getElementById('name').value;
    const birth = birthArr.join("-");

    if(password != passwordCheck){
        alert('비밀번호가 일치하지 않습니다.')
        return;
    }
    // request api
    fetch(`${BASE_URL}/api/v1/users/join`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email, password, nickname, birth,
        }),
    }).then((response) => response.json())
    .then((response) => {
        console.log(response);
        if (response.resultCode === 'SUCCESS') {
            alert("회원가입 완료")
            window.location.href = "login.html";
        } else if (response.resultCode === 'ERROR') {
            alert(response.result.message);
        }
    });
}