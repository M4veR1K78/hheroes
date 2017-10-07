"use strict";

var heroesApp = angular.module('heroesApp', ['ui.bootstrap', 'datatables', 'datatables.bootstrap']);

// services
heroesApp.service('FilleService', FilleService)
	.service('HeroesService', HeroesService)
	.service('ShopService', ShopService);

// controllers
heroesApp.controller('IndexController', IndexController)
	.controller('ModalLoginController', ModalLoginController)
	.controller('ModalAvatarController', ModalAvatarController);

// constants
heroesApp.constant('conf', {
	HHEROES_URL: "https://www.hentaiheroes.com"
});

FilleService.$inject = ['$http'];

function FilleService($http) {
	var service = {};
	var api = "filles"
	
	service.getAll = getAll;
	service.collectSalary = collectSalary;
	
	function getAll() {
		return $http.get(api);
	}
	
	function collectSalary() {
		return $http.post(api + "/collectAll");
	}
	
	return service;
}

HeroesService.$inject = ['$http'];

function HeroesService($http) {
	var service = {};
	
	service.login = login;
	service.isAuthenticated = isAuthenticated;
	
	function login(user) {
		return $http.post('login', user);
	}
	
	function isAuthenticated() {
		return $http.get('auth');
	}
	
	return service;
}

ShopService.$inject = ['$http'];

function ShopService($http) {
	var service = {};
	
	service.getAvailableGifts = getAvailableGifts;
	
	function getAvailableGifts(user) {
		return $http.get('shop/gifts');
	}
	
	return service;
}

IndexController.$inject = ['$uibModal', 'FilleService', 'HeroesService', 'ShopService', 'conf'];

/**
 * Controller de la liste des filles.
 * 
 * @param FilleService
 * @param conf
 * @returns
 */
function IndexController($uibModal, FilleService, HeroesService, ShopService, conf) {
	var vm = this;

	var Status = {
		FORBIDDEN: 403	
	};
	
	var StatusLevel = {
		MAX: "Max.",
		UPGRADE: "En upgrade"
	}
	
	vm.filles = [];
	vm.cadeaux = [];
	vm.hhUrl = conf.HHEROES_URL;
	vm.openModalAvatar = openModalAvatar;
	vm.getBestOnTypeGirl = getBestOnTypeGirl;
	vm.getCloseEvolveGirl = getCloseEvolveGirl;
	vm.getWeakestGirl = getWeakestGirl;
	vm.collectSalary = collectSalary;

	activate();
	
	function activate() {
		HeroesService.isAuthenticated().then(function(response) {
			if (response.data) {
				getFilles();				
			} else {
				login();
			}
		}, function(response) {
			console.debug('Impossible de savoir si on est connecté', response);
		});
	}
	
	function getFilles() {
		FilleService.getAll().then(function(response) {
			vm.filles = response.data;
		});
		ShopService.getAvailableGifts().then(function(response) {
			vm.cadeaux = response.data;
		});
		
	}
	
	function login() {
		var modalInstance = $uibModal.open({
			animation : true,
			templateUrl : 'pages/templates/login.html',
			controller : 'ModalLoginController',
			controllerAs : 'vm'
		});
		
		modalInstance.result.then(function() {
			getFilles();
	    }, function () {
	    	console.log('User is not connected, no data to display');
	    });
	}
	
	function openModalAvatar(fille) {
		var modalInstance = $uibModal.open({
			animation : true,
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'pages/templates/avatars.html',
			controller : 'ModalAvatarController',
			controllerAs : 'vm',
			size: 'lg',
			resolve : {
				girl : function() {
					return fille;
				}
			}
		});
	}
	
	function getFloatValue(expLeft) {
		var value = expLeft;
		
		if (angular.isString(value)) {
			var value = value.replace(/\s+/g, '');
		}
		
		if (value === StatusLevel.MAX) value = 99999.9;
		if (value === StatusLevel.UPGRADE) value = 0.0;
		
		return parseFloat(value);
	}
	
	/**
	 * Récupère la fille la plus simple à évoluer.
	 */
	function getCloseEvolveGirl() {
		var girl = { affLeftNextLevel: 99999 };
		
		angular.forEach(vm.filles, function(fille) {
			if (getFloatValue(fille.affLeftNextLevel) < getFloatValue(girl.affLeftNextLevel)) {
				girl = fille;
			}
		});
		
		return girl;
	}
	
	/**
	 * Récupère la fille ayant le plus bas niveau
	 */
	function getWeakestGirl() {
		var girl = { level: 99999 };
		
		angular.forEach(vm.filles, function(fille) {
			if (fille.level < girl.level) {
				girl = fille;
			}
		});
		
		return girl;
	}
	
	/**
	 * Récupère la meilleure fille dans son domaine (= type).
	 */
	function getBestOnTypeGirl(type) {
		var girl = {};
		girl[type] = 0;
		
		angular.forEach(vm.filles, function(fille) {
			if (fille[type] > girl[type]) {
				girl = fille;
			}
		});
		
		return girl;
	}
	
	function collectSalary() {
		console.log(2);
		return FilleService.collectSalary();
	}
}

ModalLoginController.$inject = ['$uibModalInstance', 'HeroesService'];

/**
 * Modal de connexion à HHeroes.
 * 
 * @param $uibModalInstance
 * @param HeroesService
 * @returns
 */
function ModalLoginController($uibModalInstance, HeroesService) {
	var vm = this;
	vm.user = {};
		
	vm.login = login;
	vm.close = close;
	
	function login() {
		HeroesService.login(vm.user).then(function() {
			$uibModalInstance.close(true);
		}, function(response) {
			vm.errorMessage = response.data.message;
		});
	}
	
	function close() {
		$uibModalInstance.dismiss('cancel');
	}
}

ModalAvatarController.$inject = ['$uibModalInstance', 'girl'];

/**
 * Modal permettant de voir les avatars des filles.
 * 
 * @param $uibModalInstance
 * @param girl
 * @returns
 */
function ModalAvatarController($uibModalInstance, girl) {
	var vm = this;

	vm.girl = girl;
	vm.grades = [];
	
	for (var i = 0; i <= girl.grade; i++) {
		vm.grades.push(i);
	}
	
	vm.close = close;
	
	function close() {
		$uibModalInstance.dismiss('cancel');
	}
}

// components

heroesApp.component('girlEvolveWidget', {
	templateUrl: 'pages/templates/girlEvolveWidget.html',
	controller: function GirlEvolveWidgetController() { var vm = this;	},
	controllerAs: 'vm',
	bindings: {
		fille: '<'
	}
});

heroesApp.component('girlWeakestWidget', {
	templateUrl: 'pages/templates/girlWeakestWidget.html',
	controller: function GirlEvolveWidgetController() { var vm = this;	},
	controllerAs: 'vm',
	bindings: {
		fille: '<'
	}
});

heroesApp.component('girlBestWidget', {
	templateUrl: 'pages/templates/girlBestWidget.html',
	controller: function GirlBestWidgetController() { 
		var vm = this; 
		
		vm.getNumber = getNumber;
		
		function getNumber(num) {
			return new Array(num); 
		}
	},
	controllerAs: 'vm',
	bindings: {
		fille: '<',
		type: '<'
	}
});

heroesApp.component('girlSalaryWidget', {
	templateUrl: 'pages/templates/girlSalaryWidget.html',
	controller: GirlSalaryWidgetController,
	controllerAs: 'vm',
	bindings: {
		filles: '<',
		onCollect: '&'
	}
});

function GirlSalaryWidgetController() { 
	var vm = this;
	
	vm.totalSalary = 0;
	vm.totalSalaryPerHour = 0;
	vm.collectableSalary = 0;
	vm.collect = collect;
	
	vm.$onInit = function() {
		angular.forEach(vm.filles, function(fille) {
			vm.totalSalary += fille.salary;
			vm.totalSalaryPerHour += fille.salaryPerHour;
			vm.collectableSalary += (fille.collectable) ? fille.salary : 0;
		});
	}
	
	function collect() {
		vm.onCollect().then(function(response) {
			vm.collectableSalary = 0;				
		}, function(response) {
			console.debug(response);
		});
	}
}

heroesApp.component('giftSumWidget', {
	templateUrl: 'pages/templates/giftSumWidget.html',
	controller: GiftSumWidgetController,
	controllerAs: 'vm',
	bindings: {
		cadeaux: '<',
	}
});

function GiftSumWidgetController() {
	var vm = this;
	vm.totalPrix = 0;
	vm.totalAff = 0;
	
	vm.$onInit = function() {
		angular.forEach(vm.cadeaux, function(cadeau) {
			vm.totalPrix += cadeau.prix;
			vm.totalAff += cadeau.affectation;
		});
	}
}
