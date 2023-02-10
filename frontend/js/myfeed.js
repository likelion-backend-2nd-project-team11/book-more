function fetchGetMyId(token) {
    return fetch(`${BASE_URL}/api/v1/users/me`, {
        method: 'GET',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        },
    }).then((response) => response.json())
        .then((response) => {
            const id = response.result.id;
            return id;
        })
}

function fetchGetMyImage(id) {
    return fetch(`${BASE_URL}/api/v1/users/${id}`, {
        method: 'GET',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        },
    }).then((response) => response.json())
        .then((response) => {
            if (response.resultCode === 'SUCCESS') {
                const wrapper = document.querySelector(".bm-profile-wrapper");
                wrapper.insertAdjacentHTML('afterbegin',
                    `<img class="bm-profile-imgrounded-circle ms-auto mt-4 me-3" style="width:200px; height:200px; border-radius: 70%;" src="https://www.bookmore.site/${response.result.profile}">`);
            } else {
                window.history.back();
            }
        })
}

function fetchGetName(id) {
    return fetch(`${BASE_URL}/api/v1/users/${id}`, {
        method: 'GET'
    }).then((response) => response.json())
        .then((response) => {
            const wrapper = document.querySelector(".me-auto");
            wrapper.insertAdjacentHTML('afterbegin',
                `<div class="m-3 d-flex flex-column">
                        <h3 id="myNickname" class="text-center mt-3" style ="font-size:1.8em;">@${response.result.nickname}</h3>
                      </div>`);
        })
}

function fetchGetFollower(id) {
    fetch(`${BASE_URL}/api/v1/users/${id}/follower`, {
        method: 'GET'
    }).then((response) => response.json())
        .then((response) => {
            const followers = response.result.content;
            document.querySelector('.bm-profile-follower').innerText = followers.length;
        })
}

function fetchGetFollowing(id) {
    fetch(`${BASE_URL}/api/v1/users/${id}/following`, {
        method: 'GET'
    }).then((response) => response.json())
        .then((response) => {
            const followings = response.result.content;
            document.querySelector('.bm-profile-following').innerText = followings.length;
        })
}

function fetchDeleteUnfollow(id, token) {
    console.log("함수에서 id" + id);
    console.log("함수에서 token" + token);
    fetch(`${BASE_URL}/api/v1/users/${id}/follow`, {
        method: 'DELETE',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        },
    }).then((response) => response.json())
        .then((response) => {
            const resultCode = response.resultCode;
            const errorCode = response.result.errorCode;
            if (resultCode === 'SUCCESS') {
                alert("팔로우가 취소되었습니다.");
                window.location.href = `detail.html?id=${id}`;
            } else if (resultCode === 'ERROR') {
                alert(response.result.message);
                if (errorCode === 'USER_NOT_FOUND' || errorCode === 'FOLLOW_NOT_FOUND') {
                    window.location.href = '../users/detail.html';
                } else {
                    alert("잘못된 요청입니다.");
                    window.location.href = '../users/detail.html';
                }
            } else {
                console.log(response);
            }
        })
}

function fetchPostFollow(id, token) {
    fetch(`${BASE_URL}/api/v1/users/${id}/follow`, {
        method: 'POST',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        },
    }).then((response) => response.json())
        .then((response) => {
            const resultCode = response.resultCode;
            const errorCode = response.result.errorCode;
            if (resultCode === 'SUCCESS') {
                alert("팔로우 되었습니다.");
                window.location.href = `detail.html?id=${id}`;
            } else if (resultCode === 'ERROR') {
                if (errorCode === 'USER_NOT_FOUND' || errorCode === 'FOLLOW_NOT_ME' || errorCode === 'DUPLICATED_FOLLOW') {
                    alert(response.result.message);
                    window.location.href = '../users/detail.html';
                } else {
                    alert("로그인이 필요합니다.");
                    window.location.href = '../users/detail.html';
                }
            } else {
                console.log(response);
            }
        })
}

function followButton(id, token) {
    if (!!id) { //id가 있을 때
        fetch(`${BASE_URL}/api/v1/users/${id}/follow`, {
            method: 'GET',
            headers: {
                "Authorization": token ? "Bearer " + token : '',
            },
        }).then((response) => response.json())
            .then((response) => {
                const isFollow = response.result;
                fetchGetMyId(token).then((result) => {
                    if (isFollow === true) { //언팔로우 버튼이 있어야 함.
                        document.querySelector('#followButton').innerHTML =
                            `<button onclick="fetchDeleteUnfollow(${id}, '${token}')" class="bm-profile-follow-btn btn btn-light" style="border-color: darkgrey; color: grey">팔로잉</button>`;
                    } else if (result == id) {  //나 일때
                        document.querySelector('#followButton').innerHTML =
                            `<button class="bm-profile-follow-btn btn btn-primary" style="visibility : hidden;">나</button>`;
                    } else { // 팔로우 버튼이 있어야함.
                        document.querySelector('#followButton').innerHTML =
                            `<button onclick="fetchPostFollow(${id}, '${token}')"class="bm-profile-follow-btn btn btn-primary" >팔로우</button>`;
                    }
                })
            })
    } else {  //id가 없을 때(나)
        document.querySelector('#followButton').innerHTML =
            `<button class="bm-profile-follow-btn btn btn-primary" style="visibility : hidden;">나</button>`;
    }

}

function followerModal(id) {
    fetch(`${BASE_URL}/api/v1/users/${id}/follower`, {
        method: 'GET'
    }).then((response) => response.json())
        .then((response) => {
            const followers = response.result.content;
            let result = followers.map(follower => {
                return `<div class="row mb-3">
                            <a class="d-flex align-items-center text-decoration-none text-dark" href="detail.html?id=${follower.followerId}">
                            <img class="bm-profile-img rounded-circle me-3" style="width:65px; height:65px; border-radius: 70%;" src="https://www.bookmore.site/${follower.profile}">
                            <h3 class="fs-5">@${follower.nickname}</h3>
                            </a>
                        </div>`;
            })
            document.querySelector('#followerModalBody').innerHTML = result.join('\n');
        })
}

function followingModal(id) {
    fetch(`${BASE_URL}/api/v1/users/${id}/following`, {
        method: 'GET'
    }).then((response) => response.json())
        .then((response) => {
            const followings = response.result.content;
            let result = followings.map(following => {
                return `<div class="row mb-3">
                            <a class="d-flex align-items-center text-decoration-none text-dark" href="detail.html?id=${following.followingId}">
                                <img class="bm-profile-img rounded-circle me-3" style="width:65px; height:65px; border-radius: 70%;"src="https://www.bookmore.site/${following.profile}">
                                <h3 class="fs-5">@${following.nickname}</h3>
                            </a>
                        </div>`;
            })
            document.querySelector('#followingModalBody').innerHTML = result.join('\n');
        })
}

const labels = [
    '전문성',
    '재미',
    '가독성',
    '소장가치',
    '난이도'
]

const options = {
    responsive: false,
    plugins: {
        legend: {
            display: false
        },
    },
    elements: {
        line: {
            borderWidth: 1
        }
    },
    scales: {
        r: {
            angleLines: {
                display: false
            },
            suggestedMin: 0,
            suggestedMax: 5
        }
    }
}

function fetchGetReview(id) {
    fetch(`${BASE_URL}/api/v1/users/${id}/reviews`, {
        method: 'GET'
    }).then((response) => response.json())
        .then((res) => {
            const wrapper = document.querySelector(".bm-my-reviews-wrapper");
            const reviews = res.result.content;
            if (reviews.length >= 1) {
                console.log("리뷰있음" + reviews.length);
                reviews.forEach(review => {
                    wrapper.insertAdjacentHTML('beforeend',
                        `<div class="bm-review-col p-3 bm-scale-animation " style="text-align: center">
                                    <div class="bm-review-item border border-secondary p-3 rounded-3">
                                        <canvas class="chart me-3" style="width:100%;" id="chart-${review.id}"></canvas>
                                        <hr/>
                                        <a class="d-flex align-items-center text-decoration-none text-dark" href="../books/detail.html?isbn=${review.isbn}">
                                        <div class="mx-auto overflow-hidden text-nowrap">${review.title}</div>
                                        </a>
                                    </div>
                                </div>`);

                    new Chart(document.getElementById(`chart-${review.id}`), {
                        type: 'radar',
                        data: {
                            labels,
                            datasets: [{
                                data: [
                                    review.professionalism,
                                    review.fun,
                                    review.readability,
                                    review.collectible,
                                    review.difficulty
                                ],
                                fill: true,
                                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                                borderColor: 'rgb(255, 99, 132)',
                                pointBackgroundColor: 'rgb(255, 99, 132)',
                                pointBorderColor: '#fff',
                                pointHoverBackgroundColor: '#fff',
                                pointHoverBorderColor: 'rgb(255, 99, 132)'
                            }]
                        },
                        options
                    });
                })
            } else {
                console.log("실행됨");
                const wrapper = document.querySelector(".bm-my-reviews-wrapper");
                wrapper.insertAdjacentHTML('beforeend',
                    `<img class="bm-profile-img mx-auto" src="https://www.bookmore.site/images/3ecf29a049d947ad.jpeg">`);
            }
        })
}
