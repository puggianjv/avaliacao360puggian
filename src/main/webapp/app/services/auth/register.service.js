(function () {
    'use strict';

    angular
        .module('avaliacao360PuggianApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
