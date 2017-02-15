(function() {
    'use strict';

    angular
        .module('avaliacao360PuggianApp')
        .factory('EquipeSearch', EquipeSearch);

    EquipeSearch.$inject = ['$resource'];

    function EquipeSearch($resource) {
        var resourceUrl =  'api/_search/equipes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
