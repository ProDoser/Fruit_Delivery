'use strict';

angular.module('fruitcrmApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


