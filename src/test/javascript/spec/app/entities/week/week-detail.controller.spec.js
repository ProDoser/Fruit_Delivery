'use strict';

describe('Controller Tests', function() {

    describe('Week Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWeek, MockOrders;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWeek = jasmine.createSpy('MockWeek');
            MockOrders = jasmine.createSpy('MockOrders');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Week': MockWeek,
                'Orders': MockOrders
            };
            createController = function() {
                $injector.get('$controller')("WeekDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fruitcrmApp:weekUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
