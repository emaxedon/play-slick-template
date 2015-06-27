define [ 'appModule' ], (app) ->
	app.controller 'UsersController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.matches = 0
		$scope.users = []
		$scope.email = ""
		service.loggedin()
		
		search = ->
			if $scope.email.length > 0
				$http.get '/auth/search',
					params:
						q: $scope.email
				.success (data, status, headers, config) ->
					if data.result == 1
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
			$scope.error = false
			$scope.message = false

			$http.get('/auth/remove/' + $scope.userId)
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.form = false
						$scope.message = "Successfully deleted user."
						search()
		
		$scope.create = ->
			$scope.userDetails =
				"email": ""
				"password": ""
				"role": "user"
				"location": ""
			$scope.disabled = false
			$scope.error = false
			$scope.message = false
			$scope.form = 'create'

		$scope.cancel = -> 
			$scope.form = false
			$scope.error = false
			$scope.message = false
		
		$scope.edit = (id) ->
			$scope.error = false
			$scope.message = false
			$http.get('/auth/user/' + id)
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.userId = id
						$scope.userDetails =
							"email": data.data.email
							"role": data.data.role
							"location": data.data.location
						$scope.disabled = false
						$scope.form = 'edit'
			
		$scope.formSubmit = ->
			$scope.disabled = true
			$scope.error = false
			$scope.message = false

			if $scope.form == 'edit'
				$http.put('/auth/user/' + $scope.userId, $scope.userDetails )
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.disabled = false
							$scope.message = "Successfully updated user."
							search()
						else
							$scope.error = data.message
							$scope.disabled = false
					.error (data, status, headers, config) ->
						$scope.error = "Oops! Problem editing user. Try again."
			else
				$http.post( '/auth/create', $scope.userDetails )
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.form = false
							$scope.disabled = false
							$scope.message = "Successfully created new user."
							search()
						else
							$scope.error = data.message
							$scope.disabled = false
					.error (data, status, headers, config) ->
						$scope.error = "Oops! Problem creating user. Try again."

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