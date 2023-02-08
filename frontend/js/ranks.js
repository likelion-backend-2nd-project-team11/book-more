function fetchGetRanks() {

    const result = fetch(`${BASE_URL}/api/v1/users/ranks`, {
        method: 'GET'

    }).then(res => {
        if (res.ok) return res.json();
        else alert(res.json().result.message);
    }).then((data) => {
        console.log(data)
        const ranks = data.result;
        const result = {};
        console.log(ranks)
        ranks.forEach(item => result[item.id] = item);
        console.log(result)
        const contentWrapper = document.querySelector('.bm-rank-wrapper');
        contentWrapper.innerHTML = ranks.map(rank => {
            return `
                
                       <tr>
                        <td>
                            <small class="d-block text-center">${rank.ranking}위</small>
                        </td>
                        <td>
                            <small class="d-block text-center">${rank.nickName}</small>
                        </td>
                        <td>
                            <small class="d-block text-center">${rank.point}</small>
                        </td>
                        </tr>
              
            `
        }).join("\n");
        return result;
    })
    return result;
}

function fetchGetMyRanks() {

    const result = fetch(`${BASE_URL}/api/v1/users/ranks/my`, {
        method: 'GET',
        headers: {
            "Authorization": "Bearer " + token,
        }
    }).then(res => {
        if (res.ok) return res.json();
        else alert(res.json().result.message);
    }).then((data) => {
        console.log(data)
        const ranks = data.result;
        console.log(ranks)
        console.log(result)
        const contentWrapper = document.querySelector('.bm-my-rank-wrapper');
        contentWrapper.innerHTML =
            `<table class="bm-my-rank table table-striped mb-0 ">     
                <thead>           
                    <tr>
                        <th scope="col" class="text-center col-2">내 순위</th>
                        <th scope="col" class="text-center col-4">내 닉네임</th>
                        <th scope="col" class="text-center col-4">내 포인트</th>
                    </tr>
                </thead>    
                    <tr>
                        <td>
                            <small class="d-block text-center">${ranks.ranking}위</small>
                        </td>
                        <td>
                            <small class="d-block text-center">${ranks.nickName}</small>
                        </td>
                        <td>
                            <small class="d-block text-center">${ranks.point}</small>
                        </td>
                    </tr>
            </table> `

    })
}
