define [ 'appModule' ], (app) ->

	app.controller 'ForgotPasswordController', ['$scope', '$http', ($scope, $http) ->
		$scope.form = 'forgot'

		$scope.submit = ->
			$scope.disabled = true
			$scope.info = "Sending password reset email..."
			$scope.clear()

			$http.put('/auth/forgotPassword', {email: $scope.email})
				.success (data, status, headers, config) ->
					if (data.result == 1)
						$scope.success 'Password reset email has been sent'
						$scope.form = false
					else
						$scope.error data.message
						
					$scope.disabled = false
				.error (data, status, headers, config) ->
					$scope.error 'Error resetting password - please try again'
					$scope.disabled = false
		]
