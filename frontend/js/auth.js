function getToken() {
    const token = window.localStorage.getItem("token");
    return token || undefined;
}

function fetchVerifyToken(token) {
    fetch(`${BASE_URL}/api/v1/users/verify`, {
        method: 'POST',
        headers: {
            "Authorization": "Bearer " + token,
        },
    }).then(response => response.json())
    .then(response => {
        if (response.resultCode !== 'SUCCESS') deleteToken();
    });
}

function deleteToken() {
    window.localStorage.removeItem("token");
    window.location.reload();
}