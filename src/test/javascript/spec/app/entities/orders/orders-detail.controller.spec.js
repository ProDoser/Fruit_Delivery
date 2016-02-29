'use strict';

describe('Controller Tests', function() {

    describe('Orders Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrders, MockCustomer, MockFruitPack, MockWeek, MockDeliveryDay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrders = jasmine.createSpy('MockOrders');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockFruitPack = jasmine.createSpy('MockFruitPack');
            MockWeek = jasmine.createSpy('MockWeek');
            MockDeliveryDay = jasmine.createSpy('MockDeliveryDay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Orders': MockOrders,
                'Customer': MockCustomer,
                'FruitPack': MockFruitPack,
                'Week': MockWeek,
                'DeliveryDay': MockDeliveryDay
            };
            createController = function() {
                $injector.get('$controller')("OrdersDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'fruitcrmApp:ordersUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
