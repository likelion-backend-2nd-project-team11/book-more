function challengeSelectInit() {
    let today = new Date();
    let today_year = today.getFullYear();
    let start_year =today_year + 10;// 시작할 년도
    console.log(start_year);
    let index = 0;
    for (let y = today_year; y <= start_year; y++) { //start_year ~ 현재 년도
        document.getElementById('select_year').options[index] = new Option(y, y);
        index++;
    }
    index = 0;
    for (let m = 1; m <= 12; m++) {
        document.getElementById('select_month').options[index] = new Option(m, m);
        index++;
    }
}

function lastday(){ //년과 월에 따라 마지막 일 구하기
    var Year=document.getElementById('select_year').value;
    var Month=document.getElementById('select_month').value;
    var day=new Date(new Date(Year,Month,1)-86400000).getDate();
    /* = new Date(new Date(Year,Month,0)).getDate(); */

    var dayindex_len=document.getElementById('select_day').length;
    if(day>dayindex_len){
        for(var i=(dayindex_len+1); i<=day; i++){
            document.getElementById('select_day').options[i-1] = new Option(i, i);
        }
    }
    else if(day<dayindex_len){
        for(var i=dayindex_len; i>=day; i--){
            document.getElementById('select_day').options[i]=null;
        }
    }
}

function setMydeadline(deadline){ //년과 월에 따라 마지막 일 구하기
    let tempdeadline = deadline.split('-');
    editDeadlineArr[0] = tempdeadline[0];
    editDeadlineArr[1] = tempdeadline[1];
    editDeadlineArr[2] = tempdeadline[2];

    let Year = parseInt(tempdeadline[0], 10);
    let Month = parseInt(tempdeadline[1], 10);
    let Day = parseInt(tempdeadline[2], 10);
    let date=new Date(new Date(Year,Month,1)-86400000).getDate();
    /* = new Date(new Date(Year,Month,0)).getDate(); */

    const selectYear = document.getElementById('editSelect_year');
    const selectMonth = document.getElementById('editSelect_month');
    const selectDay = document.getElementById('editSelect_day');

    let dayindex_len=selectDay.length;
    if(date>dayindex_len){
        for(let i=(dayindex_len+1); i<=date; i++){
            selectDay.options[i-1] = new Option(i, i);
        }
    }
    else if(date<dayindex_len){
        for(let i=dayindex_len; i>=date; i--){
            document.getElementById('editSelect_day').options[i]=null;
        }
    }

    for (let i = 0; i <selectYear.length; i++) {
        if (selectYear.options[i].value === tempdeadline[0]) selectYear.options[i].selected = true;
    }

    for (let i = 0; i <selectMonth.length; i++) {
        if (selectMonth.options[i].value === Month.toString()) selectMonth.options[i].selected = true;
    }

    for (let i = 0; i < selectDay.length; i++) {
        if (selectDay.options[i].value === Day.toString()) selectDay.options[i].selected = true;
    }
}


let deadlineArr = [new Date().getFullYear(), "01", "01"];

let year = "";
const showYear = (target) => {
    year = target.value;
    deadlineArr[0] = year;
}

let month = "";
const showMonth = (target) => {
    month = target.value;
    if (month.length === 1) {
        month = "0" + month;
    }
    deadlineArr[1] = month;
}

let day = "";
const showDay = (target) => {
    day = target.value;
    if (day.length === 1) {
        day = "0" + day;
    }
    deadlineArr[2] = day;
}
//---------------------------------------------------------------------------------------------------
function challengeSelectInit2() {
    let today = new Date();
    let today_year = today.getFullYear();
    let start_year =today_year + 10;// 시작할 년도
    let index = 0;
    for (let y = today_year; y <= start_year; y++) { //start_year ~ 현재 년도
        document.getElementById('editSelect_year').options[index] = new Option(y, y);
        index++;
    }
    index = 0;
    for (let m = 1; m <= 12; m++) {
        document.getElementById('editSelect_month').options[index] = new Option(m, m);
        index++;
    }
}

function lastdayEdit(){ //년과 월에 따라 마지막 일 구하기
    var Year=document.getElementById('editSelect_year').value;
    var Month=document.getElementById('editSelect_month').value;
    var day=new Date(new Date(Year,Month,1)-86400000).getDate();
    /* = new Date(new Date(Year,Month,0)).getDate(); */

    var dayindex_len=document.getElementById('editSelect_day').length;
    if(day>dayindex_len){
        for(var i=(dayindex_len+1); i<=day; i++){
            document.getElementById('editSelect_day').options[i-1] = new Option(i, i);
        }
    }
    else if(day<dayindex_len){
        for(var i=dayindex_len; i>=day; i--){
            document.getElementById('editSelect_day').options[i]=null;
        }
    }
}

let editDeadlineArr = [new Date().getFullYear(), "01", "01"];

let editYear = "";
const editShowYear = (target) => {
    editYear = target.value;
    editDeadlineArr[0] = editYear;
}

let editMonth = "";
const editShowMonth = (target) => {
    editMonth = target.value;
    if (editMonth.length === 1) {
        editMonth = "0" + editMonth;
    }
    editDeadlineArr[1] = editMonth;
}

let editDay = "";
const editShowDay = (target) => {
    editDay = target.value;
    if (editDay.length === 1) {
        editDay = "0" + editDay;
    }
    editDeadlineArr[2] = editDay;
}