/**
 *  1. 회원가입 시 비밀번호 더블 체크 에러
 *  2. 이메일 중복 에러
 *  3. 닉네임 중복 에러
 *  4. null 값 에러
 */
function getMyPage(token) {
    fetch(`${BASE_URL}/api/v1/users/me`, {
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
                        <button type="button" data-bs-toggle="modal" data-bs-target="#myprofileModal" style="border: none; border-radius: 0.7em; position: absolute; bottom: 0;margin-left: 35px;">Edit</button>
                        </div>
                        
                        
<!--                         <div class="form-floating mb-3 text-center">-->
<!--                                            <input type="file" id="update-image" name="avatar" accept="image/png, image/jpeg">-->
<!--                                            <button class="btn btn-primary w-70" onclick="updateImage(token)">프로필 변경</button>-->
<!--                                        </div>-->

<!--                                       <div class="filebox">-->
<!--                                            <input id="update-image" name="avatar" accept="image/png, image/jpeg" class="upload-name" style="display: inline-block;height: 40px;padding: 0 10px;vertical-align: middle;border:1px solid #dddddd; width: 62%; color: #999999;float: right;" value="첨부파일" placeholder="첨부파일">-->
<!--                                            <label style="display:inline-block;padding: 10px 20px; color: #fff;vertical-align: middle; background-color: #999999;cursor: pointer;height: 40px;margin-left: 10px;" for="file">파일찾기</label> -->
<!--                                            <input style="position: absolute; width: 0;height: 0;padding: 0;overflow: hidden;border: 0;" type="file" id="file">-->
<!--                                            <button class="w-70" style="float: right; border: none; border-radius: 0.7em;" onclick="updateImage(token)">프로필 변경</button>-->
<!--                                        </div>-->
                        
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
                </div>
                
                    <div class="modal fade" id="modifyModal" tabindex="-1" aria-labelledby="modifyModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5 " id="modifyModalLabel">프로필 변경</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row m-4">
                                <div class="input-form col-md-12 mx-auto">
                                    <div>
        
 
        
                                        <div class="form-floating mb-3">
                                            <input type="text" class="form-control" id="update-nickname" value="${data.result.nickname}" placeholder="제목을 입력하세요." required>
                                            <label for="update-nickname">닉네임</label>
                                        </div>
        
                                        <div class="form-floating mb-3">
                                            <input type="password" class="form-control"  id="update-password" placeholder="제목을 입력하세요." required>
                                            <label for="update-password">비밀번호</label>
                                        </div>
        
<!--                                        <div class="form-floating mb-3">-->
<!--                                            <input type="password" class="form-control"  id="pass check" placeholder="제목을 입력하세요." required>-->
<!--                                            <label for="update-password">비밀번호 확인</label>-->
<!--                                        </div>-->
        
                                        <div class="form-floating mb-3">
                                            <input type="text" class="form-control" id="update-birth" value="${data.result.birth}" placeholder="1900-01-01" required>
                                            <label for="update-birth">생년월일 </label>
                                        </div>
        
        
                                        <div class="mt-3">
                                            <button class="btn btn-primary w-100" onclick="editUser(token)">수정</button>
        <!--                                    <button class="btn btn-primary w-100" onclick="editUser(token), updateImage(token)">수정</button>-->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
`;
        });
};

// $("#file").on('change',function(){
//     var fileName = $("#file").val();
//     $(".upload-name").val(fileName);
// });









function deleteUser(token) {
    fetch(`${BASE_URL}/api/v1/users/me`, {
        method: 'DELETE',
        headers: {
            // 'Content-Type': 'application/json',
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
        })
}

function editUser(token) {
    const nickname = document.getElementById("update-nickname").value;
    const password = document.getElementById("update-password").value;
    const birth = document.getElementById("update-birth").value;

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

