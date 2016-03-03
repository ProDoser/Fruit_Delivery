'use strict';

angular.module('fruitcrmApp')
    .controller('CustomerDetailController', function ($scope, $rootScope, $stateParams, entity, Customer, Orders, OrdersSearch, ParseLinks) {
        $scope.customer = entity;
        $scope.load = function (id) {
            Customer.get({id: id}, function(result) {
                $scope.customer = result;



            });
        };

        // Added by Aleksandr //



        $scope.orderss = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Orders.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.orderss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


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


        /* ---------------- */


        var unsubscribe = $rootScope.$on('fruitcrmApp:customerUpdate', function(event, result) {
            $scope.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
