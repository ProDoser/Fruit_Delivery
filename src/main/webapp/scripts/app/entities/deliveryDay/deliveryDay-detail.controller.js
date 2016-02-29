'use strict';

angular.module('fruitcrmApp')
    .controller('DeliveryDayDetailController', function ($scope, $rootScope, $stateParams, entity, DeliveryDay, Orders) {
        $scope.deliveryDay = entity;
        $scope.load = function (id) {
            DeliveryDay.get({id: id}, function(result) {
                $scope.deliveryDay = result;
            });
        };
        var unsubscribe = $rootScope.$on('fruitcrmApp:deliveryDayUpdate', function(event, result) {
            $scope.deliveryDay = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
