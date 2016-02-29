'use strict';

angular.module('fruitcrmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('deliveryDay', {
                parent: 'entity',
                url: '/deliveryDays',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fruitcrmApp.deliveryDay.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/deliveryDay/deliveryDays.html',
                        controller: 'DeliveryDayController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('deliveryDay');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('deliveryDay.detail', {
                parent: 'entity',
                url: '/deliveryDay/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fruitcrmApp.deliveryDay.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/deliveryDay/deliveryDay-detail.html',
                        controller: 'DeliveryDayDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('deliveryDay');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DeliveryDay', function($stateParams, DeliveryDay) {
                        return DeliveryDay.get({id : $stateParams.id});
                    }]
                }
            })
            .state('deliveryDay.new', {
                parent: 'deliveryDay',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deliveryDay/deliveryDay-dialog.html',
                        controller: 'DeliveryDayDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    weekday: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('deliveryDay', null, { reload: true });
                    }, function() {
                        $state.go('deliveryDay');
                    })
                }]
            })
            .state('deliveryDay.edit', {
                parent: 'deliveryDay',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deliveryDay/deliveryDay-dialog.html',
                        controller: 'DeliveryDayDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DeliveryDay', function(DeliveryDay) {
                                return DeliveryDay.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('deliveryDay', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('deliveryDay.delete', {
                parent: 'deliveryDay',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deliveryDay/deliveryDay-delete-dialog.html',
                        controller: 'DeliveryDayDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['DeliveryDay', function(DeliveryDay) {
                                return DeliveryDay.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('deliveryDay', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
