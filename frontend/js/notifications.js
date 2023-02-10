function fetchGetAlarms(token) {
    fetch(`${BASE_URL}/api/v1/alarms`, {
        method: 'GET',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        }
    }).then((response) => response.json())
        .then(getResponse())
}

function fetchGetNewAlarms(token) {
    fetch(`${BASE_URL}/api/v1/alarms/new`, {
        method: 'GET',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        }
    }).then((response) => response.json())
        .then(getResponse())
}

function getResponse() {
    return (res) => {
        console.log(res);
        const wrapper = document.querySelector(".bm-alarms-wrapper");
        const alarms = res.result.content;
        wrapper.innerHTML = alarms.map(alarm => {
            const alarmId = alarm.id;
            const alarmType = alarm.alarmType;
            const confirmed = alarm.confirmed;
            const fromUserId = alarm.fromUserId;
            const fromUserNickname = `<strong>${alarm.fromUserNickname}</strong>`;


            let alarmMessage = "";
            let source = "";
            let isbn = "";

            if (alarmType === "NEW_FOLLOW_REVIEW") {
                alarmMessage = fromUserNickname + " 님이 새로운 리뷰를 등록했어요.";
                source = alarm.source.title
                isbn = alarm.source.isbn;
            } else if (alarmType === "NEW_FOLLOW") {
                alarmMessage = fromUserNickname + " 님이 나를 팔로우했어요.";
                source = "팔로우 리스트 보기"
            } else {
                alarmMessage = fromUserNickname + " 님이 나의 리뷰에 좋아요를 눌렀어요.";
                source = alarm.source.title;
                isbn = alarm.source.isbn;
            }

            const origin = new Date(alarm.createdDatetime);
            const year = origin.getFullYear();
            const month = origin.getMonth();
            const date = origin.getDate();
            const hour = origin.getHours();
            const minute = origin.getMinutes();
            const second = origin.getSeconds();
            const utcDate = new Date(Date.UTC(year, month, date, hour, minute, second));
            const dateString = utcDate.toLocaleString();

            if (confirmed === true) {
                return `
                        <button type="button" onclick="locateDetail(${alarmId}, '${alarmType}', ${fromUserId}, ${isbn})" class="list-group-item list-group-item-action" style="background: #f5f5f5;color: #bdbdbd">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1 me-5">${alarmMessage}</h5>
                                <small class="float-end" style="color: #bdbdbd">${dateString}</small>
                            </div>
                            <p class="mb-1">${source}</p>
                        </button>`;
            } else {
                return `
                        <button type="button" onclick="locateDetail(${alarmId}, '${alarmType}', ${fromUserId}, ${isbn})" class="list-group-item list-group-item-action">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1 me-5">${alarmMessage}</h5>
                                <small class="text-muted float-end">${dateString}</small>
                            </div>
                            <p class="mb-1">${source}</p>
                        </button>`;
            }
        }).join('\n');
    }
}

function locateDetail(alarmId, alarmType, fromUserId, isbn) {

    confirmNotification(alarmId);

    if (alarmType === "NEW_FOLLOW") {
        window.location.href = "../users/detail.html?id=" + fromUserId;
    } else {
        window.location.href = "../books/detail.html?isbn=" + isbn;
    }
}

function confirmNotification(id) {
    fetch(`${BASE_URL}/api/v1/alarms/${id}/confirm`, {
        method: 'POST',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        }
    }).then((response) => response.json())
}

function fetchGetNewAlarmsCount(token) {
    fetch(`${BASE_URL}/api/v1/alarms/new`, {
        method: 'GET',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        }
    }).then((response) => response.json())
        .then((res) => {
            console.log(res);
            const wrapper = document.querySelector(".bm-alarmsCount-wrapper");
            const count = res.result.totalElements;

            wrapper.innerText = count
        })
}

function fetchCheckNewAlarms(token) {
    if (!token) return;
    fetch(`${BASE_URL}/api/v1/alarms/new`, {
        method: 'GET',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        }
    }).then((response) => response.json())
        .then((res) => {
            console.log(res);
            const wrapper = document.querySelector(".bm-checkAlarms-wrapper");
            const count = res.result.totalElements;

            if (count !== 0) {
                wrapper.innerHTML = `<span class="position-absolute top-n10 start-75 translate-middle p-2 my-1 bg-danger rounded-circle"></span>`;
            }
        })
}