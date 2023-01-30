function getToken() {
    const token = window.localStorage.getItem("token");
    return token || undefined;
}

function verifyToken() {
    const token = getToken();
    return token !== undefined;
}

function deleteToken() {
    window.localStorage.removeItem("token");
    window.location.reload();
}