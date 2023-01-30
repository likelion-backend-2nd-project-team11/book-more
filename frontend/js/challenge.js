const mainContainerEl = document.querySelector(".main-container");

let containerCount = 0;
function addProgressBar() {
    const thisCount = containerCount;
    const container = `<div class="bm-container">
        <div id="challenge">
          <h3 class="bm-challenge">challenge 등록</h3>
          <div>
          <div>
    <form>
      <div>
        <label for="title">제목</label>
        <input type="text" id="title" placeholder="제목을 입력하세요">
      </div>
      <div class="bm-description">
        <label for = "description">내용</label>
<!--        <input type="text" id="description" placeholder="내용을 입력하세요">-->
        <textarea id="description" placeholder="내용을 입력하세요"></textarea>
      </div>
       <div>
        <label for="deadline">기한</label>
        <input type="text"  id="deadline" placeholder="0000-00-00">
      </div>
      <div class="bm-button">
      <button type="button" role="button" id="btn-cancle" ">취소</a>
      <button type="button" onclick="fetchCreateChallenge(${thisCount})"  id="btn-save">등록</button>
      </div>
    </form>
<!--        <input hidden id="progess" value=0/>-->
<div class="bm-radio">
        <input
          type="radio"
          class="radio five"
          name="progress${thisCount}"
          value=0
          id="five${thisCount}"
          checked
        />
        <label for="five${thisCount}" class="label">0%</label>

        <input
          type="radio"
          class="radio twentyfive"
          name="progress${thisCount}"
          value=25
          id="twentyfive${thisCount}"
        />
        <label for="twentyfive${thisCount}" class="label">25%</label>

        <input
          type="radio"
          class="radio fifty"
          name="progress${thisCount}"
          value=50
          id="fifty${thisCount}"
        />
        <label for="fifty${thisCount}" class="label">50%</label>

        <input
          type="radio"
          class="radio seventyfive"
          name="progress${thisCount}"
          value=75
          id="seventyfive${thisCount}"
        />
        <label for="seventyfive${thisCount}" class="label">75%</label>

        <input
          type="radio"
          class="radio onehundred"
          name="progress${thisCount}"
          value= 100
          id="onehundred${thisCount}"
        />
        <label for="onehundred${thisCount}" class="label" onclick="bm()" id="particle"
          >100%</label
        >
</div>
        <div class="progress">
          <div class="progress-bar"></div>
        </div>
      </div>`;
    mainContainerEl.innerHTML += container;
    containerCount++;
}



const TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE2NzUwNjI1NDAsImV4cCI6MTY3NTA2NjE0MH0.fAcFwIRrSZmriGZUHno5ykhdzQqkOt_mSu6jnbnHSUA";

function fetchCreateChallenge(containerCounter) {
    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const deadline = document.getElementById("deadline").value;
    console.log(containerCounter);
    const progress = document.querySelector(`input[name="progress${containerCounter}"]:checked`).value;

    const data = {
        title,
        description,
        deadline,
        progress
    }

    console.log(data);

    // request api
    fetch('http://localhost:8080/api/v1/challenges', {
        method: 'POST', // *GET, POST, PUT, PATCH, DELETE 등
        headers: {
            'Content-Type': 'application/json',
            "Authorization": "Bearer " + TOKEN,
        },
        body: JSON.stringify(data),
    }).then((response) => response.json())
    .then((response) => {
        // success handler
        alert(response.result.message);
    }).catch((err) => {
        // error handler
        console.error(err);
    })


}



window.onload = function() {
    fetch("http://localhost:8080/api/v1/challenges", {
        method: 'GET', // *GET, POST, PUT, PATCH, DELETE 등
        headers: {
            "Authorization": "Bearer " + TOKEN,
        }
    }).then((response) => response.json())
        .then((res) => {
            console.log(res);
            const challenges = res.result.content;
            document.querySelector('.bm-wrapper').innerHTML= challenges.map(challenge => {
                return `
                
                    <div>
                    <div id="box-shadow">
                        <h4>제목:${challenge.title}</h4>
                        <h5>내용:${challenge.description}</h5>
                        <p>기한:${challenge.deadline}</p>
                        
                        <p>progress:${challenge.progress}</p>
                        <div class="progress">
  <div class="progress-bar" role="progressbar" aria-label="Example with label" style="width: ${challenge.progress}%;" aria-valuenow="${challenge.progress}" aria-valuemin="0" aria-valuemax="100">${challenge.progress}%</div>
                       
                    </div>
                        <div class="bm-btn">
                            <button type="button"  id="btn-edit">수정</button>
                            <button type="button"  id="btn-delete">삭제</button>
                        </div>
                        </div>
                    </div>`
            }).join("\n");
        })
}




// ReadableStream 사용 방법(Back 에서 Page 객체 return 한 결과) or back 코드 리턴 타입 고치기
// 챌린지 리스트 불러올때 로그인한 사용자 아이디 따와서 로그인한 사용자가 쓴 챌린지만 불러오기