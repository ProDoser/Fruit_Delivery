'use strict';

angular.module('fruitcrmApp')
    .factory('CustomerSearch', function ($resource) {
        return $resource('api/_search/customers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
