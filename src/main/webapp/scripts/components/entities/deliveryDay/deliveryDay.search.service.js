'use strict';

angular.module('fruitcrmApp')
    .factory('DeliveryDaySearch', function ($resource) {
        return $resource('api/_search/deliveryDays/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
