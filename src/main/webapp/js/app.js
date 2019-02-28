'use strict';

var heroesApp = angular.module('heroesApp', ['ui.bootstrap', 'datatables', 'datatables.bootstrap', 'ui-notification']);

// services
heroesApp.service('FilleService', FilleService)
	.service('GameService', GameService)
	.service('ShopService', ShopService)
	.service('HeroService', HeroService)
	.service('BossService', BossService)
	.service('ActivityService', ActivityService)
	.service('UserService', UserService)
	.service('UtilService', UtilService)
	.service('EntityService', EntityService);

// controllers
heroesApp.controller('IndexController', IndexController)
	.controller('ModalLoginController', ModalLoginController)
	.controller('ModalAvatarController', ModalAvatarController)
	.controller('ModalUserController', ModalUserController);

// filters
heroesApp.filter('secondsToDateTime', secondsToDateTime)
	.filter('ordinal', ordinal);

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
	service.doCollectSalaries = doCollectSalaries;
	service.getRarityLibelle = getRarityLibelle;
	service.getRarityClass = getRarityClass;
	
	function getAll() {
		return $http.get(api);
	}
	
	function collectSalary() {
		return $http.post(api + "/collectAll");
	}
	
	function doCollectSalaries() {
		return $http.post(api + '/doCollectAll');
	}
	
	/**
	 * Récupère le libellé de la rareté d'une fille.
	 */
	function getRarityLibelle(filleRarity) {
		if (filleRarity === 'DEPART') {
			return 'Départ';
		} else if (filleRarity === 'COMMUN') {
			return 'Commune';
		} else if (filleRarity === 'RARE') {
			return 'Rare';
		} else if (filleRarity === 'EPIQUE') {
			return 'Épique';
		} else if (filleRarity === 'LEGENDAIRE') {
			return 'Légendaire';
		}
		
		return null;
	}
	
	/**
	 * Récupère la classe d'une fille en fonction de sa rareté.
	 */
	function getRarityClass(filleRarity) {
		var rarity = {};
		if (filleRarity === 'DEPART') {
			rarity.starting = true;
		} else if (filleRarity === 'COMMUN') {
			rarity.common = true;
		} else if (filleRarity === 'RARE') {
			rarity.rare = true;
		} else if (filleRarity === 'EPIQUE') {
			rarity.epic = true;
		} else if (filleRarity === 'LEGENDAIRE') {
			rarity.legendary = true;
		}
		
		return rarity;
	}
	
	return service;
}

GameService.$inject = ['$http'];

function GameService($http) {
	var service = {};
	
	service.login = login;
	service.logout = logout;
	service.isAuthenticated = isAuthenticated;
	
	function login(user) {
		return $http.post('login', user);
	}
	
	function isAuthenticated() {
		return $http.get('auth');
	}
	
	function logout() {
		return $http.get('logout');
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

HeroService.$inject = ['$http'];

function HeroService($http) {
	var service = {};
	
	service.getHero = getHero;
	service.updateStat = updateStat;
	
	function getHero(user) {
		return $http.get('hero');
	}
	
	function updateStat(carac) {
		return $http.post('hero/update?carac=' + carac);
	}
	
	return service;
}

BossService.$inject = ['$http'];

function BossService($http) {
	var service = {};
	
	service.getAll = getAll;
	service.destroy = destroy;
	
	function getAll() {
		return $http.get('boss/all');
	}
	
	function destroy(id) {
		return $http.post('boss/' + id + '/destroy');
	}
	
	return service;
}

ActivityService.$inject = ['$http'];

function ActivityService($http) {
	var service = {};
	
	service.getAll = getAll;
	service.start = start;
	service.claimRewards = claimRewards;
	
	function getAll() {
		return $http.get('activity/all');
	}
	
	function start() {
		return $http.post('activity/start');
	}
	
	function claimRewards() {
		return $http.post('activity/claimRewards');
	}
	
	return service;
}

UserService.$inject = ['$http'];

function UserService($http) {
	var service = {};
	
	service.getMe = getMe;
	service.update = update;
	
	function getMe() {
		return $http.get('user/me');
	}
	
	function update(user) {
		return $http.put('user', user);
	}
	
	return service;
}

function UtilService() {
	var service = {};
	
	service.getDTTLangues = getDTTLangues;
	
	function getDTTLangues() {
		return { 
			search: 'Rechercher',
			decimal:        '',
		    emptyTable:     'Aucune donnée disponible',
		    info:           '_START_ à _END_ sur _TOTAL_ entrées',
		    infoEmpty:      '0 à 0 sur 0 entrée',
		    infoFiltered:   '(filtré à partir d\'un total de _MAX_ entrées)',
		    infoPostFix:    '',
		    thousands:      ',',
		    lengthMenu:     'Afficher _MENU_ entrées',
		    loadingRecords: 'Chargement...',
		    processing:     'En cours de calcul...',
		    zeroRecords:    'Aucune donnée trouvée',
		    paginate: {
		        'first':      'Première',
		        'last':       'Dernière',
		        'next':       'Suivante',
		        'previous':   'Précédente'
		    },
		    aria: {
		        'sortAscending':  ': activate to sort column ascending',
		        'sortDescending': ': activate to sort column descending'
		    }
		};
	}
	
	return service;
}

EntityService.$inject = ['GameService', 'FilleService', 'BossService', 'ShopService', 'HeroService', 'ActivityService', 'UtilService', 'UserService'];

function EntityService(GameService, FilleService, BossService, ShopService, HeroService, ActivityService, UtilService, UserService) {
	var service = {
		gameSrv: GameService,
		filleSrv: FilleService,
		bossSrv: BossService,
		shopSrv: ShopService,
		heroSrv: HeroService,
		activitySrv: ActivityService,
		utilSrv: UtilService,
		userSrv: UserService
	}
	
	return service;
}


function secondsToDateTime() {
    return function(seconds) {
        var d = new Date(0,0,0,0,0,0,0);
        d.setSeconds(seconds);
        return d;
    };
}

function ordinal() {
	return function(input) {
		return input + (input == 1 ? 'ère' : 'ème');
	}
};

IndexController.$inject = ['$q', '$uibModal', 'EntityService', 'conf', 'Notification'];

/**
 * Controller de la liste des filles.
 * 
 * @param FilleService
 * @param conf
 * @returns
 */
function IndexController($q, $uibModal, EntityService, conf, Notification) {
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
	vm.bosses = [];
	vm.bossSelected = "1";
	vm.hhUrl = conf.HHEROES_URL;
	vm.types = {
		hardcore: { libelle: 'Hardcore', field: 'hardcore', id: 1},
		charme: { libelle: 'Charme', field: 'charme', id: 2},
		savoirFaire: { libelle: 'Savoir-faire', field: 'savoirFaire', id: 3}
	}
	
	// méthodes
	vm.openModalAvatar = openModalAvatar;
	vm.getBestOnTypeGirl = getBestOnTypeGirl;
	vm.getCloseEvolveGirl = getCloseEvolveGirl;
	vm.getWeakestGirl = getWeakestGirl;
	vm.collectSalary = collectSalary;
	vm.destroyBoss = destroyBoss;
	vm.getMissions = getMissions;
	vm.doCollectSalaries = doCollectSalaries;
	vm.logout = logout;

	activate();
	
	function activate() {
		EntityService.gameSrv.isAuthenticated().then(function(response) {
			if (response.data) {
				getFilles();				
			} else {
				showLoginModal();
			}
		}, function(response) {
			console.debug('Impossible de savoir si on est connecté', response);
		});
	}
	
	function getFilles() {
		EntityService.filleSrv.getAll().then(function(response) {
			vm.filles = response.data;
			vm.nextGirlToEvolve = vm.getCloseEvolveGirl();
		});
		EntityService.shopSrv.getAvailableGifts().then(function(response) {
			vm.cadeaux = response.data;
		});
		EntityService.heroSrv.getHero().then(function(response) {
			vm.hero = response.data;
		});
		EntityService.bossSrv.getAll().then(function(response) {
			vm.bosses = response.data;
			if (vm.bosses.length) {
				vm.bossSelected = vm.bosses[0].id;
			}
		});
		EntityService.userSrv.getMe().then(function(response) {
			vm.user = response.data;
		});
	}
	
	function getMissions() {
		var deferred = $q.defer();
		
		EntityService.activitySrv.getAll().then(function(response) {
			deferred.resolve(response.data);
		});
		
		return deferred.promise;
	}
	
	function showLoginModal() {
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
	
	function logout() {
		EntityService.gameSrv.logout().then(function() {
			vm.filles = [];
			vm.cadeaux = [];
			vm.bosses = [];
			vm.hero = {};
			vm.user = {};
			showLoginModal();
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
		var girl = { name: 'Aucune', affLeftNextLevel: 99999, affProgress: 0 };
		
		angular.forEach(vm.filles, function(fille) {
			if (!fille.maxed && getFloatValue(fille.affLeftNextLevel) < getFloatValue(girl.affLeftNextLevel)) {
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
	
	function collectSalary(collectedSalary) {
		var deferred = $q.defer();
		EntityService.filleSrv.collectSalary().then(function() {
			vm.hero.money += collectedSalary;
			deferred.resolve();
		}, function() {
			deferred.reject();
		});
		
		return deferred.promise;
	}	
	
	var rewards= ["<div class=\"slot common\" rarity=\"common\" id_item=\"25\" subtype=\"0\" id_m_i=\"Array\" data-d=\"{&quot;id_item&quot;:&quot;25&quot;,&quot;type&quot;:&quot;gift&quot;,&quot;subtype&quot;:&quot;0&quot;,&quot;identifier&quot;:&quot;K4&quot;,&quot;rarity&quot;:&quot;common&quot;,&quot;value&quot;:&quot;25&quot;,&quot;carac1&quot;:0,&quot;carac2&quot;:0,&quot;carac3&quot;:0,&quot;endurance&quot;:0,&quot;chance&quot;:0,&quot;ego&quot;:0,&quot;damage&quot;:0,&quot;duration&quot;:0,&quot;level_mini&quot;:&quot;1&quot;,&quot;name&quot;:&quot;Lingerie&quot;,&quot;Name&quot;:&quot;Lingerie&quot;,&quot;ico&quot;:&quot;https:\\/\\/hh.hh-content.com\\/pictures\\/items\\/K4.png&quot;,&quot;price_sell&quot;:1738,&quot;count&quot;:1,&quot;id_m_i&quot;:[]}\">\n\n\t\n\t<div id=\"item_25\" class=\"stack_num\" style=\"display:none\"><span>1</span></div>\n\t\n\t\t<img src=\"https://hh.hh-content.com/pictures/items/K4.png\" />\n\t<i><i>i</i></i>\n</div>","<div class=\"slot common\" rarity=\"common\" id_item=\"19\" subtype=\"0\" id_m_i=\"Array\" data-d=\"{&quot;id_item&quot;:&quot;19&quot;,&quot;type&quot;:&quot;gift&quot;,&quot;subtype&quot;:&quot;0&quot;,&quot;identifier&quot;:&quot;K2&quot;,&quot;rarity&quot;:&quot;common&quot;,&quot;value&quot;:&quot;7&quot;,&quot;carac1&quot;:0,&quot;carac2&quot;:0,&quot;carac3&quot;:0,&quot;endurance&quot;:0,&quot;chance&quot;:0,&quot;ego&quot;:0,&quot;damage&quot;:0,&quot;duration&quot;:0,&quot;level_mini&quot;:&quot;1&quot;,&quot;name&quot;:&quot;Chocolats&quot;,&quot;Name&quot;:&quot;Chocolats&quot;,&quot;ico&quot;:&quot;https:\\/\\/hh.hh-content.com\\/pictures\\/items\\/K2.png&quot;,&quot;price_sell&quot;:487,&quot;count&quot;:1,&quot;id_m_i&quot;:[]}\">\n\n\t\n\t<div id=\"item_19\" class=\"stack_num\" style=\"display:none\"><span>1</span></div>\n\t\n\t\t<img src=\"https://hh.hh-content.com/pictures/items/K2.png\" />\n\t<i><i>i</i></i>\n</div>","<div class=\"slot epic\" rarity=\"epic\" id_item=\"21\" subtype=\"0\" id_m_i=\"Array\" data-d=\"{&quot;id_item&quot;:&quot;21&quot;,&quot;type&quot;:&quot;gift&quot;,&quot;subtype&quot;:&quot;0&quot;,&quot;identifier&quot;:&quot;K2&quot;,&quot;rarity&quot;:&quot;epic&quot;,&quot;value&quot;:&quot;250&quot;,&quot;carac1&quot;:0,&quot;carac2&quot;:0,&quot;carac3&quot;:0,&quot;endurance&quot;:0,&quot;chance&quot;:0,&quot;ego&quot;:0,&quot;damage&quot;:0,&quot;duration&quot;:0,&quot;level_mini&quot;:&quot;1&quot;,&quot;name&quot;:&quot;Chocolats&quot;,&quot;Name&quot;:&quot;Chocolats&quot;,&quot;ico&quot;:&quot;https:\\/\\/hh.hh-content.com\\/pictures\\/items\\/K2.png&quot;,&quot;price_sell&quot;:17375,&quot;count&quot;:1,&quot;id_m_i&quot;:[]}\">\n\n\t\n\t<div id=\"item_21\" class=\"stack_num\" style=\"display:none\"><span>1</span></div>\n\t\n\t\t<img src=\"https://hh.hh-content.com/pictures/items/K2.png\" />\n\t<i><i>i</i></i>\n</div>","<div class=\"slot rare\" rarity=\"rare\" id_item=\"23\" subtype=\"0\" id_m_i=\"Array\" data-d=\"{&quot;id_item&quot;:&quot;23&quot;,&quot;type&quot;:&quot;gift&quot;,&quot;subtype&quot;:&quot;0&quot;,&quot;identifier&quot;:&quot;K3&quot;,&quot;rarity&quot;:&quot;rare&quot;,&quot;value&quot;:&quot;80&quot;,&quot;carac1&quot;:0,&quot;carac2&quot;:0,&quot;carac3&quot;:0,&quot;endurance&quot;:0,&quot;chance&quot;:0,&quot;ego&quot;:0,&quot;damage&quot;:0,&quot;duration&quot;:0,&quot;level_mini&quot;:&quot;1&quot;,&quot;name&quot;:&quot;Bracelet&quot;,&quot;Name&quot;:&quot;Bracelet&quot;,&quot;ico&quot;:&quot;https:\\/\\/hh.hh-content.com\\/pictures\\/items\\/K3.png&quot;,&quot;price_sell&quot;:5561,&quot;count&quot;:1,&quot;id_m_i&quot;:[]}\">\n\n\t\n\t<div id=\"item_23\" class=\"stack_num\" style=\"display:none\"><span>1</span></div>\n\t\n\t\t<img src=\"https://hh.hh-content.com/pictures/items/K3.png\" />\n\t<i><i>i</i></i>\n</div>","<div class=\"slot common\" rarity=\"common\" id_item=\"22\" subtype=\"0\" id_m_i=\"Array\" data-d=\"{&quot;id_item&quot;:&quot;22&quot;,&quot;type&quot;:&quot;gift&quot;,&quot;subtype&quot;:&quot;0&quot;,&quot;identifier&quot;:&quot;K3&quot;,&quot;rarity&quot;:&quot;common&quot;,&quot;value&quot;:&quot;15&quot;,&quot;carac1&quot;:0,&quot;carac2&quot;:0,&quot;carac3&quot;:0,&quot;endurance&quot;:0,&quot;chance&quot;:0,&quot;ego&quot;:0,&quot;damage&quot;:0,&quot;duration&quot;:0,&quot;level_mini&quot;:&quot;1&quot;,&quot;name&quot;:&quot;Bracelet&quot;,&quot;Name&quot;:&quot;Bracelet&quot;,&quot;ico&quot;:&quot;https:\\/\\/hh.hh-content.com\\/pictures\\/items\\/K3.png&quot;,&quot;price_sell&quot;:1043,&quot;count&quot;:1,&quot;id_m_i&quot;:[]}\">\n\n\t\n\t<div id=\"item_22\" class=\"stack_num\" style=\"display:none\"><span>1</span></div>\n\t\n\t\t<img src=\"https://hh.hh-content.com/pictures/items/K3.png\" />\n\t<i><i>i</i></i>\n</div>","<div class=\"slot common\" rarity=\"common\" id_item=\"19\" subtype=\"0\" id_m_i=\"Array\" data-d=\"{&quot;id_item&quot;:&quot;19&quot;,&quot;type&quot;:&quot;gift&quot;,&quot;subtype&quot;:&quot;0&quot;,&quot;identifier&quot;:&quot;K2&quot;,&quot;rarity&quot;:&quot;common&quot;,&quot;value&quot;:&quot;7&quot;,&quot;carac1&quot;:0,&quot;carac2&quot;:0,&quot;carac3&quot;:0,&quot;endurance&quot;:0,&quot;chance&quot;:0,&quot;ego&quot;:0,&quot;damage&quot;:0,&quot;duration&quot;:0,&quot;level_mini&quot;:&quot;1&quot;,&quot;name&quot;:&quot;Chocolats&quot;,&quot;Name&quot;:&quot;Chocolats&quot;,&quot;ico&quot;:&quot;https:\\/\\/hh.hh-content.com\\/pictures\\/items\\/K2.png&quot;,&quot;price_sell&quot;:487,&quot;count&quot;:1,&quot;id_m_i&quot;:[]}\">\n\n\t\n\t<div id=\"item_19\" class=\"stack_num\" style=\"display:none\"><span>1</span></div>\n\t\n\t\t<img src=\"https://hh.hh-content.com/pictures/items/K2.png\" />\n\t<i><i>i</i></i>\n</div>","<div class=\"slot rare\" rarity=\"rare\" id_item=\"20\" subtype=\"0\" id_m_i=\"Array\" data-d=\"{&quot;id_item&quot;:&quot;20&quot;,&quot;type&quot;:&quot;gift&quot;,&quot;subtype&quot;:&quot;0&quot;,&quot;identifier&quot;:&quot;K2&quot;,&quot;rarity&quot;:&quot;rare&quot;,&quot;value&quot;:&quot;60&quot;,&quot;carac1&quot;:0,&quot;carac2&quot;:0,&quot;carac3&quot;:0,&quot;endurance&quot;:0,&quot;chance&quot;:0,&quot;ego&quot;:0,&quot;damage&quot;:0,&quot;duration&quot;:0,&quot;level_mini&quot;:&quot;1&quot;,&quot;name&quot;:&quot;Chocolats&quot;,&quot;Name&quot;:&quot;Chocolats&quot;,&quot;ico&quot;:&quot;https:\\/\\/hh.hh-content.com\\/pictures\\/items\\/K2.png&quot;,&quot;price_sell&quot;:4171,&quot;count&quot;:1,&quot;id_m_i&quot;:[]}\">\n\n\t\n\t<div id=\"item_20\" class=\"stack_num\" style=\"display:none\"><span>1</span></div>\n\t\n\t\t<img src=\"https://hh.hh-content.com/pictures/items/K2.png\" />\n\t<i><i>i</i></i>\n</div>","<div class=\"slot rare\" rarity=\"rare\" id_item=\"20\" subtype=\"0\" id_m_i=\"Array\" data-d=\"{&quot;id_item&quot;:&quot;20&quot;,&quot;type&quot;:&quot;gift&quot;,&quot;subtype&quot;:&quot;0&quot;,&quot;identifier&quot;:&quot;K2&quot;,&quot;rarity&quot;:&quot;rare&quot;,&quot;value&quot;:&quot;60&quot;,&quot;carac1&quot;:0,&quot;carac2&quot;:0,&quot;carac3&quot;:0,&quot;endurance&quot;:0,&quot;chance&quot;:0,&quot;ego&quot;:0,&quot;damage&quot;:0,&quot;duration&quot;:0,&quot;level_mini&quot;:&quot;1&quot;,&quot;name&quot;:&quot;Chocolats&quot;,&quot;Name&quot;:&quot;Chocolats&quot;,&quot;ico&quot;:&quot;https:\\/\\/hh.hh-content.com\\/pictures\\/items\\/K2.png&quot;,&quot;price_sell&quot;:4171,&quot;count&quot;:1,&quot;id_m_i&quot;:[]}\">\n\n\t\n\t<div id=\"item_20\" class=\"stack_num\" style=\"display:none\"><span>1</span></div>\n\t\n\t\t<img src=\"https://hh.hh-content.com/pictures/items/K2.png\" />\n\t<i><i>i</i></i>\n</div>"];
	Notification.success({ message: '<b>Butin collecté</b> :<br><ul class="rewards"><li> ' + rewards.join('</li><li>').replace(/\<i\>i\<\/i\>/g, '') + '</li></ul>', delay: '0' });
	
	function destroyBoss() {
		if (vm.bossSelected) {
			EntityService.bossSrv.destroy(vm.bossSelected).then(function(response) {
				vm.hero.energyFight = 0;
				var rewards = response.data;
				if (rewards.length) {
					Notification.success({ message: '<b>Butin collecté</b> :<br><ul><li> ' + rewards.join('</li><li>').replace(/\<i\>i\<\/i\>/g, '') + '</li></ul>' });
				}
			}, function(response) {
				console.debug(response.data);
			});
		}
	}
	
	function doCollectSalaries() {
		EntityService.filleSrv.doCollectSalaries(function() {
			// nothing to do
		}, function(response) {
			console.debug(response.data);
		});
	}
}

ModalLoginController.$inject = ['$uibModalInstance', 'GameService'];

/**
 * Modal de connexion à HHeroes.
 * 
 * @param $uibModalInstance
 * @param GameService
 * @returns
 */
function ModalLoginController($uibModalInstance, GameService) {
	var vm = this;
	vm.user = {};
		
	vm.login = login;
	vm.close = close;
	
	function login() {
		GameService.login(vm.user).then(function() {
			$uibModalInstance.close(true);
		}, function(response) {
			vm.errorMessage = response.data.message;
		});
	}
	
	function close() {
		$uibModalInstance.dismiss('cancel');
	}
}

ModalAvatarController.$inject = ['$uibModalInstance', 'girl', 'FilleService'];

/**
 * Modal permettant de voir les avatars des filles.
 * 
 * @param $uibModalInstance
 * @param girl
 * @returns
 */
function ModalAvatarController($uibModalInstance, girl, FilleService) {
	var vm = this;

	vm.girl = girl;
	vm.grades = [];
	
	vm.close = close;
	
	function activate() {
		for (var i = 0; i <= girl.grade; i++) {
			vm.grades.push(i);
		}
		
		vm.rarityLabel = FilleService.getRarityLibelle(vm.girl.rarity);
		vm.rarityClass = FilleService.getRarityClass(vm.girl.rarity);
	}	
	
	function close() {
		$uibModalInstance.dismiss('cancel');
	}
	
	activate();
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
		onCollect: '&',
		onDoCollect: '&'
	}
});

function GirlSalaryWidgetController() { 
	var vm = this;
	
	vm.totalSalary = 0;
	vm.totalSalaryPerHour = 0;
	vm.collectableSalary = 0;
	vm.collect = collect;
	vm.doCollect = doCollect;
	
	vm.$onInit = function() {
		angular.forEach(vm.filles, function(fille) {
			vm.totalSalary += fille.salary;
			vm.totalSalaryPerHour += fille.salaryPerHour;
			vm.collectableSalary += (fille.collectable) ? fille.salary : 0;
		});
	}
	
	function collect() {
		vm.disabled = true;
		vm.onCollect({ collectedSalary: vm.collectableSalary }).then(function(response) {
			vm.collectableSalary = 0;				
		}, function(response) {
			console.debug(response);
		});
	}
	
	function doCollect() {
		vm.onDoCollect();
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

heroesApp.component('heroWidget', {
	templateUrl: 'pages/templates/heroWidget.html',
	controller: HeroWidgetController,
	controllerAs: 'vm',
	bindings: {
		hero: '<',
	}
});

heroesApp.component('headerWidget', {
	templateUrl: 'pages/templates/header.tpl.html',
	controller: HeaderWidgetController,
	controllerAs: 'vm',
	bindings: {
		hero: '<',
		nbFilles: '<',
		onLogout: '&'
	}
});

HeaderWidgetController.$inject = ['$uibModal'];

function HeaderWidgetController($uibModal) { 
	var vm = this; 
	vm.updateUser = updateUser;
	vm.logout = logout;
	
	function updateUser() {
		var modalInstance = $uibModal.open({
			animation : true,
			templateUrl : 'pages/templates/user.html',
			controller : 'ModalUserController',
			controllerAs : 'vm',
			resolve : {
				hero : function() {
					return vm.hero;
				}
			}
		});
		
		modalInstance.result.then(function() {
			//
	    });
	}
	
	function logout() {
		vm.onLogout();
	}
}

ModalUserController.$inject = ['$uibModalInstance', 'EntityService', 'Notification', 'hero'];

function ModalUserController($uibModalInstance, EntityService, Notification, hero) {
	var vm = this; 
	vm.hero = hero;
	
	vm.valider = valider;
	vm.close = close;
	
	activate();
	
	function activate() {
		EntityService.userSrv.getMe().then(function(response) {
			vm.user = response.data;
		});
		EntityService.bossSrv.getAll().then(function(response) {
			vm.bosses = response.data;
		});
	}
	
	function valider() {
		EntityService.userSrv.update(vm.user).then(function() {
			$uibModalInstance.close();
		}, function(response) {
			Notification.error({ message: 'Erreur lors de la mise à jour de l\'utilisateur : ' + response.data.message });
		});
	}
	
	function close() {
		$uibModalInstance.dismiss('cancel');
	}
	
}

HeroWidgetController.$inejct = ['HeroService', 'Notification'];

function HeroWidgetController(HeroService, Notification) { 
	var vm = this; 
	vm.updateStat = updateStat;
	vm.carac = 0;
	
	function updateStat(carac) {
		vm.carac = carac;
		
		HeroService.updateStat(carac).then(function(response) {
			var stat = response.data;
			var moneySpent = vm.hero.money - stat.soft_currency;
			var message = 'Vous êtes arrivé au max de vos stats';
			if (stat.success) {
				message = 'Nouvelles statistiques:<br>' 
					+ '<b>Chance</b>: ' + stat.chance.toLocaleString() 
					+ '<br><b>Endurance: </b>' + stat.endurance.toLocaleString() 
					+ '<br><b>Argent dépensé</b>: ' + moneySpent.toLocaleString() + ' $';
				vm.hero.money = stat.soft_currency;
			}
			
			vm.carac = 0;
			Notification.success({ message: message });
		}, function(response) {
			vm.carac = 0;
			Notification.error({ message: 'Erreur lors de la mise des stats du héros : ' + response.data.message });
		});
	}
}