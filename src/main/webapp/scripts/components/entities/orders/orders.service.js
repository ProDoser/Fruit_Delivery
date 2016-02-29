'use strict';

angular.module('fruitcrmApp')
    .factory('Orders', function ($resource, DateUtils) {
        return $resource('api/orderss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.orderDate = DateUtils.convertLocaleDateFromServer(data.orderDate);
                    data.firstDelivery = DateUtils.convertLocaleDateFromServer(data.firstDelivery);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.orderDate = DateUtils.convertLocaleDateToServer(data.orderDate);
                    data.firstDelivery = DateUtils.convertLocaleDateToServer(data.firstDelivery);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.orderDate = DateUtils.convertLocaleDateToServer(data.orderDate);
                    data.firstDelivery = DateUtils.convertLocaleDateToServer(data.firstDelivery);
                    return angular.toJson(data);
                }
            }
        });
    });
