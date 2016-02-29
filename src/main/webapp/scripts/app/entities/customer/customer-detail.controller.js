'use strict';

angular.module('fruitcrmApp')
    .controller('CustomerDetailController', function ($scope, $rootScope, $stateParams, entity, Customer, Orders) {
        $scope.customer = entity;
        $scope.load = function (id) {
            Customer.get({id: id}, function(result) {
                $scope.customer = result;
            });
        };
        var unsubscribe = $rootScope.$on('fruitcrmApp:customerUpdate', function(event, result) {
            $scope.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
