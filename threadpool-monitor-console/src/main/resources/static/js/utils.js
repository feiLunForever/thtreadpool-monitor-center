function addCookie(name, value, days, path) {
    /**添加设置cookie**/
    var name = escape(name);
    var value = escape(value);
    var expires = new Date();
    expires.setTime(expires.getTime() + days * 3600000 * 24);
    //path=/，表示cookie能在整个网站下使用，path=/temp，表示cookie只能在temp目录下使用
    path = path == "" ? "" : ";path=" + path;
    //GMT(Greenwich Mean Time)是格林尼治平时，现在的标准时间，协调世界时是UTC
    //参数days只能是数字型
    var _expires = (typeof days) == "string" ? "" : ";expires=" + expires.toUTCString();
    document.cookie = name + "=" + value + _expires + path;
}

function deleteCookie(name, path) {
    /**根据cookie的键，删除cookie，其实就是设置其失效**/
    var name = escape(name);
    var expires = new Date(0);
    path = path == "" ? "" : ";path=" + path;
    document.cookie = name + "=" + ";expires=" + expires.toUTCString() + path;
}

function logout() {
    deleteCookie("ztscrip", "/");
    window.location.href = '../common/admin_login.html';
}


function request(url, params, options) {
    if (options == 'get') {
        axios.get(url,params).then(function (response) {
            if(response.data.code==200){
                return response.data.data;
            } else if(response.data.code==403){
                console.log('无权限请求');
            }
        })
    } else if (options == 'post') {
        axios.post(url,params).then(function (response) {
            if(response.data.code==200){
                return response.data.data;
            } else if(response.data.code==403){
                console.log('无权限请求');
            }
        })
    }
}

function httpPost(url, params) {
    console.log(params);
    let result = axios.post(url,params).then(function (response) {
        if(response.data.code==200){
            return response.data.data;
        } else if(response.data.code==403){
                window.location.href = '../common/admin_login.html';
                return null;
        }
    });
    return result;
}

function httpGet(url, params) {
    let result = axios.get(url,params).then(function (response) {
        if(response.data.code==200){
            return response.data.data;
        } else if(response.data.code==403){
            window.location.href = '../common/admin_login.html';
            return null;
        }
    });
    return result;
}
