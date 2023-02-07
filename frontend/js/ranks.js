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
