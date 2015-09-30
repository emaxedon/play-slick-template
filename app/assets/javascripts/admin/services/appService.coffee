define [ 'appModule' ], (app) ->
	app.factory 'service', ['$http', '$location', ($http, $location) ->
		logout: ->
			$http.get('/auth/logout')
				.success (data, status, headers, config) ->
					$location.path '/'
		loggedin: ->
			$http.get('/auth/user')
				.success (data, status, headers, config) ->
					if (data == null)
						$location.path '/'
		user: (success, error) ->
			$http.get('/auth/user')
				.success (data, status, headers, config) -> success(data)			
				.error (data, status, headers, config) -> error(status)
		]