/**
 * Created by Doser on 03.03.2016.
 */
'use strict';

angular.module('fruitcrmApp')
    .factory('OrdersToday', function ($resource) {
        return $resource('api/today', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });

