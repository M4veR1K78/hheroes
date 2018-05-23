(function() {
	'use strict';
	
	angular.module('heroesApp').component('girlList', {
		templateUrl: 'pages/girl/girlList.tpl.html',
		controller: ListeFilleController,
		controllerAs: 'vm',
		bindings: {
			filles: '<'
		}
	});
	
	ListeFilleController.$inject = ['$scope', '$uibModal', 'DTOptionsBuilder', 'DTColumnDefBuilder', 'UtilService'];
	
	function ListeFilleController($scope, $uibModal, DTOptionsBuilder, DTColumnDefBuilder, UtilService) { 
		var vm = this;
		vm.search = {};
		vm.dtColumnDefs = [];
		vm.dtInstance = {};
		vm.dtOptions = [];
		vm.nbHardcore = 0;
		vm.nbCharme = 0;
		vm.nbSavoirFaire = 0;
		
		var StatusLevel = {
			MAX: 'Max.',
			UPGRADE: 'En upgrade'
		}
		
		$scope.$watchCollection('vm.filles', function(filles) {
			if (filles.length > 0) {
				angular.forEach(filles, function(fille) {
					if (fille.typeId === 1) vm.nbHardcore++;
					else if (fille.typeId === 2) vm.nbCharme++;
					else if (fille.typeId === 3) vm.nbSavoirFaire++;
				});
			}
		});
		
		vm.openModalAvatar = openModalAvatar;
		
		$.fn.dataTableExt.oSort['exp-left-asc'] = sortExp(true); 
		$.fn.dataTableExt.oSort['exp-left-desc'] = sortExp(false);
		$.fn.dataTableExt.oSort['classement-asc'] = sortClassement(true);
		$.fn.dataTableExt.oSort['classement-desc'] = sortClassement(false);
		
		function activate() {
			vm.currentPage = 0;
			
			vm.dtOptions = DTOptionsBuilder.newOptions()
				.withPaginationType('full_numbers')
				.withOption('order', [])
				.withOption('language', UtilService.getDTTLangues())
				.withDisplayLength(10)
				.withOption('deferRender', true)
				.withOption('initComplete', function() {
					if(vm.dtInstance.DataTable) {
						vm.dtInstance.DataTable.on('page.dt', function () {
				            $scope.$apply(function() {
				            	vm.currentPage = vm.dtInstance.DataTable.page();
				            });
				        });
					}
				})
				.withBootstrap();
			
			vm.dtColumnDefs = [
		        DTColumnDefBuilder.newColumnDef(0).notSortable().withOption('width', '60px'),
		        DTColumnDefBuilder.newColumnDef(6).withOption('type', 'exp-left'),
		        DTColumnDefBuilder.newColumnDef(7).withOption('type', 'exp-left'),
		        DTColumnDefBuilder.newColumnDef(12).withOption('type', 'classement')
		    ];
		}
		
		function openModalAvatar(fille) {
			console.log(vm.dtInstance.DataTable.rows(), vm.dtInstance.DataTable.page());
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
		
		/**
		 * Fonction de tri de l'expérience (ou affectation).
		 * 
		 * @param asc true si tri ascendant.
		 */
		function sortExp(asc) {
			function sortArray(a, b) {
				var value1 = getFloatValue((asc) ? a : b);
				var value2 = getFloatValue((asc) ? b : a);
				
				return (value1 > value2) ? 1 : (value1 < value2) ? -1 : 0;
			}
			
			return sortArray;
		}
		
		/**
		 * Fonction de tri du classement.
		 * 
		 * @param asc true si tri ascendant.
		 */
		function sortClassement(asc) {
			function sortArray(a, b) {
				a = removeText(a);
				b = removeText(b);
				var value1 = getFloatValue((asc) ? a : b);
				var value2 = getFloatValue((asc) ? b : a);
				
				return (value1 > value2) ? 1 : (value1 < value2) ? -1 : 0;
			}
			
			return sortArray;
		}
		
		/**
		 * Retire les éléments superflue du classement pour pouvoir le trier correctement.
		 */
		function removeText(value) {
			return angular.element(value).text().replace(/(ère)|(ème)/g, '');
		}
		
		function getFloatValue(expLeft) {
			var value = expLeft;

			if (value === StatusLevel.MAX) value = 99999.9;
			if (value === StatusLevel.UPGRADE) value = 0.0;

			if (angular.isString(value)) {
				var value = value.replace(/\s+/g, '');
			}

			return parseFloat(value);
		}
		
		activate();
	}
}());