define [ 'appModule' ], (app) ->
	
	app.controller 'LoginController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		service.user ((data) ->
			if data.result == 1
				$location.path '/dash'
			else
				$scope.show = true
		), (status) ->
			$scope.show = true

		$scope.submit = ->
			$scope.clear()
			$scope.disabled = true

			$http.post('/auth/login', {email: $scope.email, password: $scope.password})
				.success (data, status, headers, config) ->
					$scope.disabled = false
					
					if (data.result == 1)
						if (data.data.role != "admin")
							$scope.error 'Oops! Not an administrator account.'
							service.logout()
						else
							$location.path '/dash'
					else
						$scope.error data.message
				.error (data, status, headers, config) ->
					$scope.error 'Error logging in - please try again.'
		]