function getToken() {
    const token = window.localStorage.getItem("token");
    return token || undefined;
}

function verifyToken() {
    let token = getToken();
    let isLoggedIn = false;
    if (token === undefined || token === '') return {token, isLoggedIn};
    isLoggedIn = true;
    return {token, isLoggedIn};
}

function deleteToken() {
    window.localStorage.removeItem("token");
    window.location.reload();
}