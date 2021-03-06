/**
 * Created by Doser on 03.03.2016.
 */
'use strict';

angular.module('fruitcrmApp')
    .controller('OrdersTodayController', function ($scope, $state, OrdersToday, OrdersSearch, Employee, EmployeeSearch, ParseLinks) {

        $scope.orderss = [];
        $scope.employees = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            OrdersToday.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.orderss = result;
            });
            Employee.query(function(result) {
                $scope.employees = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            OrdersSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.orderss = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.orders = {
                details: null,
                orderDate: null,
                firstDelivery: null,
                isActive: false,
                id: null
            };
        };


        $scope.status = "danger";

        $scope.statuses = [{
            value: 'danger',
            label: 'Not ready',
            required: null
        }, {
            value: 'warning',
            label: 'Packed',
            required: null
        }, {
            value: 'info',
            label: 'Being delivered',
            required: '!employee'
        }, {
            value: 'default',
            label: 'Delivered',
            required: '!employee'
        }];

        /*

        $scope.dangerCounter = 0;
        $scope.warningCounter = 0;
        $scope.infoCounter = 0;
        $scope.defaultCounter = 0;
        $scope.test = 0;
        $scope.stats = function (){
            var log = [];

            angular.forEach($scope.statuses, function(value) {
                if (value.value == 'danger')
                    $scope.dangerCounter++;
                else
                    $scope.dangerCounter--;
            }, log)

        }
*/



        $scope.status2 = null;

        $scope.notReady = 0;





    });
