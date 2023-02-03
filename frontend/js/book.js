function search({query, page, size, atHome}) {
    if (query.length < 1) {
        alert('검색어를 확인해주세요. [최소 한 글자]');
        return;
    }
    if (atHome) { // 홈페이지에서 검색하는 경우
        window.location.href=`books/search.html?query=${query}&page=${page || 1}&size=${size || 20}`;
        return;
    }
    window.location.href=`search.html?query=${query}&page=${page || 1}&size=${size || 20}`;
}

function submitQueryHandler(e, atHome) {
    let key = e.key || e.keyCode;

    if (key === 'Enter' || key === 13) {
        search({query:e.target.value, atHome});
    }
}

function getQuery() {
    return document.querySelector('.search-box input').value;
}

function setQueryInputVal(value) {
    document.querySelector('.search-box input').value = value;
}

function fetchSearchBooks(query, page, size) {
    if (query === '' || query === undefined) return;
    fetch(`${BASE_URL}/api/v1/books?query=${query}&page=${page}&size=${size}`)
        .then(res => {
            if (res.ok) return res.json();
            else alert(res.json().result.message);
        }).then(data => {
            console.log(data)
            const books = data.result.content;
            const contentWrapper = document.querySelector('.books-shelf');
            contentWrapper.innerHTML = books.map(book => {
                return `
                    <a class="p-3 text-decoration-none text-black" href="detail.html?isbn=${book.isbn}">
                        <div class="book-item bg-white col h-100 p-4 rounded-4 shadow bm-scale-animation">
                            <img class="mx-auto mb-3 w-100 d-block shadow" src="${book.image}"/>
                            <div class="text-center fw-bold text-wrap">${book.title}</div>
                        </div>
                    </a>`;
            }).join('\n');
    })
}

function fetchSearchBookDetail(isbn) {
    if (isbn === '' || isbn === undefined) return;
    fetch(`${BASE_URL}/api/v1/books/${isbn}`)
        .then(res => {
            if (res.ok) return res.json();
            else alert(res.json().result.message);
        }).then(data => {
        console.log(data)
        const book = data.result;
        const contentWrapper = document.querySelector('.contents-wrapper');
        contentWrapper.innerHTML = `
                <div class="container contents p-5 shadow bg-white">
                    <div class="book-detail d-flex">
                        <img class="me-5 shadow" height="240px" src="${book.image}"/>
                        <div class="info">
                            <h2>${book.title}</h2>
                            <p>가격 : ${book.price || '-'}원</p>
                            <p>페이지 : ${book.pages || '-'}</p>
                            <p>저자 : ${book.authors || '-'}</p>
                            <p>번역 : ${book.translators || '-'}</p>
                            <p>출판사 : ${book.publisher || '-'}</p>
                        </div>
                    </div>
                    <hr/>
                    <div>
                        <h4>책소개</h4>
                        <textarea class="w-100 border-0 mb-3" readonly rows="12">${book.introduce || '-'}</textarea>
                        <h4>목차</h4>
                        <p class="desc">
                            ${book.chapter || '-'}
                        </p>
                    </div>
                </div>`;
    })
}