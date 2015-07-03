define [ 'angular', 'ngRoute', 'angular-ui-bootstrap', 'ngCkeditor' ], (angular, ngRoute, angular_ui_bootstrap, ngCkeditor) ->
	angular.module( 'app', ['ngRoute', 'ui.bootstrap', 'ngCkeditor'] )