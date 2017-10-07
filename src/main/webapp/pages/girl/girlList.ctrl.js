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
	
	ListeFilleController.$inject = ['$uibModal', 'DTOptionsBuilder', 'DTColumnDefBuilder'];
	
	function ListeFilleController($uibModal, DTOptionsBuilder, DTColumnDefBuilder) { 
		var vm = this;
		vm.search = {};
		vm.dtColumnDefs = [];
		vm.dtOptions = [];
		
		var StatusLevel = {
			MAX: 'Max.',
			UPGRADE: 'En upgrade'
		}
		
		vm.openModalAvatar = openModalAvatar;
		
		$.fn.dataTableExt.oSort['exp-left-asc'] = sortExp(true); 
		$.fn.dataTableExt.oSort['exp-left-desc'] = sortExp(false);
		
		function activate() {
			vm.dtOptions = DTOptionsBuilder.newOptions()
				.withPaginationType('full_numbers')
				.withOption('order', [])
				.withOption('language', { 
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
					
				})
				.withDisplayLength(10)
				.withBootstrap();
			
			vm.dtColumnDefs = [
		        DTColumnDefBuilder.newColumnDef(0).notSortable().withOption('width', '60px'),
		        DTColumnDefBuilder.newColumnDef(5).withOption('type', 'exp-left'),
		        DTColumnDefBuilder.newColumnDef(6).withOption('type', 'exp-left')
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
		
		function getFloatValue(expLeft) {
			var value = expLeft;
			
			if (angular.isString(value)) {
				var value = value.replace(/\s+/g, '');
			}
			
			if (value === StatusLevel.MAX) value = 99999.9;
			if (value === StatusLevel.UPGRADE) value = 0.0;
			
			return parseFloat(value);
		}
		
		activate();
	}
}());