const urlSearchParams = new URLSearchParams(location.search);
const input = document.querySelector('.search-box input');
const query = urlSearchParams.get('query');
const page = urlSearchParams.get('page');
const size = urlSearchParams.get('size');
const BASE_URL = 'http://localhost:8080';

function search(query, page, size) {
    if (query.length > 1) window.location.href=`./books.html?query=${query}&page=${page | 1}&size=${size | 20}`;
    else alert('검색어를 확인해주세요. [최소 두 글자]');
}
function getQuery() {
    return input.value;
}

function setQueryInputVal(value) {
    input.value = value;
}

function fetchSearchBooks(query, page, size) {
    if (query === '' || query === undefined) return;
    fetch(`${BASE_URL}/api/v1/books?query=${query}&page=1&size=20`)
        .then(res => {
            if (res.ok) return res.json();
            else alert(res.json()['result']['message']);
        }).then(data => {
            console.log(data);
    })
}