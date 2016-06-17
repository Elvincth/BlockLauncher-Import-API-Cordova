# BlockLauncher Import API in Phonegap

Test project for BlockLauncher's public API using Ionic, that is easy for develoepr that do not know Java.

#How to add your own import code
//Import this plugin: https://github.com/pwlin/cordova-plugin-file-opener2
//Code:
$scope.download = function () {



var ft = new FileTransfer();
ft.download(
 "http://download1653.mediafire.com/qu92sy5ystyg/4q0ceg1191nakog/HDI+FREE.js",
 "/sdcard/Mods/HDI FREE.js",
  function(entry) {
$scope.download = 'Download success!';
      open();

  },
  function(err) {

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
               
        }
    }
);



}
