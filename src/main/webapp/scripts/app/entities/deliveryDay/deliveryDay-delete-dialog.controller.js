'use strict';

angular.module('fruitcrmApp')
	.controller('DeliveryDayDeleteController', function($scope, $uibModalInstance, entity, DeliveryDay) {

        $scope.deliveryDay = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            DeliveryDay.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
