define [ 'angular', 'ngRoute', 'ngCookies', 'angular-ui-bootstrap' ], (angular, ngCookies) ->
	app = angular.module( 'app', ['ngRoute', 'ngCookies', 'ui.bootstrap'] )

	app.factory 'service', ['$http', '$location', ($http, $location) ->
		logout: ->
			$http.get('/auth/logout')
				.success (data, status, headers, config) ->
					$location.path '/'
		loggedin: ->
			$http.get('/auth/user')
				.success (data, status, headers, config) ->
					if (data.result != 0)
						$location.path '/'
		user: (success, error) ->
			$http.get('/auth/user')
				.success (data, status, headers, config) -> success(data)			
				.error (data, status, headers, config) -> error(status)
		]
		
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
			.otherwise redirectTo: '/'
		]

	app.controller 'ForgotPasswordController', ['$scope', '$http', ($scope, $http) ->
		$scope.submit = ->
			$scope.disabled = true
			$scope.info = "Generating password reset email..."
			$scope.message = false
			$scope.error = false
			$http.post('/auth/forgotPassword', {email: $scope.email})
				.success (data, status, headers, config) ->
					if (data.result == 0)
						$scope.info = false
						$scope.message = "Password reset email has been sent"
					else
						$scope.error = data.message
						$scope.message = false
						$scope.info = false
						
					$scope.disabled = false
				.error (data, status, headers, config) ->
					$scope.message = false
					$scope.info = false
					$scope.error = "Error resetting password - try again"
					$scope.disabled = false
		]
	
	app.controller 'LoginController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		service.user ((data) ->
			if data.result == 0
				$location.path '/dash'
			else
				$scope.show = true
		), (status) ->
			$scope.show = true

		$scope.submit = ->
			$scope.disabled = true
			$http.post('/auth/login', {email: $scope.email, password: $scope.password})
				.success (data, status, headers, config) ->
					$scope.disabled = false
					
					if (data.result == 0)
						if (data.data.role != "admin")
							$scope.error = "Oops! Not an administrator account."
							service.logout()
						else
							$location.path '/dash'
					else
						$scope.error = data.message
				.error (data, status, headers, config) ->
					$scope.error = "Error logging in - try again."
		]

	app.controller 'DashController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		service.loggedin()
		$http.get('/auth/recent/10')
			.success (data, status, headers, config) ->
				console.log data
				
				if data.result == 0
					$scope.users = data.data.users
					
			.error (data, status, headers, config) ->
				console.log data
				console.log status
				
		$http.get('/auth/count')
			.success (data, status, headers, config) ->
				if data.result == 0
					$scope.userCount = data.data.count
		$http.get('/feeds/recent/10')
			.success (data, status, headers, config) ->
				if data.result == 0
					$scope.feeds = data.data.feeds
				
		$http.get('/feeds/count')
			.success (data, status, headers, config) ->
				if data.result == 0
					$scope.feedCount = data.data.count
				
		$scope.logout = service.logout
		]

	app.controller 'UsersController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.matches = 0
		$scope.users = []
		$scope.email = ""
		service.loggedin()
		
		search = ->
			if $scope.email.length > 0
				$http.post('/auth/prefix', {search: $scope.email})
					.success (data, status, headers, config) ->
						if data.result == 0
							$scope.matches = data.data.users.length
							$scope.users = data.data.users
			else
				$scope.matches = 0
				$scope.users = []

		$scope.change = ->
			if $scope.email.length >= 3
				search()
			else
				$scope.matches = 0
				$scope.users = []
				
		$scope.remove = ->
			$http.get('/auth/remove/' + $scope.userId)
				.success (data, status, headers, config) ->
					if data.result == 0
						$scope.form = false
						search()
		
		$scope.create = ->
			$scope.userDetails =
				"email": ""
				"password": ""
				"role": "user"
				"location": ""
			$scope.form = 'create'

		$scope.cancel = -> $scope.form = false
		
		$scope.edit = (id) ->
			$http.get('/auth/user/' + id)
				.success (data, status, headers, config) ->
					if data.result == 0
						$scope.userId = id
						$scope.userDetails =
							"email": data.data.email
							"password": ""
							"role": data.data.role
							"location": data.data.location
						$scope.disabled = false
						$scope.form = 'edit'
			
		$scope.formSubmit = ->
			$scope.disabled = true
			
			if $scope.form == 'edit'
				$http.put('/auth/user/' + $scope.userId, $scope.userDetails )
					.success (data, status, headers, config) ->
						if data.result == 0
							$scope.disabled = false
							search()
			else
				$http.post( '/auth/create', $scope.userDetails )
					.success (data, status, headers, config) ->
						if data.result == 0
							$scope.form = false
							$scope.disabled = false
							search()

		$scope.getLocation = (val) ->
			$http.get 'http://maps.googleapis.com/maps/api/geocode/json',
				params:
					address: val
					sensor: false
			.then (response) ->
				response.data.results.map( (item) -> item.formatted_address )

		$scope.submit = search
		
		$scope.logout = service.logout
		]

	app.controller 'FeedsController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.matches = 0
		$scope.feeds = []
		$scope.name = ""
		service.loggedin()
		
		search = ->
			if $scope.name.length > 0
				$http.post('/feeds/prefix', {search: $scope.name})
					.success (data, status, headers, config) ->
						if data.result == 0
							$scope.matches = data.data.feeds.length
							$scope.feeds = data.data.feeds
			else
				$scope.matches = 0
				$scope.feeds = []

		$scope.getInstagramUsername = (val) ->
			$http.jsonp 'https://api.instagram.com/v1/users/search',
				responseType: "json"
				params:
					callback: "JSON_CALLBACK"
					q: val
					client_id: "e7ac00adad054cde8d2ee7ea6956dc8e"
			.then (response) ->
				response.data.data.map( (u) -> u.username )

		$scope.getTwitterUsername = (val) ->
			$http.get '/twitter/search',
				params:
					screenName: val
			.then (response) ->
				response.data.map( (item) -> item.screen_name )
			
		$scope.change = ->
			if $scope.name.length >= 3
				search()
			else
				$scope.matches = 0
				$scope.feeds = []
				
		$scope.remove = ->
			$http.get('/feeds/remove/' + $scope.feedId)
				.success (data, status, headers, config) ->
					if data.result == 0
						$scope.form = false
						search()
		
		$scope.create = ->
			$scope.feedDetails =
				"category": "player"
				"name": ""
				"facebookApi": ""
				"twitterApi": ""
				"instagramApi": ""
				"youtubeApi": ""
				"location": ""
			$scope.disabled = false
			$scope.form = 'create'

		$scope.cancel = -> $scope.form = false
		
		$scope.edit = (id) ->
			$http.get('/feeds/' + id)
				.success (data, status, headers, config) ->
					if data.result == 0
						$scope.feedId = id
						$scope.feedDetails =
							"category": data.data.category
							"name": data.data.name
							"facebookApi": data.data.facebookApi
							"twitterApi": data.data.twitterApi
							"instagramApi": data.data.instagramApi
							"youtubeApi": data.data.youtubeApi
							"location": data.data.location
						$scope.disabled = false
						$scope.form = 'edit'
		
		$scope.formSubmit = ->
			$scope.disabled = true
			
			if $scope.feedDetails['facebookApi'] == ""
				delete $scope.feedDetails['facebookApi']
				
			if $scope.feedDetails['twitterApi'] == ""
				delete $scope.feedDetails['twitterApi']
				
			if $scope.feedDetails['instagramApi'] == ""
				delete $scope.feedDetails['instagramApi']
				
			if $scope.feedDetails['youtubeApi'] == ""
				delete $scope.feedDetails['youtubeApi']
			
			if $scope.form == 'edit'
				$http.put('/feeds/' + $scope.feedId, $scope.feedDetails )
					.success (data, status, headers, config) ->
						if data.result == 0
							$scope.disabled = false
							search()
					.error (data, status, headers, config) ->
						console.log 'error'
			else
				$http.post( '/feeds/create', $scope.feedDetails )
					.success (data, status, headers, config) ->
						if data.result == 0
							$scope.form = false
							$scope.disabled = false
							search()
					.error (data, status, headers, config) ->
						console.log data
						console.log status

		$scope.getLocation = (val) ->
			$http.get 'http://maps.googleapis.com/maps/api/geocode/json',
				params:
					address: val
					sensor: false
			.then (response) ->
				response.data.results.map( (item) -> item.formatted_address )
		
		$scope.submit = search
			
		$scope.logout = service.logout
		]
	
	app.init = -> angular.bootstrap document, [ 'app' ]

	return app
	
#				console.log JSON.stringify(
