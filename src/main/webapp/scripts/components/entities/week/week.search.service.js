'use strict';

angular.module('fruitcrmApp')
    .factory('WeekSearch', function ($resource) {
        return $resource('api/_search/weeks/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
