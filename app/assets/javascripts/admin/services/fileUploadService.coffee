define [ 'appModule' ], (app) ->
	app.service 'fileUpload', ['$http', ($http) ->
		this.uploadFileToUrl = (file, uploadUrl) ->
			fd = new FormData()
			fd.append('file', file)
			$http.post uploadUrl, fd,
				transformRequest: angular.identity
				headers: {'Content-Type': undefined} #'multipart/form-data'
			.success (data, status, headers, config) ->
				console.log 'success'
			.error (data, status, headers, config) ->
				console.log 'error'
	]