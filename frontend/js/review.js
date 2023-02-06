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

function fetchGetReviewsByBook(isbn) {
    fetch(`${BASE_URL}/api/v1/books/${isbn}/reviews`, {
        method: 'GET'
    }).then((response) => response.json())
    .then((res) => {
        console.log(res);
        const wrapper = document.querySelector(".bm-reviews-wrapper");
        const reviews = res.result.content;
        reviews.forEach(review => {
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
                    <canvas class="chart me-3" width="200px" height="200px" id="chart-${review.id}"></canvas>
                    <div style="width: 100%">
                        <h4>@${review.nickname}</h4>
                        <p class="spoiler" style="display: ${review.spoiler ? 'none' : 'block'}">
                            ${review.body}
                        </p>
                        <p class="spoilerText" style="display: ${review.spoiler ? 'block' : 'none'}">스포일러가 포함된 내용입니다</p>
                        <button type="button" onclick="more()" style="display: ${review.spoiler ? 'block' : 'none'};float:right;margin-top: 90px;border: none;border-radius:0.7em;padding: 6px;background-color: white;color: dimgrey">더보기</button>
                    </div>
                    </div>
                    <div class="review-footer">
                        <hr/>
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
    })
}


function more(){
    document.querySelector('.spoiler').style.display = "block";
    document.querySelector('.spoilerText').style.display = "none";
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
            if (errorCode === 'USER_NOT_LOGGED_IN' || errorCode === 'INVALID_TOKEN') window.location.href='../users/login.html';
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

    const data = {
        body,
        spoiler,
        chart : {
            professionalism,
            fun,
            readability,
            collectible,
            difficulty
        }
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
                window.location.href=`detail.html?isbn=${isbn}`;
            } else if (resultCode === 'ERROR') {
                alert(response.result.message);
                if (errorCode === 'USER_NOT_LOGGED_IN' || errorCode === 'INVALID_TOKEN') window.location.href='../users/login.html';
            } else {
                console.log(response);
            }
        })
}
