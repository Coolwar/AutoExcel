angular.module("myApp", [])
    .controller("myController", ["$scope", "$http", function ($scope, $http) {

        $scope.errorJosn = {"errorMsg": "服务器异常，请稍后再试"};
        $scope.dbo = {1: "name", 2: "id_card", 3: "mobiles", 4: "sources", 5: "categories", 6: "others"}
        $scope.mapDbo = {};

        $scope.selected = '';

        $scope.model = [{
            categories: '网贷'
        }, {
            categories: '法院'
        }, {
            categories: '银行'
        }];

        $scope.excel1 = false;
        $scope.excel2 = false;
        $scope.uploadStatus = 1;
        $scope.mongoStatus = 1;
        $scope.saveStatus = 1;

        //保存数据
        $scope.saveExcel2Mongo = function () {
            $scope.saveStatus = 0;
            $http.post("/service/save/saveExcel2Mongo.do")
                .success(function (resData) {
                    $scope.saveStatus = 1;
                    $scope.saveFlagMsg = resData;
                }).error(function () {
                    $scope.saveStatus = 1;
                    $scope.saveFlagMsg = $scope.errorJosn;
                })
        };

        //查看格式化mongo的数据
        $scope.lookExcel = function () {
            $scope.mongoStatus = 0;
            var mapExcel = {};

            if ($scope.selected != "" && $scope.selected != null) {
                mapExcel.categories = $scope.selected.categories;
            }
            if ($scope.dboname != undefined) {
                mapExcel.name_idx = $scope.dboname;
            }
            if ($scope.dboid_card != undefined) {
                mapExcel.id_card_idx = $scope.dboid_card;
            }
            if ($scope.dbomobiles != undefined) {
                mapExcel.mobiles_idx = $scope.dbomobiles;
            }
            if ($scope.dbosources != undefined) {
                mapExcel.sources = $scope.dbosources;
            }
            if ($scope.dbodebt != undefined) {
                mapExcel.debt_idx = $scope.dbodebt;
            }
            if ($scope.dboclear != undefined) {
                mapExcel.clear_idx = $scope.dboclear;
            }

            $http.post("/service/get/lookExcel.do", mapExcel)
                .success(function (resData) {
                    $scope.mongoStatus = 1;
                    $scope.mongoDate = resData.excelData;
                    $scope.successMsg = resData.successMsg;
                    $scope.excel1 = false;
                    $scope.excel2 = true;
                }).error(function () {
                    $scope.mongoStatus = 1;
                    $scope.mongoDate = $scope.errorJosn;
                })
        };

        //jsonEditor查看器
        $scope.loadJsonEditor = function () {

            var container = document.getElementById("jsoneditor");
            $scope.editor = new JSONEditor(container);
            // set json
            $scope.editor.set($scope.mongoDate);
            $scope.editor.setMode("form")
            // get json
            $scope.json = $scope.editor.get();
        };

        //加载excel原始数据
        $scope.loadFile = function () {
            $scope.uploadStatus = 0;
            $http.get("/service/get/getExcel.do")
                .success(function (resData) {
                    $scope.uploadStatus = 1;
                    $scope.reportData = resData.excelData;
                    $scope.successMsg = resData.successMsg;

                    if (resData.excelMap != null) {
                        if (resData.excelMap.name != null)
                            $scope.dboname = resData.excelMap.name;
                        if (resData.excelMap.mobiles != null)
                            $scope.dbomobiles = resData.excelMap.mobiles;
                        if (resData.excelMap.id_card != null)
                            $scope.dboid_card = resData.excelMap.id_card;
                        if (resData.excelMap.debt != null)
                            $scope.dbodebt = resData.excelMap.debt;
                    }
                    $scope.excel1 = true;
                    $scope.excel2 = false;
                }).error(function () {
                    $scope.uploadStatus = 1;
                    $scope.reportData = $scope.errorJosn;
                })
        };

    }]);