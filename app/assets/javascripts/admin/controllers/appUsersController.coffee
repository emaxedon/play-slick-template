define [ 'appModule' ], (app) ->
	app.controller 'UsersController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.matches = 0
		$scope.users = []
		$scope.email = ""
		$scope.clear()
		service.loggedin()
		
		search = ->
			if $scope.email.length > 0
				$http.get '/auth/search',
					params:
						q: $scope.email
				.success (data, status, headers, config) ->
					if data != null
						$scope.matches = data.users.length
						$scope.users = data.users
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
			$scope.clear()

			$http.get('/auth/remove/' + $scope.userId)
				.success (data, status, headers, config) ->
					$scope.form = false
					$scope.success 'Successfully deleted user.'
					search()
		
		$scope.create = ->
			$scope.userDetails =
				"email": ""
				"password": ""
				"role": "user"
				"location": ""
			$scope.disabled = false
			$scope.clear()
			$scope.form = 'create'

		$scope.cancel = -> 
			$scope.form = false
			$scope.clear()
		
		$scope.edit = (id) ->
			$scope.clear()

			$http.get('/auth/user/' + id)
				.success (data, status, headers, config) ->
					if data != null
						$scope.userId = id
						$scope.userDetails =
							"email": data.email
							"role": data.role
							"location": data.location
						$scope.disabled = false
						$scope.form = 'edit'
			
		$scope.formSubmit = ->
			$scope.disabled = true
			$scope.clear()

			if $scope.form == 'edit'
				$http.put('/auth/user/' + $scope.userId, $scope.userDetails )
					.success (data, status, headers, config) ->
						$scope.disabled = false
						$scope.success 'Successfully updated user.'
						search()
					.error (data, status, headers, config) ->
						$scope.error 'Oops! Problem editing user. Try again.'
			else
				$http.post( '/auth/create', $scope.userDetails )
					.success (data, status, headers, config) ->
						if data != null
							$scope.form = false
							$scope.disabled = false
							$scope.success 'Successfully created new user.'
							search()
						else
							$scope.error data
							$scope.disabled = false
					.error (data, status, headers, config) ->
						$scope.error 'Oops! Problem creating user. Try again.'

		$scope.getLocation = (val) ->
			$http.get 'http://maps.googleapis.com/maps/api/geocode/json',
				params:
					address: val
					sensor: false
			.then (response) ->
				response.data.results.map( (item) -> item.formatted_address )

		$scope.submit = search
		]