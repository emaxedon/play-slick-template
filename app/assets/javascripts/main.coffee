require.config
  baseUrl: '/assets'

  shim: 
    'bootstrap': deps: [ 'jquery' ]

  paths:
    'jquery': 'lib/jquery/jquery.min'
    'bootstrap': 'lib/bootstrap/js/bootstrap.min'

require [
  'jquery'
], ($) ->
  $(document).ready ->
    $('#form-reset').on 'submit', ->
      password = $('#form-reset #inputPassword').val()
      confirmPassword = $('#form-reset #inputConfirmPassword').val()

      if (password == confirmPassword)
        $.ajax
          url: '/auth/password'
          type: 'PUT'
          contentType: 'application/json'
          dataType: 'json'
          data: JSON.stringify({ password: password })
          success: (data) ->
            console.log('password updated')

      return false