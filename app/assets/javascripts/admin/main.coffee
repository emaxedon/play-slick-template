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
		'ngCkeditor':
			deps: [ 'angular', 'ckeditor' ]

	paths:
		'jquery': 'lib/jquery/jquery.min'
		'bootstrap': 'lib/bootstrap/js/bootstrap.min'
		'angular': 'lib/angularjs/angular.min'
		'ngRoute': 'lib/angularjs/angular-route.min'
		'angular-ui-bootstrap': 'lib/angular-ui-bootstrap/ui-bootstrap-tpls.min'
		'ngCkeditor': 'lib/ng-ckeditor/ng-ckeditor'
		'ckeditor': 'ckeditor/ckeditor'
		
		'appModule': 'javascripts/admin/appModule'
		'appService': 'javascripts/admin/services/appService'
		'appRoute': 'javascripts/admin/appRoute'
		'appAdminController': 'javascripts/admin/controllers/appAdminController'
		'appForgotPasswordController': 'javascripts/admin/controllers/appForgotPasswordController'
		'appLoginController': 'javascripts/admin/controllers/appLoginController'
		'appDashController': 'javascripts/admin/controllers/appDashController'
		'appUsersController': 'javascripts/admin/controllers/appUsersController'
		'appNewslettersController': 'javascripts/admin/controllers/appNewslettersController'
		'appBoot': 'javascripts/admin/appBoot'

require [
	'jquery'
	'appBoot'
	'bootstrap'
], ($, app) ->
	$(document).ready ->
		app.init()