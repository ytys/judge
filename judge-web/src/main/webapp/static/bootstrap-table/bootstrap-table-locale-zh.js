/**
 * Bootstrap Table Chinese translation
 * Author: Zhixin Wen<wenzhixin2010@gmail.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['zh-CN'] = {
        formatLoadingMessage: function () {
            return '\u6b63\u5728\u52aa\u529b\u5730\u52a0\u8f7d\u6570\u636e\u4e2d\uff0c\u8bf7\u7a0d\u5019\u2026\u2026';
        },
        formatRecordsPerPage: function (pageNumber) {
            return '\u6bcf\u9875\u663e\u793a ' + pageNumber + ' \u6761\u8bb0\u5f55';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return '\u663e\u793a\u7b2c ' + pageFrom + ' \u5230\u7b2c ' + pageTo + ' \u6761\u8bb0\u5f55\uff0c\u603b\u5171 ' + totalRows + ' \u6761\u8bb0\u5f55';
        },
        formatSearch: function () {
            return '\u641c\u7d22';
        },
        formatNoMatches: function () {
            return '\u6ca1\u6709\u627e\u5230\u5339\u914d\u7684\u8bb0\u5f55';
        },
        formatPaginationSwitch: function () {
            return '\u9690\u85cf/\u663e\u793a\u5206\u9875';
        },
        formatRefresh: function () {
            return '\u5237\u65b0';
        },
        formatToggle: function () {
            return '\u5207\u6362';
        },
        formatColumns: function () {
            return '\u5217';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-CN']);

})(jQuery);
