'use strict';

angular.module('fruitcrmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('fruitPack', {
                parent: 'entity',
                url: '/fruitPacks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fruitcrmApp.fruitPack.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fruitPack/fruitPacks.html',
                        controller: 'FruitPackController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('fruitPack');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('fruitPack.detail', {
                parent: 'entity',
                url: '/fruitPack/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'fruitcrmApp.fruitPack.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fruitPack/fruitPack-detail.html',
                        controller: 'FruitPackDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('fruitPack');
                        $translatePartialLoader.addPart('orders');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FruitPack', function($stateParams, FruitPack) {
                        return FruitPack.get({id : $stateParams.id});
                    }]
                }
            })
            .state('fruitPack.new', {
                parent: 'fruitPack',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/fruitPack/fruitPack-dialog.html',
                        controller: 'FruitPackDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    price: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('fruitPack', null, { reload: true });
                    }, function() {
                        $state.go('fruitPack');
                    })
                }]
            })
            .state('fruitPack.edit', {
                parent: 'fruitPack',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/fruitPack/fruitPack-dialog.html',
                        controller: 'FruitPackDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FruitPack', function(FruitPack) {
                                return FruitPack.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fruitPack', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('fruitPack.delete', {
                parent: 'fruitPack',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/fruitPack/fruitPack-delete-dialog.html',
                        controller: 'FruitPackDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FruitPack', function(FruitPack) {
                                return FruitPack.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fruitPack', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
