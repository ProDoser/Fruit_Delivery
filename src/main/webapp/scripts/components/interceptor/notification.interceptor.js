 'use strict';

angular.module('fruitcrmApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-fruitcrmApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-fruitcrmApp-params')});
                }
                return response;
            }
        };
    });
