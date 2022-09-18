new Vue({
    el: "#app",
    data: function () {
        return {
            serviceNameList: [],
            threadPoolRealTimeRespVO: {
                poolName: "",
                activePoolSize: 0,
                corePoolSize: 0,
                keepAliveTime: 0,
                maximumPoolSize: 0,
                queueCapacity: 0,
                rejectedExecutionType: "",
                taskCountScoreThreshold: "",
                corePoolLoad: 0,
                queueSizeLoad: 0,
                queueSize: 0
            },
            colors: [
                {color: '#3ff557', percentage: 20},
                {color: '#e6df63', percentage: 40},
                {color: '#ff7932', percentage: 60},
                {color: '#fa4c21', percentage: 80},
                {color: '#d31205', percentage: 100}
            ],
            matchServiceNameList: [],
            reqArg: {
                queryDate: '',
                ipAndPort: '',
                poolName: '',
                applicationName: '',
                page: 1,
                pageSize: 10
            },
            isRealTime: false,
            addressSet: [],
            poolNameSet: [],
            totalSize: 0,
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
            httpPost(getApplicationNameUrl(), {}).then(resp => {
                for (let i = 0; i < resp.length; i++) {
                    _this.serviceNameList.push(resp[i]);
                }
            });
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
        showActivePoolSizeChart(resp) {
            //展示activePoolSize的统计图
            var dom = document.getElementById('activePoolSize-app');
            var myChart = echarts.init(dom, null, {
                renderer: 'canvas',
                useDirtyRect: false
            });
            var option;
            let activePoolSizeArr = resp.activePoolSizeArr;
            let recordDateArr = resp.recordTimeArr;

            option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt) {
                        return [pt[0], '10%'];
                    }
                },
                title: {
                    left: 'center',
                    text: 'ActivePoolSize监控图'
                },
                toolbox: {
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: recordDateArr
                },
                yAxis: {
                    type: 'value',
                    boundaryGap: [0, '100%']
                },
                dataZoom: [
                    {
                        type: 'inside',
                        start: 0,
                        end: 100
                    },
                    {
                        start: 0,
                        end: 10
                    }
                ],
                series: [
                    {
                        name: 'ActivePoolSize值',
                        type: 'line',
                        symbol: 'none',
                        sampling: 'lttb',
                        itemStyle: {
                            color: 'rgb(255,92,20)'
                        },
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                {
                                    offset: 0,
                                    color: 'rgb(255,194,5)'
                                },
                                {
                                    offset: 1,
                                    color: 'rgb(255,131,30)'
                                }
                            ])
                        },
                        data: activePoolSizeArr
                    }
                ]
            };
            if (option && typeof option === 'object') {
                myChart.setOption(option);
            }
            window.addEventListener('resize', myChart.resize);
        },

        showQueueSizeChart(resp) {
            //展示activePoolSize的统计图
            var dom = document.getElementById('queueSize-app');
            var myChart = echarts.init(dom, null, {
                renderer: 'canvas',
                useDirtyRect: false
            });
            var option;
            let queueSizeArr = resp.queueSizeArr;
            let recordDateArr = resp.recordTimeArr;

            option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt) {
                        return [pt[0], '10%'];
                    }
                },
                title: {
                    left: 'center',
                    text: 'QueueSize监控图'
                },
                toolbox: {
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: recordDateArr
                },
                yAxis: {
                    type: 'value',
                    boundaryGap: [0, '100%']
                },
                dataZoom: [
                    {
                        type: 'inside',
                        start: 0,
                        end: 100
                    },
                    {
                        start: 0,
                        end: 10
                    }
                ],
                series: [
                    {
                        name: 'QueueSize值',
                        type: 'line',
                        symbol: 'none',
                        sampling: 'lttb',
                        itemStyle: {
                            color: 'rgb(113,102,255)'
                        },
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                {
                                    offset: 0,
                                    color: 'rgb(159,150,255)'
                                },
                                {
                                    offset: 1,
                                    color: 'rgb(181,180,255)'
                                }
                            ])
                        },
                        data: queueSizeArr
                    }
                ]
            };
            if (option && typeof option === 'object') {
                myChart.setOption(option);
            }
            window.addEventListener('resize', myChart.resize);
        },

        showTaskTimesChart(resp) {
            //展示activePoolSize的统计图
            var dom = document.getElementById('taskTimes-app');
            var myChart = echarts.init(dom, null, {
                renderer: 'canvas',
                useDirtyRect: false
            });
            var option;
            let perMinuteArr = resp.taskCountTimeArr;
            let perTaskTimeArr = resp.taskCountArr;

            option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt) {
                        return [pt[0], '10%'];
                    }
                },
                title: {
                    left: 'center',
                    text: '任务执行量监控图'
                },
                toolbox: {
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: perMinuteArr
                },
                yAxis: {
                    type: 'value',
                    boundaryGap: [0, '100%']
                },
                dataZoom: [
                    {
                        type: 'inside',
                        start: 0,
                        end: 100
                    },
                    {
                        start: 0,
                        end: 10
                    }
                ],
                series: [
                    {
                        name: '一分钟任务数',
                        type: 'line',
                        symbol: 'none',
                        sampling: 'lttb',
                        itemStyle: {
                            color: 'rgb(41,145,56)'
                        },
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                {
                                    offset: 0,
                                    color: 'rgb(75,133,67)'
                                },
                                {
                                    offset: 1,
                                    color: 'rgb(107,162,102)'
                                }
                            ])
                        },
                        data: perTaskTimeArr
                    }
                ]
            };
            if (option && typeof option === 'object') {
                myChart.setOption(option);
            }
            window.addEventListener('resize', myChart.resize);
        },

        showErrorTimesChart(resp) {
            //展示activePoolSize的统计图
            var dom = document.getElementById('errorTask-app');
            var myChart = echarts.init(dom, null, {
                renderer: 'canvas',
                useDirtyRect: false
            });
            var option;
            let perMinuteArr = resp.errorTaskTimeArr;
            let perTaskTimeArr = resp.errorTaskCountArr;

            option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt) {
                        return [pt[0], '10%'];
                    }
                },
                title: {
                    left: 'center',
                    text: '任务执行异常监控图'
                },
                toolbox: {
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: perMinuteArr
                },
                yAxis: {
                    type: 'value',
                    boundaryGap: [0, '100%']
                },
                dataZoom: [
                    {
                        type: 'inside',
                        start: 0,
                        end: 100
                    },
                    {
                        start: 0,
                        end: 10
                    }
                ],
                series: [
                    {
                        name: '15秒异常记录数',
                        type: 'line',
                        symbol: 'none',
                        sampling: 'lttb',
                        itemStyle: {
                            color: 'rgb(255,34,0)'
                        },
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                {
                                    offset: 0,
                                    color: 'rgb(255,76,49)'
                                },
                                {
                                    offset: 1,
                                    color: 'rgb(255,111,92)'
                                }
                            ])
                        },
                        data: perTaskTimeArr
                    }
                ]
            };
            if (option && typeof option === 'object') {
                myChart.setOption(option);
            }
            window.addEventListener('resize', myChart.resize);
        },

        showTagRecordTimesChart(resp) {
            //展示标签记录频率的统计图
            var dom = document.getElementById('tagRecord-app');
            var myChart = echarts.init(dom, null, {
                renderer: 'canvas',
                useDirtyRect: false
            });
            var option;
            let tagRecordTimeArr = resp.tagRecordTimeArr;
            let tagRecordCountArr = resp.tagRecordCountArr;

            option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt) {
                        return [pt[0], '10%'];
                    }
                },
                title: {
                    left: 'center',
                    text: '任务标签记录监控图'
                },
                toolbox: {
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: tagRecordTimeArr
                },
                yAxis: {
                    type: 'value',
                    boundaryGap: [0, '100%']
                },
                dataZoom: [
                    {
                        type: 'inside',
                        start: 0,
                        end: 100
                    },
                    {
                        start: 0,
                        end: 10
                    }
                ],
                series: [
                    {
                        name: '一分钟标签记录数',
                        type: 'line',
                        symbol: 'none',
                        sampling: 'lttb',
                        itemStyle: {
                            color: 'rgb(28,133,255)'
                        },
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                {
                                    offset: 0,
                                    color: 'rgb(100,168,255)'
                                },
                                {
                                    offset: 1,
                                    color: 'rgb(155,202,255)'
                                }
                            ])
                        },
                        data: tagRecordCountArr
                    }
                ]
            };
            if (option && typeof option === 'object') {
                myChart.setOption(option);
            }
            window.addEventListener('resize', myChart.resize);
        },

        //根据应用名称查询线程池信息填充统计图
        queryChartDataByApplicationName() {
            var _this = this;
            httpPost(getChartDataUrl(), {
                "applicationName": _this.reqArg.applicationName,
                "ipAndPort": _this.reqArg.ipAndPort,
                "poolName": _this.reqArg.poolName,
                "queryDate": _this.reqArg.queryDate
            }).then(resp => {
                _this.showActivePoolSizeChart(resp);
                _this.showQueueSizeChart(resp);
                _this.showTaskTimesChart(resp);
                _this.fillThreadPoolDetail(resp);
                _this.showErrorTimesChart(resp);
                _this.showTagRecordTimesChart(resp);
            });
            if (this.isRealTime == true) {
                return;
            }
            setInterval(function () {
                if (_this.isRealTime == false) {
                    return;
                }
                _this.queryChartDataByApplicationName();
            }, 1000);
        },

        fillThreadPoolDetail(resp) {
            this.threadPoolRealTimeRespVO.activePoolSize = resp.threadPoolRealTimeRespVO.activePoolSize;
            this.threadPoolRealTimeRespVO.keepAliveTime = resp.threadPoolRealTimeRespVO.keepAliveTime;
            this.threadPoolRealTimeRespVO.corePoolSize = resp.threadPoolRealTimeRespVO.corePoolSize;
            this.threadPoolRealTimeRespVO.maximumPoolSize = resp.threadPoolRealTimeRespVO.maximumPoolSize;
            this.threadPoolRealTimeRespVO.queueCapacity = resp.threadPoolRealTimeRespVO.queueCapacity;
            this.threadPoolRealTimeRespVO.rejectedExecutionType = resp.threadPoolRealTimeRespVO.rejectedExecutionType;
            this.threadPoolRealTimeRespVO.taskCountScoreThreshold = resp.threadPoolRealTimeRespVO.taskCountScoreThreshold;
            this.threadPoolRealTimeRespVO.corePoolLoad = resp.threadPoolRealTimeRespVO.corePoolLoad;
            this.threadPoolRealTimeRespVO.queueSizeLoad = resp.threadPoolRealTimeRespVO.queueSizeLoad;
            this.threadPoolRealTimeRespVO.queueSize = resp.threadPoolRealTimeRespVO.queueSize;
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