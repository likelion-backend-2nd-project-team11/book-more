function getToken() {
    const token = window.localStorage.getItem("token");
    return token || undefined;
}

function fetchVerifyToken(token) {
    return fetch(`${BASE_URL}/api/v1/users/verify`, {
        method: 'POST',
        headers: {
            "Authorization": token ? "Bearer " + token : '',
        },
    }).then(response => response.json())
    .then(response => {
        if (response.resultCode !== 'SUCCESS') deleteToken();
        const id = response.result.id;
        const email = response.result.email;
        const nickname = response.result.nickname;
        return {id, email, nickname};
    });
}

function deleteToken() {
    window.localStorage.removeItem("token");
    window.location.reload();
}