window.google = window.google || {};
google.maps = google.maps || {};
(function() {

    function getScript(src) {
        document.write('<' + 'script src="' + src + '"' +
                ' type="text/javascript"><' + '/script>');
    }

    var modules = google.maps.modules = {};
    google.maps.__gjsload__ = function(name, text) {
        modules[name] = text;
    };

    google.maps.Load = function(apiLoad) {
        delete google.maps.Load;
        apiLoad([0.009999999776482582, [[["http://mt0.googleapis.com/vt?lyrs=m@264000000\u0026src=api\u0026hl=en-US\u0026", "http://mt1.googleapis.com/vt?lyrs=m@264000000\u0026src=api\u0026hl=en-US\u0026"], null, null, null, null, "m@264000000", ["https://mts0.google.com/vt?lyrs=m@264000000\u0026src=api\u0026hl=en-US\u0026", "https://mts1.google.com/vt?lyrs=m@264000000\u0026src=api\u0026hl=en-US\u0026"]], [["http://khm0.googleapis.com/kh?v=150\u0026hl=en-US\u0026", "http://khm1.googleapis.com/kh?v=150\u0026hl=en-US\u0026"], null, null, null, 1, "150", ["https://khms0.google.com/kh?v=150\u0026hl=en-US\u0026", "https://khms1.google.com/kh?v=150\u0026hl=en-US\u0026"]], [["http://mt0.googleapis.com/vt?lyrs=h@264000000\u0026src=api\u0026hl=en-US\u0026", "http://mt1.googleapis.com/vt?lyrs=h@264000000\u0026src=api\u0026hl=en-US\u0026"], null, null, null, null, "h@264000000", ["https://mts0.google.com/vt?lyrs=h@264000000\u0026src=api\u0026hl=en-US\u0026", "https://mts1.google.com/vt?lyrs=h@264000000\u0026src=api\u0026hl=en-US\u0026"]], [["http://mt0.googleapis.com/vt?lyrs=t@132,r@264000000\u0026src=api\u0026hl=en-US\u0026", "http://mt1.googleapis.com/vt?lyrs=t@132,r@264000000\u0026src=api\u0026hl=en-US\u0026"], null, null, null, null, "t@132,r@264000000", ["https://mts0.google.com/vt?lyrs=t@132,r@264000000\u0026src=api\u0026hl=en-US\u0026", "https://mts1.google.com/vt?lyrs=t@132,r@264000000\u0026src=api\u0026hl=en-US\u0026"]], null, null, [["http://cbk0.googleapis.com/cbk?", "http://cbk1.googleapis.com/cbk?"]], [["http://khm0.googleapis.com/kh?v=84\u0026hl=en-US\u0026", "http://khm1.googleapis.com/kh?v=84\u0026hl=en-US\u0026"], null, null, null, null, "84", ["https://khms0.google.com/kh?v=84\u0026hl=en-US\u0026", "https://khms1.google.com/kh?v=84\u0026hl=en-US\u0026"]], [["http://mt0.googleapis.com/mapslt?hl=en-US\u0026", "http://mt1.googleapis.com/mapslt?hl=en-US\u0026"]], [["http://mt0.googleapis.com/mapslt/ft?hl=en-US\u0026", "http://mt1.googleapis.com/mapslt/ft?hl=en-US\u0026"]], [["http://mt0.googleapis.com/vt?hl=en-US\u0026", "http://mt1.googleapis.com/vt?hl=en-US\u0026"]], [["http://mt0.googleapis.com/mapslt/loom?hl=en-US\u0026", "http://mt1.googleapis.com/mapslt/loom?hl=en-US\u0026"]], [["https://mts0.googleapis.com/mapslt?hl=en-US\u0026", "https://mts1.googleapis.com/mapslt?hl=en-US\u0026"]], [["https://mts0.googleapis.com/mapslt/ft?hl=en-US\u0026", "https://mts1.googleapis.com/mapslt/ft?hl=en-US\u0026"]], [["https://mts0.googleapis.com/mapslt/loom?hl=en-US\u0026", "https://mts1.googleapis.com/mapslt/loom?hl=en-US\u0026"]]], ["en-US", "US", null, 0, null, null, "http://maps.gstatic.com/mapfiles/", "http://csi.gstatic.com", "https://maps.googleapis.com", "http://maps.googleapis.com"], ["http://maps.gstatic.com/intl/en_us/mapfiles/api-3/17/3", "3.17.3"], [4129959954], 1, null, null, null, null, null, "", null, null, 0, "http://khm.googleapis.com/mz?v=150\u0026", null, "https://earthbuilder.googleapis.com", "https://earthbuilder.googleapis.com", null, "http://mt.googleapis.com/vt/icon", [["http://mt0.googleapis.com/vt", "http://mt1.googleapis.com/vt"], ["https://mts0.googleapis.com/vt", "https://mts1.googleapis.com/vt"], [null, [[0, "m", 264000000]], [null, "en-US", "US", null, 18, null, null, null, null, null, null, [[47], [37, [["smartmaps"]]]]], 0], [null, [[0, "m", 264000000]], [null, "en-US", "US", null, 18, null, null, null, null, null, null, [[47], [37, [["smartmaps"]]]]], 3], [null, [[0, "m", 264000000]], [null, "en-US", "US", null, 18, null, null, null, null, null, null, [[50], [37, [["smartmaps"]]]]], 0], [null, [[0, "m", 264000000]], [null, "en-US", "US", null, 18, null, null, null, null, null, null, [[50], [37, [["smartmaps"]]]]], 3], [null, [[4, "t", 132], [0, "r", 132000000]], [null, "en-US", "US", null, 18, null, null, null, null, null, null, [[5], [37, [["smartmaps"]]]]], 0], [null, [[4, "t", 132], [0, "r", 132000000]], [null, "en-US", "US", null, 18, null, null, null, null, null, null, [[5], [37, [["smartmaps"]]]]], 3], [null, null, [null, "en-US", "US", null, 18], 0], [null, null, [null, "en-US", "US", null, 18], 3], [null, null, [null, "en-US", "US", null, 18], 6], [null, null, [null, "en-US", "US", null, 18], 0], ["https://mts0.google.com/vt", "https://mts1.google.com/vt"], "/maps/vt"], 2, 500, ["http://geo0.ggpht.com/cbk?cb_client=maps_sv.uv_api_demo", "http://www.gstatic.com/landmark/tour", "http://www.gstatic.com/landmark/config", "/maps/preview/reveal?authuser=0", "/maps/preview/log204", "/gen204?tbm=map", "http://static.panoramio.com.storage.googleapis.com/photos/"]], loadScriptTime);
    };
    var loadScriptTime = (new Date).getTime();
    getScript("http://maps.gstatic.com/intl/en_us/mapfiles/api-3/17/3/main.js");
})();

map = {};
var map;
map.showMap = function(latitude, longitude, addr) {
    var googleLatandLong = new google.maps.LatLng(latitude, longitude);
    var mapOptions = {
        zoom: 17,
        center: googleLatandLong,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        language: 'VN'
    };

    var mapDiv = document.getElementById("map");
    map = new google.maps.Map(mapDiv, mapOptions);

    var title = "Server Location";
    addMarker(map, googleLatandLong, title, addr);

}

function addMarker(map, latlong, title, content) {

    var markerOptions = {
        position: latlong,
        map: map,
        title: title,
        clickable: true,
        animation: google.maps.Animation.DROP
    };
    var marker = new google.maps.Marker(markerOptions);
    var infowindow = new google.maps.InfoWindow({
        content: content
    });
    infowindow.open(map, marker);
}