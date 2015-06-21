define [ 'appModule' ], (app) ->

	app.controller 'ForgotPasswordController', ['$scope', '$http', ($scope, $http) ->
		$scope.form = 'forgot'

		$scope.submit = ->
			$scope.disabled = true
			$scope.info = "Sending password reset email..."
			$scope.message = false
			$scope.error = false
			$http.post('/auth/forgotPassword', {email: $scope.email})
				.success (data, status, headers, config) ->
					if (data.result == 1)
						$scope.info = false
						$scope.message = "Password reset email has been sent"
						$scope.form = false
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
