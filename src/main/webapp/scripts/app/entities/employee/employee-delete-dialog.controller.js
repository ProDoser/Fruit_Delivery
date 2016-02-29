'use strict';

angular.module('fruitcrmApp')
	.controller('EmployeeDeleteController', function($scope, $uibModalInstance, entity, Employee) {

        $scope.employee = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Employee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
