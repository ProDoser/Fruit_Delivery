'use strict';

angular.module('fruitcrmApp')
    .controller('FruitPackDetailController', function ($scope, $rootScope, $stateParams, entity, FruitPack, Orders, OrdersSearch, ParseLinks) {
        $scope.fruitPack = entity;
        $scope.load = function (id) {
            FruitPack.get({id: id}, function(result) {
                $scope.fruitPack = result;
            });
        };

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




        var unsubscribe = $rootScope.$on('fruitcrmApp:fruitPackUpdate', function(event, result) {
            $scope.fruitPack = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
