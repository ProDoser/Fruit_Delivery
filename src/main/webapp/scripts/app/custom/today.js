/**
 * Created by Doser on 01.03.2016.
 */

'use strict';

angular.module('fruitcrmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('today', {
                parent: 'entity',
                url: '/today',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fruitcrmApp.orders.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/custom/today.html',
                        controller: 'OrdersTodayController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('today');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

    });
