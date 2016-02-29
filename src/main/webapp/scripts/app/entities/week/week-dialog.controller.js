'use strict';

angular.module('fruitcrmApp').controller('WeekDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Week', 'Orders',
        function($scope, $stateParams, $uibModalInstance, entity, Week, Orders) {

        $scope.week = entity;
        $scope.orderss = Orders.query();
        $scope.load = function(id) {
            Week.get({id : id}, function(result) {
                $scope.week = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('fruitcrmApp:weekUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.week.id != null) {
                Week.update($scope.week, onSaveSuccess, onSaveError);
            } else {
                Week.save($scope.week, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
