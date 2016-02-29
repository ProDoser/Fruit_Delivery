'use strict';

angular.module('fruitcrmApp')
	.controller('WeekDeleteController', function($scope, $uibModalInstance, entity, Week) {

        $scope.week = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Week.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
