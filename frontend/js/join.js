/**
 *  1. 회원가입 시 비밀번호 더블 체크 에러
 *  2. 이메일 중복 에러
 *  3. 닉네임 중복 에러
 *  4. null 값 에러
 */

let birthArr = [new Date().getFullYear(), "01", "01"];

let year = "";
const showYear = (target) => {
    year = target.value;
    birthArr[0] = year;
}

let month = "";
const showMonth = (target) => {
    month = target.value;
    if (month.length === 1) {
        month = "0" + month;
    }
    birthArr[1] = month;
}

let day = "";
const showDay = (target) => {
    day = target.value;
    if (day.length === 1) {
        day = "0" + day;
    }
    birthArr[2] = day;
}

function join() {

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const nickname = document.getElementById('name').value;
    const birth = birthArr.join("-");

    // request api
    fetch(`${BASE_URL}/api/v1/users/join`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email, password, nickname, birth,
        }),
    }).then((response) => response.json())
    .then((response) => {
        console.log(response);
        if (response.resultCode === 'SUCCESS') {
            alert("회원가입 완료")
            window.location.href = "login.html";
        } else if (response.resultCode === 'ERROR') {
            alert(response.result.message);
        }
    });
}

let today = new Date();
let today_year= today.getFullYear();
let start_year=today_year-100;// 시작할 년도
let index=0;
for(let y=today_year; y>=start_year; y--){ // 현재 년도 ~ start_year
    document.getElementById('select_year').options[index] = new Option(y, y);
    index++;
}
index=0;
for(let m=1; m<=12; m++){
    document.getElementById('select_month').options[index] = new Option(m, m);
    index++;
}

lastday();

function lastday(){ //년과 월에 따라 마지막 일 구하기
    let Year=document.getElementById('select_year').value;
    let Month=document.getElementById('select_month').value;
    let day=new Date(new Date(Year,Month,1)-86400000).getDate();
    /* = new Date(new Date(Year,Month,0)).getDate(); */

    let dayindex_len=document.getElementById('select_day').length;
    if(day>dayindex_len){
        for(let i=(dayindex_len+1); i<=day; i++){
            document.getElementById('select_day').options[i-1] = new Option(i, i);
        }
    }
    else if(day<dayindex_len){
        for(let i=dayindex_len; i>=day; i--){
            document.getElementById('select_day').options[i]=null;
        }
    }
}