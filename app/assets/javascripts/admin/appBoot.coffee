define [
	'appService'
	'appRoute'
	'appAdminController'
	'appForgotPasswordController'
	'appLoginController'
	'appDashController'
	'appUsersController'
	'appNewslettersController'
], (app) ->
	app.init = -> angular.bootstrap document, [ 'app' ]
	app
