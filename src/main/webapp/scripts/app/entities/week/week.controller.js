'use strict';

angular.module('fruitcrmApp')
    .controller('WeekController', function ($scope, $state, Week, WeekSearch) {

        $scope.weeks = [];
        $scope.loadAll = function() {
            Week.query(function(result) {
               $scope.weeks = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            WeekSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.weeks = result;
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
            $scope.week = {
                name: null,
                week: null,
                id: null
            };
        };
    });
