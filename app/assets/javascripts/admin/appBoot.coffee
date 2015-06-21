define [
	'appModule'
	'appService'
	'fileUploadService'
	'appRoute'
	'appForgotPasswordController'
	'appLoginController'
	'appDashController'
	'appUsersController'
	'appFeedsController'
], (app) ->
	app.init = -> angular.bootstrap document, [ 'app' ]
	app
