'use strict';

angular.module('fruitcrmApp')
	.controller('FruitPackDeleteController', function($scope, $uibModalInstance, entity, FruitPack) {

        $scope.fruitPack = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FruitPack.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
