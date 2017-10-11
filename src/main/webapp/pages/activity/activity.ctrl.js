(function() {
	'use strict';
	
	angular.module('heroesApp').component('activityList', {
		templateUrl: 'pages/activity/activity.tpl.html',
		controller: ListeMissionController,
		controllerAs: 'vm',
		bindings: {
			missions: '<'
		}
	});
	
	ListeMissionController.$inject = ['$uibModal', 'DTOptionsBuilder', 'DTColumnDefBuilder', 'EntityService'];
	
	function ListeMissionController($uibModal, DTOptionsBuilder, DTColumnDefBuilder, EntityService) { 
		var vm = this;
		
		vm.start = start;
		
		function activate() {
			vm.dtOptions = DTOptionsBuilder.newOptions()
				.withOption('order', [])
				.withOption('language', EntityService.utilSrv.getDTTLangues())
				.withDisplayLength(5)
				.withOption('searching', false)
				.withOption('info', false)
				.withOption('lengthChange', false)
				.withBootstrap();
		}
		
		function start() {
			EntityService.activitySrv.start().then(function() {
				vm.disabled = true;
			}, function() {
				alert('Echec du lancement de script automatique des missions');
			});
		}
		
		activate();
	}
}());