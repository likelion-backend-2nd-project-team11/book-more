/**
 *  1. 회원가입 시 비밀번호 더블 체크 에러
 *  2. 이메일 중복 에러
 *  3. 닉네임 중복 에러
 *  4. null 값 에러
 */
function getMyPage(token) {
    return fetch(`${BASE_URL}/api/v1/users/me`, {
        method: 'GET',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        },
    }).then(response => response.json())
        .then(data => {
            document.querySelector('.bm-all').innerHTML = `
            <h3>${data.result.nickname}</h3>
                <div class="bm-width">
                   <div class="text-center" style="position: relative;">
                        <hr>
                        <p>프로필 사진</p>
                        <div>
                        <button onclick=updateDefault(token) style="border: none; border-radius: 0.7em; position: absolute; bottom: 60%;margin-left: -60px;">X</button>
                        </div>    
                        <div>
                        <button type="button" data-bs-toggle="modal" data-bs-target="#myprofileModal" style="border: none; border-radius: 0.7em; position: absolute; bottom: 0;margin-left: 35px;">Edit</button>
                        </div>                        
                        <img style="margin: 0 auto;" class="rounded-circle" src="https://www.bookmore.site/${data.result.profile}" width="150px" height="150px">
                    </div>
                    <hr>
                    <button class="bm-editBtn" data-bs-toggle="modal" data-bs-target="#modifyModal" >수정</button>
                    <h4 class="bm-account">내 계정</h4>
                    <p>아이디 : ${data.result.email}</p>
                    <p>비밀번호 : ********** </p>
<!--                    <p>생년월일 : ${data.result.birth[0]}년 ${data.result.birth[1]}월 ${data.result.birth[2]}일</p>-->
                    <p>생년월일 : ${data.result.birth}</p>
                     <button class ="bm-button" type="button" onclick=deleteUser(token)>회원 탈퇴</button>                
                </div>`;
            return data;
        });
}

function deleteUser(token) {
    const delConfirm = window.confirm("회원 탈퇴를 계속 진행하시겠습니까?");
    if (delConfirm) {
        fetch(`${BASE_URL}/api/v1/users/me`, {
            method: 'DELETE',
            headers: {
                "Authorization": token ? "Bearer " + token : '',
            },
        }).then(response => response.json())
            .then(data => {
                if (data.resultCode === 'SUCCESS') {
                    alert("탈퇴 완료");
                    location.href = "../index.html";
                } else if (data.resultCode === 'ERROR') {
                    alert(data.result.message);
                } else {
                    console.log(data);
                }
            });
    }
}

function editUser(token) {
    const nickname = document.getElementById("update-nickname").value;
    const password = document.getElementById("update-password").value;
    const passwordCheck = document.getElementById("update-passwordCheck").value;
    const birth = birthArr.join("-");

    if(password != passwordCheck){
        alert('비밀번호가 일치하지 않습니다.')
        return;
    }

    const data = {
        nickname,
        password : password === "" ? null : password,
        birth
    }

    console.log(token);
    console.log(data);

    fetch(`${BASE_URL}/api/v1/users/me`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": token ? "Bearer " + token : '',
        },
        body: JSON.stringify(data),
    }).then((response) => response.json())
        .then((response) => {
            const resultCode = response.resultCode;
            if (resultCode === 'SUCCESS') {
                alert("수정 완료");
                window.location.reload();
            } else if (resultCode === 'ERROR') {
                alert(response.result.message);
            } else {
                console.log(response);
            }
        })
}

function updateImage(token) {

    const image = document.getElementById('update-image').files[0];

    const formData = new FormData();
    formData.append("multipartFile", image);

    fetch(`${BASE_URL}/api/v1/users/me/profile`, {
        method: 'POST',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        },
        body: formData,
    }).then((response) => response.json())
        .then((response) => {
            const resultCode = response.resultCode;
            if (resultCode === 'SUCCESS') {
                alert("프로필 수정 완료");
                window.location.reload();
            } else if (resultCode === 'ERROR') {
                alert(response.result.message);
            } else {
                console.log(response);
            }
        })
}

function fileTypeCheck(file) {
    let index = file.value.lastIndexOf('.');
    let filetype = file.value.substring(index + 1, file.length).toLowerCase();

    if (filetype === 'jpg' || filetype === 'gif' || filetype === 'png' || filetype === 'jpeg') {
    } else {
        alert("프로필 사진은 이미지 파일만 업로드할 수 있습니다.");
        window.location.reload();
    }
}

function updateDefault(token) {
    const updateConfirm = window.confirm("프로필 사진을 기본 사진으로 변경하시겠습니까?");
    if (updateConfirm) {
        fetch(`${BASE_URL}/api/v1/users/me/profile`, {
            method: 'DELETE',
            headers: {
                "Authorization": token ? "Bearer " + token : '',
            },
        }).then((response) => response.json())
            .then((response) => {
                const resultCode = response.resultCode;
                if (resultCode === 'SUCCESS') {
                    alert("변경 완료");
                    window.location.reload();
                } else if (resultCode === 'ERROR') {
                    alert(response.result.message);
                } else {
                    console.log(response);
                }
            })
    }
}