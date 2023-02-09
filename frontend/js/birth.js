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