'use strict';

angular.module('fruitcrmApp')
    .factory('DeliveryDay', function ($resource, DateUtils) {
        return $resource('api/deliveryDays/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
