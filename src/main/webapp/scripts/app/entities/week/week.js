'use strict';

angular.module('fruitcrmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('week', {
                parent: 'entity',
                url: '/weeks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fruitcrmApp.week.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/week/weeks.html',
                        controller: 'WeekController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('week');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('week.detail', {
                parent: 'entity',
                url: '/week/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fruitcrmApp.week.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/week/week-detail.html',
                        controller: 'WeekDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('week');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Week', function($stateParams, Week) {
                        return Week.get({id : $stateParams.id});
                    }]
                }
            })
            .state('week.new', {
                parent: 'week',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/week/week-dialog.html',
                        controller: 'WeekDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    week: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('week', null, { reload: true });
                    }, function() {
                        $state.go('week');
                    })
                }]
            })
            .state('week.edit', {
                parent: 'week',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/week/week-dialog.html',
                        controller: 'WeekDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Week', function(Week) {
                                return Week.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('week', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('week.delete', {
                parent: 'week',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/week/week-delete-dialog.html',
                        controller: 'WeekDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Week', function(Week) {
                                return Week.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('week', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
