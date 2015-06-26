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
				templateUrl: 'assets/views/admin/users.html'
				controller: 'UsersController'
			.when '/feeds',
				templateUrl: 'assets/views/admin/feeds.html'
				controller: 'FeedsController'
			.when '/feeds/popular',
				templateUrl: 'assets/views/admin/popular.html'
				controller: 'PopularController'
			.when '/feeds/trending',
				templateUrl: 'assets/views/admin/trending.html'
				controller: 'TrendingController'
			.otherwise redirectTo: '/'
		]