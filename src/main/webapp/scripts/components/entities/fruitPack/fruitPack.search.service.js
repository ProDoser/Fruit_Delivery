'use strict';

angular.module('fruitcrmApp')
    .factory('FruitPackSearch', function ($resource) {
        return $resource('api/_search/fruitPacks/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
