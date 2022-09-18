new Vue({
    el: "#app",
    data: function () {
        return {
            serviceNameList: [],
            matchServiceNameList: [],
            reqArg: {
                applicationName: '',
                page:1,
                pageSize:10
            },
            totalSize:0,
            threadPoolDetailInfo: []
        }
    },
    mounted() {
        this.queryApplicationName();
    },

    methods: {
        //查询线程池执行记录
        queryApplicationName() {
            var _this = this;
            httpPost(getApplicationNameUrl(),{}).then(resp=>{
                for (let i = 0; i < resp.length; i++) {
                    _this.serviceNameList.push(resp[i]);
                }
            });
        },

        //根据应用名称查询线程池信息
        queryByApplicationName() {
            var _this = this;
            console.log(this.reqArg.applicationName);
            _this.threadPoolDetailInfo = [];
            httpPost(getThreadPoolListUrl(),{
                "applicationName": _this.reqArg.applicationName
            }).then(resp=>{
                for(let i=0;i<resp.length;i++) {
                    _this.threadPoolDetailInfo.push(resp[i]);
                }
            });
        },

        /**
         * service的模糊搜索
         */
        serviceFilter(val) {
            //判断是否为空
            if (val) {
                //同时筛选Lable与value的值
                this.matchServiceNameList = this.serviceNameList.filter((itemContent) => {
                    var item = itemContent;
                    if (!!~item.indexOf(val) || !!~item.toUpperCase().indexOf(val.toUpperCase()) || !!~item.indexOf(val) || !!~item.toUpperCase().indexOf(val.toUpperCase())) {
                        return true;
                    }
                });
                this.reqArg.serviceName = val;
            } else {
                //赋值还原
                this.matchServiceNameList = this.serviceNameList;
            }
        }
    }
});