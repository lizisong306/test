   !function(e) {
        function t() {
            var e = o.getBoundingClientRect().width;
            e > 640 && (e = 640);
            var t = e / 7.5;
            o.style.fontSize = t + "px", i.rem = t
        }

        function n() {
            clearTimeout(r), r = setTimeout(t,0)
        }

        var r, i = {}, o = e.document.documentElement;
        e.addEventListener("resize", function() {
            n()
        }, !1), e.addEventListener("pageshow", function(e) {
            e.persisted && n()
        }, !1), t(), i.refreshRem = t, i.rem2px = function(e) {
            var t = parseFloat(e) * this.rem;
            return "string" == typeof e && e.match(/rem$/) && (t += "px"), t
        }, i.px2rem = function(e) {
            var t = parseFloat(e) / this.rem;
            return "string" == typeof e && e.match(/px$/) && (t += "rem"), t
        }, e.remCalc = i
    }(window);