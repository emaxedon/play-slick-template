require.config
	baseUrl: '/assets'

	shim:
		'bootstrap': deps: [ 'jquery' ]
		'ngRoute': 
			deps: [ 'angular' ]
			exports: 'angular'
		'angular':
			deps: [ 'jquery' ]
			exports: 'angular'
		'angular-ui-bootstrap':
			deps: [ 'angular' ]

	paths:
		'jquery': 'lib/jquery/jquery.min'
		'bootstrap': 'lib/bootstrap/js/bootstrap.min'
		'angular': 'lib/angularjs/angular.min'
		'ngRoute': 'lib/angularjs/angular-route.min'
		'angular-ui-bootstrap': 'lib/angular-ui-bootstrap/ui-bootstrap-tpls.min'
		
		'appModule': 'javascripts/admin/appModule'
		'appService': 'javascripts/admin/services/appService'
		'fileUploadService': 'javascripts/admin/services/fileUploadService'
		'fileModel': 'javascripts/admin/directives/fileModel'
		'appRoute': 'javascripts/admin/appRoute'
		'appForgotPasswordController': 'javascripts/admin/controllers/appForgotPasswordController'
		'appLoginController': 'javascripts/admin/controllers/appLoginController'
		'appDashController': 'javascripts/admin/controllers/appDashController'
		'appUsersController': 'javascripts/admin/controllers/appUsersController'
		'appFeedsController': 'javascripts/admin/controllers/appFeedsController'
		'appPopularController': 'javascripts/admin/controllers/appPopularController'
		'appTrendingController': 'javascripts/admin/controllers/appTrendingController'
		'appImportExportController': 'javascripts/admin/controllers/appImportExportController'
		'appBoot': 'javascripts/admin/appBoot'

require [
	'jquery'
	'appBoot'
	'bootstrap'
], ($, app) ->
	$(document).ready ->
		app.init()