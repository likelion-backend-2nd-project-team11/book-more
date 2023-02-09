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

async function fetchGetReviewsByBook(isbn, getUserInfo) {
    let userInfo = await getUserInfo;
    return fetch(`${BASE_URL}/api/v1/books/${isbn}/reviews`, {
        method: 'GET'
    }).then((response) => response.json())
        .then((res) => {
            console.log(res);
            const wrapper = document.querySelector(".bm-reviews-wrapper");
            const reviews = res.result.content;
            const result = {};
            reviews.forEach(review => {
                result[review.id] = review;
                const origin = new Date(review.createdDatetime);
                const year = origin.getFullYear();
                const month = origin.getMonth();
                const date = origin.getDate();
                const hour = origin.getHours();
                const minute = origin.getMinutes();
                const second = origin.getSeconds();
                const utcDate = new Date(Date.UTC(year, month, date, hour, minute, second));
                const dateString = utcDate.toLocaleString();
                wrapper.insertAdjacentHTML('beforeend',
                    `<article class="review-item mb-5 p-3 pb-5 shadow bg-white">
                    <div class="review-content  d-flex">
                    <canvas class="chart me-3" style="width: 35%" id="chart-${review.id}"></canvas>
                    <div style="width: 100%">
                        <a class="d-flex align-items-center text-decoration-none text-dark" href="../users/detail.html?id=${review.userId}">
                        <h4>@${review.nickname}</h4>
                        </a>
                        <p id="spoiler-${review.id}" class="spoiler" style="display: ${review.spoiler ? 'none' : 'block'}">
                            ${review.body}
                        </p>
                        <p id="spoilerText-${review.id}" class="spoilerText" style="display: ${review.spoiler ? 'block' : 'none'}">스포일러가 포함된 내용입니다</p>
                        <button type="button" onclick="more(${review.id})" style="display: ${review.spoiler ? 'block' : 'none'};float:right;margin-top: 90px;border: none;border-radius:0.7em;padding: 6px;background-color: white;color: dimgrey">더보기</button>
                        ${userInfo !== undefined && review.nickname === userInfo.nickname ? `
                            <div id="editDelete" style="margin-top: 110px;">                       
                                <button type="button" id="edit-btn" onclick="modifyReview(reviews[${review.id}])" data-bs-toggle="modal" data-bs-target="#modifyModal" style="border: none; border-radius: 0.7em;padding-left: 10px; padding-right: 10px">수정</button>
                                <button type="button" onclick="fetchDelete(${review.id}, token)" style="border: none; border-radius: 0.7em;padding-left: 10px; padding-right: 10px">삭제</button>
                            </div>` : ''}
                    </div>
                    </div>
                    <div class="review-footer">
                        <hr/>
                        <div class="mb-3">
                            ${review.tags.map(tag => `<span class="bg-light p-2 rounded-2 me-2">#${tag.label}</span>`).join('')}
                        </div>
                        <div class="float-start">${dateString}</div>
                        <button class="btn btn-outline-danger float-end" onclick="fetchPostReviewLike(${review.id}, token)">
                            ${review.likesCount} <i class="fa-regular fa-heart"></i>
                        </button>
                    </div>
                </article>`
                );

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
            return result;
        })
}


function more(id) {
    const spoilerDisplay = document.getElementById('spoiler-' + id).style.display;
    if (spoilerDisplay === 'none') {
        document.getElementById('spoiler-' + id).style.display = "block";
        document.getElementById('spoilerText-' + id).style.display = "none";
        document.getElementById('spoiler-more-btn-' + id).innerText = "숨기기";
    } else {
        document.getElementById('spoiler-' + id).style.display = "none";
        document.getElementById('spoilerText-' + id).style.display = "block";
        document.getElementById('spoiler-more-btn-' + id).innerText = "더보기";
    }
}

//----------------------------------------------------------------------------------------------------------------------


function modifyReview({id, spoiler, body, professionalism, fun, readability, collectible, difficulty, tags}) {

    document.getElementById("spoiler-check").checked = spoiler === true;
    document.getElementById("body-input").value = body;
    document.getElementById("professionalism-value").value = professionalism;
    document.getElementById(`professinalism-score-${professionalism}`).checked = true;
    document.getElementById("fun-value").value = fun;
    document.getElementById(`fun-score-${fun}`).checked = true;
    document.getElementById("readability-value").value = readability;
    document.getElementById(`readability-score-${readability}`).checked = true;
    document.getElementById("collectible-value").value = collectible;
    document.getElementById(`collectible-score-${collectible}`).checked = true;
    document.getElementById("difficulty-value").value = difficulty;
    document.getElementById(`difficulty-score-${difficulty}`).checked = true;
    document.getElementById("modify-btn").setAttribute("onclick", `fetchReview(${id}, token)`)
    document.getElementById('tags-input').value = tags.map(tag => tag.label).join(';');
}

function fetchReview(id, token) {
    const spoiler = document.getElementById("spoiler-check").checked;
    const body = document.getElementById("body-input").value;
    const professionalism = document.getElementById("professionalism-value").value;
    const fun = document.getElementById("fun-value").value;
    const readability = document.getElementById("readability-value").value;
    const collectible = document.getElementById("collectible-value").value;
    const difficulty = document.getElementById("difficulty-value").value;
    var tags = document.getElementById('tags-input').value
        .replaceAll(' ', '')

    if (tags.lastIndexOf(';') === tags.length - 1) tags = tags.substring(0, tags.length - 1);

    tags = tags.split(';');

    const data = {
        spoiler,
        body,
        chart: {
            professionalism,
            fun,
            readability,
            collectible,
            difficulty
        },
        tags
    }

    console.log(token);
    console.log(data);


    fetch(`${BASE_URL}/api/v1/books/reviews/${id}`, {
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


//----------------------------------------------------------------------------------------------------------------------

function fetchDelete(id, token) {
    fetch(`${BASE_URL}/api/v1/books/reviews/${id}`, {
        method: 'DELETE',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        }
    }).then((response) => response.json())
        .then((response) => {
            const resultCode = response.resultCode;
            const errorCode = response.result.errorCode;
            if (resultCode === 'SUCCESS') {
                alert("삭제 완료");
                window.location.reload();
            } else if (resultCode === 'ERROR') {
                alert(response.result.message);
                if (errorCode === 'USER_NOT_LOGGED_IN' || errorCode === 'INVALID_TOKEN') window.location.href = '../users/login.html';
            } else {
                console.log(response);
            }
        })
}


function fetchPostReviewLike(id, token) {
    fetch(`${BASE_URL}/api/v1/books/reviews/${id}/likes`, {
        method: 'POST',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        },
    }).then((response) => response.json())
        .then((response) => {
            const resultCode = response.resultCode;
            const errorCode = response.result.errorCode;
            if (resultCode === 'SUCCESS') {
                alert(response.result);
                window.location.reload();
            } else if (resultCode === 'ERROR') {
                alert(response.result.message);
                if (errorCode === 'USER_NOT_LOGGED_IN' || errorCode === 'INVALID_TOKEN') window.location.href = '../users/login.html';
            } else {
                console.log(response);
            }
        })
}

function fetchPostReview(isbn) {
    const body = document.getElementById('body-input').value;
    const spoiler = document.getElementById('spoiler-check').checked;
    const professionalism = document.getElementById('professionalism-value').value;
    const fun = document.getElementById('fun-value').value;
    const readability = document.getElementById('readability-value').value;
    const collectible = document.getElementById('collectible-value').value;
    const difficulty = document.getElementById('difficulty-value').value;
    var tags = document.getElementById('tags-input').value
                    .replaceAll(' ', '')

    if (tags.lastIndexOf(';') === tags.length - 1) tags = tags.substring(0, tags.length - 1);

    tags = tags.split(';');

    const data = {
        body,
        spoiler,
        chart: {
            professionalism,
            fun,
            readability,
            collectible,
            difficulty
        },
        tags
    };

    console.log(isbn);
    console.log(data);

    fetch(`${BASE_URL}/api/v1/books/${isbn}/reviews`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": token ? "Bearer " + token : '',
        },
        body: JSON.stringify(data),
    }).then((response) => response.json())
        .then((response) => {
            const resultCode = response.resultCode;
            const errorCode = response.result.errorCode;
            if (resultCode === 'SUCCESS') {
                alert(response.result.message);
                window.location.href = `detail.html?isbn=${isbn}`;
            } else if (resultCode === 'ERROR') {
                alert(response.result.message);
                if (errorCode === 'USER_NOT_LOGGED_IN' || errorCode === 'INVALID_TOKEN') window.location.href = '../users/login.html';
            } else {
                console.log(response);
            }
        })
}
