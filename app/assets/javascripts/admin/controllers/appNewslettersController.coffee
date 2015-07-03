define [ 'appModule' ], (app) ->

	app.controller 'NewslettersController', ['$scope', '$http', 'service', ($scope, $http, service) ->
		$scope.message = none: ''
		$scope.newsletters = []
		service.loggedin()

		$scope.key = -> Object.keys($scope.message)[0]
		
		fetch = ->
			$http.get '/newsletters'
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.newsletters = data.data

		$scope.create = ->
			$scope.newsletterDetails =
				"subject": ""
				"text": ""
				"status": 0
			$scope.disabled = false
			$scope.message = none: ''
			$scope.form = 'create'

		$scope.cancel = -> 
			$scope.form = false
			$scope.message = none: ''

		$scope.edit = (id) ->
			$scope.message = none: ''

			$http.get('/newsletters/' + id)
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.status = data.data.status
						$scope.newsletterId = id
						$scope.newsletterDetails =
							"subject": data.data.subject
							"text": data.data.text
							"status": data.data.status
						$scope.disabled = false
						$scope.form = 'edit'

		$scope.send = ->
			$scope.disabled = true
			$scope.message = info: 'Sending newsletter to all users...'

			$http.post( '/newsletters/send/' + $scope.newsletterId, $scope.newsletterDetails )
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.status = data.data.status
						$scope.disabled = false
						$scope.message = success: 'Successfully sent newsletter.'
						fetch()
					else
						$scope.message = error: 'Oops! Problem sending newsletter. Please try again later.'
						$scope.disabled = false
				.error (data, status, headers, config) ->
					$scope.message = error: 'Oops! Problem sending newsletter. Please try again later.'
					$scope.disabled = false

		$scope.remove = ->
			$scope.message = none: ''

			$http.get('/newsletters/remove/' + $scope.newsletterId)
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.form = false
						$scope.message = success: 'Successfully deleted newsletter.'
						fetch()

		$scope.formSubmit = ->
			$scope.disabled = true
			$scope.message = none: ''
			
			if $scope.form == 'edit'
				$http.put('/newsletters/' + $scope.newsletterId, $scope.newsletterDetails )
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.disabled = false
							$scope.message = success: 'Successfully updated newsletter.'
							fetch()
						else
							$scope.message = error: data.message
					.error (data, status, headers, config) ->
						$scope.message = error: 'Oops! Problem editing newsletter. Please try again.'
						$scope.disabled = false
			else
				$http.post( '/newsletters/create', $scope.newsletterDetails )
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.disabled = false
							$scope.message = success: 'Successfully created new newsletter.'
							fetch()
							$scope.edit(data.data.id)
						else
							$scope.disabled = false
							$scope.message = error: data.message
					.error (data, status, headers, config) ->
						$scope.message = error: 'Oops! Problem adding newsletter. Please try again.'
						$scope.disabled = false

		fetch()
		]