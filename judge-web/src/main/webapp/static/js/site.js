/*!
 * Copyright 2015 zhanhb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function (window, $, undefined) {
    $(function () {
        var offset = 220;
        var duration = 500;
        var selector = ".back-to-top";
        var element = $(selector);

        element.length || (element = $("<span class='back-to-top'></span>").appendTo("body"));

        $(window).scroll(function () {
            element[$(this).scrollTop() > offset ? "fadeIn" : "fadeOut"](duration);
        });

        element.empty().append("<div class='before'></div><div class='after'></div>").click(function () {
            $('html, body').animate({scrollTop: 0}, duration);
            return false;
        });
    });
    $(function () {
        function doPlaceHolder() {
            $(this).placeholder({customClass: customClass});
        }

        var customClass = "placeholder";

        var placeholder = $.fn.placeholder;
        var document = this;
        var selector;

        if (placeholder) {
            if (placeholder.input && placeholder.textarea) {

            } else {
                selector = placeholder.input ? "textarea" : ":input";
                $(document).find(selector).each(doPlaceHolder);
                $('<style type="text/css" media="screen">.' + customClass + '{color:#AAA}</style>').appendTo($("head")[0] || "body");
            }
        }
    });

    $.extend($.fn.bootstrapTable.defaults, {
        undefinedText: "N/A",
        responseHandler: function (res) {
            return $.extend(res, {
                rows: res.content,
                total: res.page && +res.page.totalElements || res.totalElements || 0
            });
        }, queryParams: function (params) {
            return {
                sort: params.sort && (params.sort + "," + params.order) || params.sortName && (params.sortName + "," + params.sortOrder),
                size: +params.limit || +params.pageSize || undefined,
                page: ((params.offset / params.limit + 1) || +params.pageNumber || 1) - 1
            };
        }
    });

    var QRCode = window.QRCode;

    QRCode && ($.fn.qrcode = function (options) {
        $.each(this, function () {
            var me = $(this), qrcode;
            typeof options === "string" && (qrcode = me.data("qrcode")) ? qrcode.makeCode(options) :
                    me.data("qrcode", new QRCode(me.empty()[0], options));
        });
        return this;
    });
})(this, jQuery);
