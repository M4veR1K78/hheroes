(function() {
	'use strict';
	
	angular.module('heroesApp').component('activityList', {
		templateUrl: 'pages/activity/activity.tpl.html',
		controller: ListeMissionController,
		controllerAs: 'vm',
		bindings: {
			hero: '<'
		}
	});
	
	ListeMissionController.$inject = ['$scope', '$compile', '$q', '$uibModal', 'DTOptionsBuilder', 'DTColumnBuilder', 'EntityService'];
	
	function ListeMissionController($scope, $compile, $q, $uibModal, DTOptionsBuilder, DTColumnBuilder, EntityService) { 
		var vm = this;
		vm.missions = [];
		vm.dtInstance = {};
		
		vm.start = start;
		vm.showButton = showButton;
		vm.claimRewards = claimRewards;
		vm.canClaimReward = canClaimReward;
		
		function activate() {
			vm.dtOptions = DTOptionsBuilder.fromFnPromise(function() {
					return getMissions();
				})
				.withOption('createdRow', createdRow)
				.withOption('order', [])
				.withOption('language', EntityService.utilSrv.getDTTLangues())
				.withDisplayLength(5)
				.withOption('searching', false)
				.withOption('info', false)
				.withOption('lengthChange', false);
			
			vm.dtColumns = [
		        DTColumnBuilder.newColumn('titre').withTitle('Mission'),
		        DTColumnBuilder.newColumn('experience').withTitle('Exp.'),
		        DTColumnBuilder.newColumn('duree').withTitle('Durée').renderWith(function(data, type, mission) {
		        	return '{{ vm.missions[' + mission.id + '].duree | secondsToDateTime | date: \'mm:ss\' }}';
		        }),
		        DTColumnBuilder.newColumn('statut').withTitle('Statut').renderWith(function(data, type, mission) {
		        	mission.statut.replace('_', ' ');
		        	return '<span ng-class="{ \'bg-blue\': vm.missions[' + mission.id + '].statut === \'TERMINEE\', ' +
		        		'\'bg-red\': vm.missions[' + mission.id + '].statut === \'EN_COURS\', ' +
		        		'\'bg-yellow\': vm.missions[' + mission.id + '].statut === \'EN_ATTENTE\', ' +
		        		'\'bg-green\': vm.missions[' + mission.id + '].statut === \'PRETE\', badge: true }" style="text-transform: capitalize">{{ vm.missions[' + mission.id + '].statut | lowercase }}</span>';
		        }),
		    ];
		}
		
		function start() {
			EntityService.activitySrv.start().then(function() {
				vm.disabled = true;
				vm.dtInstance.reloadData();
			}, function() {
				alert('Echec du lancement de script automatique des missions');
			});
		}
		
		function getMissions() {
			var deferred = $q.defer();
			
			EntityService.activitySrv.getAll().then(function(response) {
				var missions = response.data;
				var exp = 0;
				missions.forEach(function(mission) {
					if (mission.statut === 'TERMINEE') {
						exp += mission.experience;
					}
				});
				vm.levelWarning = exp >= vm.hero.experience.left && (vm.hero.level + '').match(/\d*9/);
				deferred.resolve(missions);
			});
			
			return deferred.promise;
		}
		
		function createdRow(row, data, dataIndex) {
			vm.missions[data.id] = data;
		    $compile(angular.element(row).contents())($scope);
		}
		
		function showButton() {
			if (angular.equals([], vm.missions)) {
				return false;
			}
			else {
				var allOver = true;
				Object.keys(vm.missions).forEach(function(key) {
				    if (vm.missions[key].statut !== 'TERMINEE') {
				    	allOver = false;
				    }
				});				
			}
			
			return !allOver;
		}
		
		function claimRewards() {
			EntityService.activitySrv.claimRewards().then(function() {
				vm.missions = [];
				vm.dtInstance.reloadData();
			}, function() {
				alert('Echec du lancement de script automatique des missions');
			})
		}
		
		function canClaimReward() {
			var over = false
			if (vm.missions && vm.missions.length > 0) {
				Object.keys(vm.missions).forEach(function(key) {
				    if (vm.missions[key].statut === 'TERMINEE') {
				    	over = true;
				    }
				});	
			}
			return over;
		}
		
		activate();
	}
}());