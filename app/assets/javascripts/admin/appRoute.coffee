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
			.when '/users/import-export',
				templateUrl: 'assets/views/admin/user/import-export.html'
				controller: 'ImportExportController'
			.when '/feeds',
				templateUrl: 'assets/views/admin/feed/feeds.html'
				controller: 'FeedsController'
			.when '/feeds/import-export',
				templateUrl: 'assets/views/admin/feed/import-export.html'
				controller: 'ImportExportController'
			.when '/feeds/popular',
				templateUrl: 'assets/views/admin/feed/popular.html'
				controller: 'PopularController'
			.when '/feeds/trending',
				templateUrl: 'assets/views/admin/feed/trending.html'
				controller: 'TrendingController'
			.otherwise redirectTo: '/'
		]