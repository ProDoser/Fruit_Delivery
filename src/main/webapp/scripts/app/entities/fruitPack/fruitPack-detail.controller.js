'use strict';

angular.module('fruitcrmApp')
    .controller('FruitPackDetailController', function ($scope, $rootScope, $stateParams, entity, FruitPack, Orders) {
        $scope.fruitPack = entity;
        $scope.load = function (id) {
            FruitPack.get({id: id}, function(result) {
                $scope.fruitPack = result;
            });
        };
        var unsubscribe = $rootScope.$on('fruitcrmApp:fruitPackUpdate', function(event, result) {
            $scope.fruitPack = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
