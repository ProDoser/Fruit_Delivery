'use strict';

angular.module('fruitcrmApp')
    .controller('FruitPackController', function ($scope, $state, FruitPack, FruitPackSearch, ParseLinks) {

        $scope.fruitPacks = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            FruitPack.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.fruitPacks = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            FruitPackSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.fruitPacks = result;
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
            $scope.fruitPack = {
                name: null,
                description: null,
                price: null,
                id: null
            };
        };
    });
