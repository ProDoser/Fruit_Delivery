'use strict';

angular.module('fruitcrmApp')
    .controller('DeliveryDayController', function ($scope, $state, DeliveryDay, DeliveryDaySearch) {

        $scope.deliveryDays = [];
        $scope.loadAll = function() {
            DeliveryDay.query(function(result) {
               $scope.deliveryDays = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            DeliveryDaySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.deliveryDays = result;
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
            $scope.deliveryDay = {
                name: null,
                weekday: null,
                id: null
            };
        };
    });
