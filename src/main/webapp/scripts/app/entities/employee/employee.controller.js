'use strict';

angular.module('fruitcrmApp')
    .controller('EmployeeController', function ($scope, $state, Employee, EmployeeSearch, ParseLinks) {

        $scope.employees = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Employee.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.employees = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            EmployeeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.employees = result;
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
            $scope.employee = {
                fName: null,
                lName: null,
                phone: null,
                email: null,
                id: null
            };
        };
    });
