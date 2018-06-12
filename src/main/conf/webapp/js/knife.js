function submitForm(id, successFunc) {

    var option = {
        data : null,
        dataType : "json",
        error : function() {
            $.alert.Error("提交请求出错，请稍后再试！");
        },
        success : function(result) {
            var success = result.success;
            var msg = result.error;
            var data = result.data;

            if (success || success == "true") {
                successFunc(data);
            } else {
                $.alert.Error(msg);
            }
        }
    };

    $("#" + id).ajaxSubmit(option);
}


function submitRequest(url, data, successFunc) {
    $.ajax({
        url: url,
        type : "POST",
        data: data,
        dataType: 'json',
        success: function(data) {
            if(data.success || data.success == "true") {
                successFunc(data.data);
            } else {
                $.alert.Error(data.error);
            }
        },
        error: function(){
            $.alert.Error("提交请求出错，请稍后再试！");
        }
    });
}







