'use strict';

angular.module('fruitcrmApp')
    .controller('WeekDetailController', function ($scope, $rootScope, $stateParams, entity, Week, Orders) {
        $scope.week = entity;
        $scope.load = function (id) {
            Week.get({id: id}, function(result) {
                $scope.week = result;
            });
        };
        var unsubscribe = $rootScope.$on('fruitcrmApp:weekUpdate', function(event, result) {
            $scope.week = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
