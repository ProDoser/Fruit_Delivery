'use strict';

angular.module('fruitcrmApp').controller('FruitPackDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FruitPack', 'Orders',
        function($scope, $stateParams, $uibModalInstance, entity, FruitPack, Orders) {

        $scope.fruitPack = entity;
        $scope.orderss = Orders.query();
        $scope.load = function(id) {
            FruitPack.get({id : id}, function(result) {
                $scope.fruitPack = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('fruitcrmApp:fruitPackUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.fruitPack.id != null) {
                FruitPack.update($scope.fruitPack, onSaveSuccess, onSaveError);
            } else {
                FruitPack.save($scope.fruitPack, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
