# BlockLauncher Import API in Phonegap
#Example code
Import this plugin: https://github.com/pwlin/cordova-plugin-file-opener2

$scope.download = function () {

$scope.download = 'Downloading...';

var ft = new FileTransfer();
ft.download(
 "http://download1653.mediafire.com/qu92sy5ystyg/4q0ceg1191nakog/HDI+FREE.js",
 "/sdcard/Mods/HDI FREE.js",
  function(entry) {
$scope.download = 'Download success!';
      open();
      $scope.download = 'Opening...';
  },
  function(err) {
      $scope.download = 'Download error! '+err;
  }
);

}

function open() {
cordova.plugins.fileOpener2.open(
    '/sdcard/Mods/HDI FREE.js',
    'application/net.zhuoweizhang.mcpelauncher', 
    { 
        error : function(e) { 
         alert('Error status: ' + e.status + ' - Error message: ' + e.message);
        },
        success : function () {

  $scope.download = 'File opened successfully';                
        }
    }
);



}
