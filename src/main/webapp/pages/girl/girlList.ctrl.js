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
	
	ListeFilleController.$inject = ['$scope', '$uibModal', 'DTOptionsBuilder', 'DTColumnDefBuilder', 'EntityService'];
	
	function ListeFilleController($scope, $uibModal, DTOptionsBuilder, DTColumnDefBuilder, EntityService) { 
		var vm = this;
		vm.search = {};
		vm.dtColumnDefs = [];
		vm.dtInstance = {};
		vm.dtOptions = [];
		vm.nbHardcore = 0;
		vm.nbCharme = 0;
		vm.nbSavoirFaire = 0;
		vm.nbCommon = 0;
		vm.nbRare = 0;
		vm.nbEpic = 0;
		vm.nbLegendary = 0;
		
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
					
					if (fille.rarity === 'COMMUN') {
						vm.nbCommon++;
					} else if (fille.rarity === 'RARE') {
						vm.nbRare++;
					} else if (fille.rarity === 'EPIQUE') {
						vm.nbEpic++;
					} else if (fille.rarity === 'LEGENDAIRE') {
						vm.nbLegendary++;
					}
				});
			}
		});
		
		vm.openModalAvatar = openModalAvatar;
		vm.getRarityClass = getRarityClass;
		vm.getRarityLibelle = getRarityLibelle;
		
		$.fn.dataTableExt.oSort['exp-left-asc'] = sortExp(true); 
		$.fn.dataTableExt.oSort['exp-left-desc'] = sortExp(false);
		$.fn.dataTableExt.oSort['classement-asc'] = sortClassement(true);
		$.fn.dataTableExt.oSort['classement-desc'] = sortClassement(false);
		$.fn.dataTableExt.oSort['position-asc'] = sortPosition(true);
		$.fn.dataTableExt.oSort['position-desc'] = sortPosition(false);
		
		function activate() {
			vm.currentPage = 0;
			
			vm.dtOptions = DTOptionsBuilder.newOptions()
				.withPaginationType('full_numbers')
				.withOption('order', [])
				.withOption('language', EntityService.utilSrv.getDTTLangues())
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
				});
			
			vm.dtColumnDefs = [
		        DTColumnDefBuilder.newColumnDef(0).notSortable().withOption('width', '60px'),
		        DTColumnDefBuilder.newColumnDef(7).withOption('type', 'exp-left'),
		        DTColumnDefBuilder.newColumnDef(8).withOption('type', 'exp-left'),
		        DTColumnDefBuilder.newColumnDef(9).withOption('type', 'position'),
		        DTColumnDefBuilder.newColumnDef(13).withOption('type', 'classement'),
		        DTColumnDefBuilder.newColumnDef(14).notVisible()
		    ];
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
		 * Fonction de tri des positions.
		 */
		function sortPosition(asc) {
			function sortArray(a, b) {
				var value1 = extractTitle((asc) ? a : b);
				var value2 = extractTitle((asc) ? b : a);
				
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
		
		function extractTitle(value) {
			return angular.element(value).attr("title");
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
		
		/**
		 * Récupère la classe d'une fille en fonction de sa rareté.
		 */
		function getRarityClass(filleRarity) {
			return EntityService.filleSrv.getRarityClass(filleRarity);
		}
		
		/**
		 * Récupère le libellé de la rareté d'une fille (pour pouvoir les filtrer sur cet élément).
		 */
		function getRarityLibelle(filleRarity) {
			return EntityService.filleSrv.getRarityLibelle(filleRarity);
		}
		
		activate();
	}
}());