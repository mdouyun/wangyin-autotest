(function($) {
    $.utils = {
        isEmpty : function(str) {
            if (str == null || str == "" || str == undefined) {
                return true;
            } else {
                return false;
            }
        },
        trim : function(str) {
            var result = str.replace(/(^\s+)|(\s+$)/g,"");
            result = result.replace(/\s/g,"");
            return result;
        },
        /**
         * str是否包含indexStr
         * @param str
         * @param indexStr
         * @returns {boolean}
         */
        contain : function(str, indexStr) {
            str = $.utils.trim(str);
            indexStr = $.utils.trim(indexStr);

            if(str.indexOf(indexStr) >= 0 ) {
                return true;
            } else {
                return false;
            }
        },
        toString : function(domer) {
            return $($('<div></div>').html(domer.clone())).html();
        },
        refreshPage : function() {
            window.location.reload(true);
        },
        reloadPage : function() {
            window.location.replace(window.location.href);
        },
        openPage : function(url) {
            window.open(url);
        },
        toUrl : function(url) {
            window.location.href = url;
        }
    }

})(jQuery);