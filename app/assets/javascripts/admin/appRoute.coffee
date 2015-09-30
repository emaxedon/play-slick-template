define [ 'appModule' ], (app) ->
	app.config ['$routeProvider', ($routeProvider) ->
		$routeProvider
			.when '/',
				templateUrl: 'assets/views/admin/login.html'
				controller: 'LoginController'
			.when '/forgotPassword',
				templateUrl: 'assets/views/admin/forgotPassword.html'
				controller: 'ForgotPasswordController'
			.when '/dash',
				templateUrl: 'assets/views/admin/dash.html'
				controller: 'DashController'
			.when '/users',
				templateUrl: 'assets/views/admin/user/users.html'
				controller: 'UsersController'
			.when '/newsletters',
				templateUrl: 'assets/views/admin/newsletters.html'
				controller: 'NewslettersController'
			.otherwise redirectTo: '/'
		]