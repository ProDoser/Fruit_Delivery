'use strict';

angular.module('fruitcrmApp')
    .controller('OrdersDetailController', function ($scope, $rootScope, $stateParams, entity, Orders, Customer, FruitPack, Week, DeliveryDay) {
        $scope.orders = entity;
        $scope.load = function (id) {
            Orders.get({id: id}, function(result) {
                $scope.orders = result;
            });
        };
        var unsubscribe = $rootScope.$on('fruitcrmApp:ordersUpdate', function(event, result) {
            $scope.orders = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
