'use strict';

describe('Controller Tests', function() {

    describe('FruitPack Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFruitPack, MockOrders;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFruitPack = jasmine.createSpy('MockFruitPack');
            MockOrders = jasmine.createSpy('MockOrders');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FruitPack': MockFruitPack,
                'Orders': MockOrders
            };
            createController = function() {
                $injector.get('$controller')("FruitPackDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fruitcrmApp:fruitPackUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
