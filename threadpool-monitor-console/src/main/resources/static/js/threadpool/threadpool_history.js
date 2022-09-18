new Vue({
    el: "#app",
    data: function () {
        return {
            serviceNameList: [],
            matchServiceNameList: [],
            poolNameSet: [],
            addressSet: [],
            jobItemRespVOList: [],
            jobTagLst:[],
            reqArg: {
                queryDate: '',
                ipAndPort: '',
                poolName: '',
                jobId: '',
                applicationName: '',
                page: 1,
                pageSize: 10,
                tag: ''
            },
            totalSize:10
        }
    },
    mounted() {
        this.queryApplicationName();
    },

    methods: {
        //查询线程池执行记录
        queryApplicationName() {
            var _this = this;
            httpPost(getApplicationNameUrl(), {}).then(resp => {
                for (let i = 0; i < resp.length; i++) {
                    _this.serviceNameList.push(resp[i]);
                }
            });
        },

        //查询任务标签
        getJobTagList() {
            var _this = this;
            httpPost(getJobTagListUrl(),{
                "applicationName": _this.reqArg.applicationName,
                "poolName": _this.reqArg.poolName
            }).then(resp => {
                _this.jobTagLst = resp;
            });
        },

        //根据应用名称查询线程池信息
        getJobRecordByApplicationName() {
            var _this = this;
            httpPost(getJobRecordUrl(), {
                "applicationName": _this.reqArg.applicationName,
                "ipAndPort": _this.reqArg.ipAndPort,
                "poolName": _this.reqArg.poolName,
                "queryDate": _this.reqArg.queryDate,
                "jobId": _this.reqArg.jobId,
                "tag": _this.reqArg.tag,
                "page": _this.reqArg.page,
                "pageSize": _this.reqArg.pageSize
            }).then(resp => {
                _this.jobItemRespVOList = resp.jobItemRespVOList;
                _this.totalSize = resp.totalSize;
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
        },
        getAddress() {
            var _this = this;
            httpPost(getAddressUrl(), {
                "applicationName": this.reqArg.applicationName
            }).then(resp => {
                _this.poolNameSet = [];
                _this.addressSet = [];
                for (let i = 0; i < resp.poolNameSet.length; i++) {
                    _this.poolNameSet.push(resp.poolNameSet[i]);
                }
                console.log(resp);
                for (let i = 0; i < resp.addressSet.length; i++) {
                    _this.addressSet.push(resp.addressSet[i]);
                }
            });
        },
        changePage(val) {
            this.reqArg.page = val;
            this.getJobRecordByApplicationName();
        }
    }
});