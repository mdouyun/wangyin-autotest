(function ($) {

    function AlertDialog(options) {

        var defaults = {
            id: "",
            message : "",
            cls : "",
            btn : [{
                text : "确定",
                cls : "btn btn-primary",
                close : true
            }]
        };

        this.options = $.extend(defaults, options);

        if ($("#" + options.id).length <= 0) {
            var dialog = $('<div id="' + this.options.id + '" tabindex="-1" class="modal fade" aria-hidden="true" role="dialog">' +
                '<div class="modal-dialog w400"><div class="modal-content"></div></div></div>');

            dialog = dialog.find(".modal-content");

            dialog.append('<div class="modal-body"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
                '<div class="mb0 body-content"></div></div>');
            dialog.append('<div class="modal-footer"></div>');

            dialog.parents("#" + this.options.id).appendTo("body");

            $("#" + this.options.id).modal({
                backdrop: 'static',
                keyboard: false,
                show: false
            });
            $("#" + this.options.id).modal("hide");
        }

        $("#" + this.options.id + "  .modal-footer").empty();

        var id = this.options.id;
        $("#" + id).unbind("hidden.bs.modal");
        $.each(this.options.btn, function(i, n) {
            var btn = $('<button type="button"></button>');
            btn.attr("class", n.cls);
            btn.html(n.text);

            btn.unbind("click");

            if (!$.utils.isEmpty(n.click) && n.close == true) {
                btn.on("click", function() {
                    $("#" + id).modal("hide");
                    $("#" + id).on("hidden.bs.modal", n.click);
                });
            } else if(n.close == true) {
                btn.attr("data-dismiss", "modal");
            }

            $("#" + id + " .modal-footer").append(btn);
        });

        $("#" + this.options.id + " .modal-body .body-content").html("");
        $("#" + this.options.id + " .modal-body .body-content").attr("class", "mb0 body-content");
        $("#" + this.options.id + " .modal-body .body-content").html(this.options.message);
        $("#" + this.options.id + " .modal-body .body-content").addClass(this.options.cls);
    };

    AlertDialog.prototype.hide = function () {
        $("#" + this.options.id).modal("hide");
    };

    AlertDialog.prototype.show = function () {
        $("#" + this.options.id).modal("show");
    };

    $.alert = {
        Info : function(text, click) {
            var options = new Object();
            options.message = text;
            options.cls = "tip-info";
            options.click = click;

            $.alert.init(options);
        },
        Error : function(text, click) {
            var options = new Object();
            options.message = text;
            options.cls = "tip-danger";
            options.click = click;

            $.alert.init(options);
        },
        Success : function(text, click) {
            var options = new Object();
            options.message = text;
            options.cls = "tip-success";
            options.click = click;

            $.alert.init(options);
        },
        init : function(options) {
            options = {
                id: "_alert_dialog",
                message : options.message,
                cls : options.cls,
                btn : [{
                    text : "确定",
                    cls : "btn btn-primary",
                    close : true,
                    click : options.click
                }]
            };

            var alert = new AlertDialog(options);
            alert.show();
        }
    };

    $.confirm = function(text, click) {
        var options = {
            id: "_alert_dialog",
            message : text,
            cls : "tip-info",
            btn : [{
                text : "确定",
                cls : "btn btn-primary",
                close : true,
                click : click
            }, {
                text : "取消",
                cls : "btn btn-default",
                close : true
            }]
        };

        var alert = new AlertDialog(options);
        alert.show();
    }

})(jQuery);