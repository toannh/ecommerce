var property = {};
property.collor = {
    "xanh-nuoc-bien": "#00A0FF",
    "xanh-den": "#002199",
    "xanh-non-chuoi": "#00FF6D",
    "xanh-ngoc": "#33CC99",
    "xanh-la": "#009900",
    "do-tuoi": "#FF0000",
    "do-tham": "#990000",
    "mau-cam": "#FF6600",
    "hong": "#FF00CC",
    "hong-nhat": "#FFCCFF",
    "tim": "#580092",
    "tim-than": "#000066",
    "nau": "#770500",
    "nau-dam": "#400A00",
    "xam": "#999999",
    "dong": "#FFCC66",
    "mau-kem": "#FFCC99",
    "trang": "#FFFFFF",
    "vang": "#FFFF00",
    "vang-nhat": "#F6FF99",
    "den": "#000000"
}
property.getCollor = function (code) {
    var col = property.collor[code.toLocaleString()];
    if (typeof col != 'undefined')
        return col;
    else
        return null;
};


