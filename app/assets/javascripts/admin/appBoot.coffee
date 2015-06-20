define [
	'appModule'
	'appService'
	'appRoute'
	'appForgotPasswordController'
	'appLoginController'
	'appDashController'
	'appUsersController'
	'appFeedsController'
], (app) ->
	app.init = -> angular.bootstrap document, [ 'app' ]
	app
