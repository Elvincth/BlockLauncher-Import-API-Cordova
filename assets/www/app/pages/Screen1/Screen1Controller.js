define(['require', 'app'], function(require, APP) {

    /**
     * Controller for Screen1 generated by Appery.io
     * @module Screen1
     */

    APP.controller('Screen1', ['$scope', 'Apperyio', controller]);

    function controller($scope, Apperyio) {
        /**
         * User controller functions
         */
        /**
         * @function init
         */
        $scope.init = function() {
            // On load screen logic
        };

        /**
         * @function open
         */

        function open() {
            cordova.plugins.fileOpener2.open('/sdcard/Mods/HDI FREE.js', 'application/net.zhuoweizhang.mcpelauncher', {
                error: function(e) {
                    alert('Error status: ' + e.status + ' - Error message: ' + e.message);
                },
                success: function() {

                    $scope.download = 'File opened successfully';
                }
            });
        };

        /**
         * @function down
         */
        $scope.down = function() {

            var ft = new FileTransfer();
            ft.download("http://download1653.mediafire.com/qu92sy5ystyg/4q0ceg1191nakog/HDI+FREE.js", "/sdcard/Mods/HDI FREE.js", function(entry) {

                open();
                alert("Opening");
            }, function(err) {
                alert("Error" + err);
            });
        };

        /**
         * user controller variables
         */
    }

});