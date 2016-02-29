'use strict';

angular.module('fruitcrmApp').controller('DeliveryDayDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'DeliveryDay', 'Orders',
        function($scope, $stateParams, $uibModalInstance, entity, DeliveryDay, Orders) {

        $scope.deliveryDay = entity;
        $scope.orderss = Orders.query();
        $scope.load = function(id) {
            DeliveryDay.get({id : id}, function(result) {
                $scope.deliveryDay = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('fruitcrmApp:deliveryDayUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.deliveryDay.id != null) {
                DeliveryDay.update($scope.deliveryDay, onSaveSuccess, onSaveError);
            } else {
                DeliveryDay.save($scope.deliveryDay, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
