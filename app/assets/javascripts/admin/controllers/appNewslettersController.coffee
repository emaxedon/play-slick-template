define [ 'appModule' ], (app) ->

	app.controller 'NewslettersController', ['$scope', '$http', 'service', ($scope, $http, service) ->
		$scope.error = false
		$scope.message = false
		$scope.info = false
		$scope.newsletters = []
		service.loggedin()

		fetch = ->
			$http.get '/newsletters'
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.newsletters = data.data

		$scope.editorOptions =
		    language: 'en'

		$scope.create = ->
			$scope.newsletterDetails =
				"subject": ""
				"text": ""
				"status": 0
			$scope.disabled = false
			$scope.error = false
			$scope.message = false
			$scope.info = false
			$scope.form = 'create'

		$scope.cancel = -> 
			$scope.form = false
			$scope.error = false
			$scope.message = false
			$scope.info = false

		$scope.edit = (id) ->
			$scope.error = false
			$scope.message = false
			$scope.info = false

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
			$scope.error = false
			$scope.message = false
			$scope.disabled = true
			$scope.info = "Sending newsletter to all users..."

			$http.post( '/newsletters/send/' + $scope.newsletterId, $scope.newsletterDetails )
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.status = data.data.status
						$scope.disabled = false
						$scope.info = false
						$scope.message = "Successfully sent newsletter."
						fetch()
					else
						$scope.error = "Oops! Problem sending newsletter. Please try again later."
						$scope.disabled = false
						$scope.info = false 
				.error (data, status, headers, config) ->
					$scope.error = "Oops! Problem sending newsletter. Please try again later."
					$scope.disabled = false
					$scope.info = false

		$scope.remove = ->
			$scope.error = false
			$scope.message = false
			$scope.info = false

			$http.get('/newsletters/remove/' + $scope.newsletterId)
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.form = false
						$scope.message = "Successfully deleted newsletter."
						fetch()

		$scope.formSubmit = ->
			$scope.disabled = true
			$scope.error = false
			$scope.message = false
			$scope.info = false
			
			if $scope.form == 'edit'
				$http.put('/newsletters/' + $scope.newsletterId, $scope.newsletterDetails )
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.disabled = false
							$scope.message = "Successfully updated newsletter."
							fetch()
						else
							$scope.error = data.message
					.error (data, status, headers, config) ->
						$scope.error = "Oops! Problem editing newsletter. Please try again."
						$scope.disabled = false
			else
				$http.post( '/newsletters/create', $scope.newsletterDetails )
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.disabled = false
							$scope.message = "Successfully created new newsletter."
							fetch()
							$scope.edit(data.data.id)
						else
							$scope.disabled = false
							$scope.error = data.message
					.error (data, status, headers, config) ->
						$scope.error = "Oops! Problem adding newsletter. Please try again."
						$scope.disabled = false

		fetch()
		]