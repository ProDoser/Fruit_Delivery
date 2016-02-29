'use strict';

angular.module('fruitcrmApp').controller('OrdersDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Orders', 'Customer', 'FruitPack', 'Week', 'DeliveryDay',
        function($scope, $stateParams, $uibModalInstance, entity, Orders, Customer, FruitPack, Week, DeliveryDay) {

        $scope.orders = entity;
        $scope.customers = Customer.query();
        $scope.fruitpacks = FruitPack.query();
        $scope.weeks = Week.query();
        $scope.deliverydays = DeliveryDay.query();
        $scope.load = function(id) {
            Orders.get({id : id}, function(result) {
                $scope.orders = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('fruitcrmApp:ordersUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.orders.id != null) {
                Orders.update($scope.orders, onSaveSuccess, onSaveError);
            } else {
                Orders.save($scope.orders, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForOrderDate = {};

        $scope.datePickerForOrderDate.status = {
            opened: false
        };

        $scope.datePickerForOrderDateOpen = function($event) {
            $scope.datePickerForOrderDate.status.opened = true;
        };
        $scope.datePickerForFirstDelivery = {};

        $scope.datePickerForFirstDelivery.status = {
            opened: false
        };

        $scope.datePickerForFirstDeliveryOpen = function($event) {
            $scope.datePickerForFirstDelivery.status.opened = true;
        };
}]);
