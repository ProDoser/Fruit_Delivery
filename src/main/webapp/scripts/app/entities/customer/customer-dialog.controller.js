'use strict';

angular.module('fruitcrmApp').controller('CustomerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Customer', 'Orders',
        function($scope, $stateParams, $uibModalInstance, entity, Customer, Orders) {

        $scope.customer = entity;
        $scope.orderss = Orders.query();
        $scope.load = function(id) {
            Customer.get({id : id}, function(result) {
                $scope.customer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('fruitcrmApp:customerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.customer.id != null) {
                Customer.update($scope.customer, onSaveSuccess, onSaveError);
            } else {
                Customer.save($scope.customer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
