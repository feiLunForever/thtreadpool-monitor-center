// var current_url = window.location.protocol+"//"+window.location.host;
var current_url = "http://localhost:8081/";

let getApplicationName = "threadPool/getApplicationNames";
let getThreadPoolList = "threadPool/getThreadPoolList";
let getChartData = "threadPool/getDetailRecord";
let getAddress = "threadPool/getAddress";
let getJobRecord = "threadPool/getJobRecord";
let getJobTagList = "threadPool/getJobTagList";
let getAlarmInfo = "threadPool/getAlarmInfo";

function getJobTagListUrl() {
    return current_url + getJobTagList;
}
function getJobRecordUrl() {
    return current_url + getJobRecord;
}
function getApplicationNameUrl() {
    return current_url + getApplicationName;
}

function getAlarmInfoUrl() {
    return current_url + getAlarmInfo;
}
function getThreadPoolListUrl() {
    return current_url + getThreadPoolList;
}
function getChartDataUrl() {
    return current_url + getChartData;
}
function getAddressUrl() {
    return current_url + getAddress;
}

