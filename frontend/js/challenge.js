function fetchCreateChallenge(token) {
    const title = document.getElementById("create-title").value;
    const description = document.getElementById("create-description").value;
    const deadline = deadlineArr.join("-");
    const progress = document.getElementById("create-progress").value;

    const data = {
        title,
        description,
        deadline,
        progress
    }

    fetch(`${BASE_URL}/api/v1/challenges`, {
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
            alert("등록 완료");
            window.location.reload();
        } else if (resultCode === 'ERROR') {
            alert(response.result.message);
        } else {
            console.log(response);
        }
    })
}


function fetchGetChallenges(token) {
    const result = fetch(`${BASE_URL}/api/v1/challenges`, {
        method: 'GET',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        }
    }).then((response) => response.json())
        .then((res) => {
            console.log(res);
            const challenges = res.result.content;
            const result = {};
            challenges.forEach(item => result[item.id] = item);
            document.querySelector('.bm-challenge-wrapper').innerHTML= challenges.map(challenge => {
                return `
                <div class="card bg-white mt-4 shadow bm-scale-animation">
                    <div class="card-header d-flex align-items-center">
                        <span class="h4 m-0">${challenge.title}</span>
                        <span class="ms-auto">${challenge.deadline}</span>
                    </div>
                    <div class="card-body">
                        <h5>${challenge.description}</h5>
                        <p>진행도</p>
                        <div class="progress mb-3">
                            <div class="progress-bar" role="progressbar" aria-label="progress" style="width: ${challenge.progress}%;" aria-valuenow="${challenge.progress}" aria-valuemin="0" aria-valuemax="100">${challenge.progress}%</div>
                        </div>
                        <div class="d-flex">
                            <button class="btn btn-primary ms-auto" type="button" onclick="modifyChallenge(challenges[${challenge.id}])" data-bs-toggle="modal" data-bs-target="#modifyModal">수정</button>
                            <button class="btn btn-primary ms-2" type="button" onclick="fetchDeleteChallenge(${challenge.id}, token)">삭제</button>
                        </div>
                    </div>
                </div>`
            }).join("\n");
            return result;
        })
    return result;
}

function modifyChallenge({id, title, description, deadline, progress}) {
    document.getElementById("modify-title").value = title;
    document.getElementById("modify-description").value = description;
    setMydeadline(deadline);
    document.getElementById("modify-progress").value = progress;
    document.getElementById("modify-progress-value").innerText = progress;
    document.getElementById("modify-submit-btn").setAttribute("onclick", `fetchModifyChallenge(${id}, token)`)
}

function fetchModifyChallenge(id, token) {
    const title = document.getElementById("modify-title").value;
    const description = document.getElementById("modify-description").value;
    const deadline = editDeadlineArr.join("-");
    const progress = document.getElementById("modify-progress").value;

    const data = {
        title,
        description,
        deadline,
        progress
    }

    console.log(token);
    console.log(data);
    fetch(`${BASE_URL}/api/v1/challenges/${id}`, {
        method: 'PATCH',
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

function fetchDeleteChallenge(id, token) {
    fetch(`${BASE_URL}/api/v1/challenges/${id}`, {
        method: 'DELETE',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        }
    }).then((response) => response.json())
        .then((response) => {
            const resultCode = response.resultCode;
            if (resultCode === 'SUCCESS') {
                alert("삭제 완료");
                window.location.reload();
            } else if (resultCode === 'ERROR') {
                alert(response.result.message);
            } else {
                console.log(response);
            }
        })
}

// ReadableStream 사용 방법(Back 에서 Page 객체 return 한 결과) or back 코드 리턴 타입 고치기
// 챌린지 리스트 불러올때 로그인한 사용자 아이디 따와서 로그인한 사용자가 쓴 챌린지만 불러오기