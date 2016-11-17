/*
 concat from: rrloan/modules/review/js/mortgage-save.js
*/

 /*
 * @name: 车辆置换
 * @author: xuejin.xu@renren-inc.com
 * @date: 2015-08-10
 */

/*在自己的js逻辑里, 做上传图片后回调函数处理
 * window.handlePhotoData = function(r) {
 *
 * }
*/

(function($) {
  var dm = 'http://' + rrloan.env.subdomain + '.' + rrloan.env.domain;
    $.widget('ui.fileUpload', {

    options: {
       formUrl: dm+'/upload/uploadphoto',
       fileType: 'single',
       isiframe: 1,
       _rtk: rrloan.user._rtk,
       // 主要是为了显示图片，页面上的img元素id和这个一致
       img: 'file',
       // 是否做'加载中'
       isLoading: false
    },
   
    _create: function() {
        this._events();     
    },

    _init: function() {

    },

    _concatStr: function(){
        return this.options.formUrl+'?uploadid='+this.options.fileType+'_'+this.options.img+'_'+(+(new Date))+'&isiframe='+this.options.isiframe+'&_rtk='+this.options._rtk;
    },

    _events: function() {
        var This = this,
            url = this._concatStr();
        this.element.delegate('input[type=file]', 'change', function() {
              This.element.find('form').attr('action', url);
          if (This.options.isLoading) {
              showLoading(This.element);
          }
            This.element.find('form').submit();  
          $(this).after($(this).clone().val(''));
          $(this).remove();   
        });
    }
})

})(jQuery);


// 提交成功提示框
$.widget('dialog.merchantSuccess', $.ui.dialog, {
    _createWidget: function(msg,obj) {
        this._super({
            beforeClose: function() {
                $(this).dialog('instance').destroy();
            },
            buttons: [
                {
                    'text': '确定',
                    'class': 'ui-button-blue',
                    'click': function (e) {
                        // var pfDom = document.getElementById('procedureFrame');
                        // if (pfDom) {
                        //  pfDom.src = pfDom.src;
                        // } else {
                     //     $(obj).data('handle','1');
                        //  window.location.reload();
                        // }
                        var domain = 'http://' + rrloan.env.subdomain + '.' + rrloan.env.domain;
                        window.location.href = domain + '/carmortgage/swap/patch';
                        // window.location.reload();

                        $(this).dialog('instance').destroy();
                    }
                }
            ],
        width: 500,
        height: 200,
        dialogClass: 'ui-dialog-tip-SucErr',
        title: '提示',
        minHeight: 0
        }, '<div class="success">' + msg + '</div>');
    }
});

// 错误提示框
$.widget('error.dialog', $.ui.dialog, {
    _createWidget: function(msg,obj) {
        this._super({
            beforeClose: function() {
                $(this).dialog('instance').destroy();
            },
            buttons: [
                {
                    'text': '确定',
                    'class': 'ui-button-blue',
                    'click': function (e) {
                        // var pfDom = document.getElementById('procedureFrame');
                        // if (pfDom) {
                        //  pfDom.src = pfDom.src;
                        // } else {
                     //     $(obj).data('handle','1');
                        //  window.location.reload();
                        // }
                        $(this).dialog('instance').destroy();
                    }
                }
            ],
        width: 500,
        height: 200,
        dialogClass: 'ui-dialog-tip-SucErr',
        title: '提示',
        minHeight: 0
        }, '<div class="success">' + msg + '</div>');
    }
});

$(function () {
    var domain = 'http://' + rrloan.env.subdomain + '.' + rrloan.env.domain,
        picUrl = 'http://fmn.rrfmn.com/',   //上传照片的域名
        uploadUrlArr = [],                  //上传照片URL数组，元素为Object{imgid:url}
        uploadDomArr = [],                  //上传照片DOM数组
        notUploadClass = 'unupload',      //未上传类名
        i = 0,                              //计数器变量
        l = 0,                             //计数器上限变量
        j = 0,                             //计数器上限变量
        oInput = $('.input-text'),
        oSelect = $('.select-wrap select'),
        oImg = $('.upload-form').find('img'),
        oHiddenImg = $('.img-input'),
        rtk = rrloan.user._rtk,
        oTitle = $('.auto-info-title'),
        lineIndex = '',
        editBtn = $('.edit'),
        infoItem = $('.rv-car-wrap'),
        urls = {
            carInfosubmit: '/risk/caruser/save'  //提交汽车信息
        },
        regNum = /^\d+(\.\d+)?$/, //限制输入类型
        bPass = true,
        errorTip = '',
        swapId = $("#global_swapId").val(),
        listNum = 0 ;

    (function init() {
        // 上传车辆照片
        $(".auto-info-item-edit").each(function(){
            var serialNo = $(this).attr('serialNo');
            commonOldUpload(serialNo);
            //$('#car_item_' + serailNo + "0").fileUpload({img: 'car_'+ serailNo + '_0'});
        });
        // 展示车辆详细信息
        showCon('.edit');
        showCon('.view');        
        // 删除图片
        $('.pic-upload-wrap').on('click', '.del-pic', function() {
            $(this).closest('.pic-item').remove();
        })
        // 增加车辆信息
        addCar();
        // 删除汽车
        removeCar();
        $('.passBtn').on('click', confirmSubmit);
        // 显示大图
        showBig();

    })();
    

    /*function commonUpload(num){
        //获取上传照片DOM数组，照片
        uploadDomArr = $('.car-upload img');
        //给每个DOM元素添加上传照片事件，传入唯一img标志位，相当于ID，对应数组索引
        for(var i = 0, l = uploadDomArr.length; i < l; i++) {
            $('#car_item_' + num + i).fileUpload({img: 'car_'+ num + '_'+ i});
        }
    }*/

    function commonOldUpload(num){
        //获取上传照片DOM数组，照片
        $("#carinfo_ul_old_"+num).find('.pic-upload-wrap').each(function(){
            var id_start = $(this).find('.car-upload').eq(0).attr('id').split('_')[0];
            uploadDomArr = $(this).find('img');
            for(var i = 0, l = uploadDomArr.length; i < l; i++) {
                $('#'+ id_start + '_old' + num + i).fileUpload({img: id_start + '_old'+ num + '_'+ i});
                //$('#regcardimg_' + num + i).fileUpload({img: 'regcardimg_'+ num + '_'+ i});
            }
        });
    }

    function commonUpload(num){
            //获取上传照片DOM数组，照片
        $("#carinfo_ul_"+num).find('.car-upload').each(function(){
            var id_start = $(this).attr('id').split('_')[0];
            uploadDomArr = $(this).find('img');
            for(var i = 0, l = uploadDomArr.length; i < l; i++) {
                $('#'+ id_start + '_' + num + i).fileUpload({img: id_start + '_'+ num + '_'+ i});
                //$('#regcardimg_' + num + i).fileUpload({img: 'regcardimg_'+ num + '_'+ i});
            }
        });
    }
    // 上传之后的回调函数，处理各种表单上传的情况。
    window.handlePhotoData = function(r) {
        if(r.code == 0) {   //上传成功
            var imgObj = r.files[0]['images'][0],   //返回的上传照片Object数据
                imgIndex = String(r.uploadid).split('_'),  //截取拼接img标志位后的uploadid，获取索引
                id_header = imgIndex[1],//id前缀
                lineIndex = imgIndex[2],  //截取拼接img标志位后的uploadid，获取索引
                imgNumIndex = imgIndex[3], //截取拼接img标志位后的uploadid，获取索引数字部分
                imgUrl = imgObj.url,    //获取上传后的地址
                newDom = '',    //新添DOM内容
                nextIndex = -1,//新添上传图片索引
                uploadObj = null,  //
                newPicNum = null, // 新添加的未上传照片序列
                picSucUrl = ''; // 新添加的未上传照片序列

            divObj = $('#' + id_header+ '_' + lineIndex + imgNumIndex);//照片所在div
            picSucUrl = picUrl + imgUrl;
            uploadObj = divObj.find('img');
            uploadObj.attr('src', picSucUrl); 
            uploadObj.attr('data-url', picSucUrl);
               
            if (!uploadObj.parent().hasClass('uploaded')) {
                //照片的文案展示
                var imgText = divObj.attr('img-text');
                // 添加删除按钮
                uploadObj.closest('.upload').siblings('.pic-info').append('<a href="javascript:;" class="del-pic">删除</a>');
                //添加预览图显示类名
                uploadObj.parent().addClass('uploaded');

                // 创建新的图片上传dom
                newPicNum =  parseInt(imgNumIndex) + 1;

                // 生成新的上传
                var newUserCreditDom =  '<li class="pic-item">'+
                                            '<div class="upload car-upload" img-text='+ imgText +' id="'+ id_header + '_' + lineIndex + newPicNum +'">'+
                                                '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                    '<span><img src=""></span>'+
                                                    '<input type="file" value="点击上传图片" name="theFile">'+
                                                '</form>'+
                                            '</div>'+
                                            '<p class="pic-info">'+ imgText +'</p>'+
                                        '</li>';
                    
                $('#'+ id_header + '_' + lineIndex + imgNumIndex).closest('.pic-upload-wrap').append(newUserCreditDom);   //添加新DOM
                $('#'+ id_header + '_' + lineIndex + newPicNum).fileUpload({img: id_header + '_' + lineIndex + '_'+ newPicNum});  //给新添加的DOM添加事件

            } 
                    

        } else {
            $.error.dialog('网络不给力，请稍后再试！');
        }
    }

    // 车辆置换  2015-08-10 xxj
    function showCon(obj) {
        $('.auto-info').on('click',obj,function() {
            switch (obj) {
                case '.edit':
                    var oCon = $(this).parent().siblings('.info-wrap'),
                        loadUrl = oCon.data('url'),
                        lineIndex = oCon.data('index');

                        if(oCon.is(":hidden")){
                            $(this).html('<i class="icon">-</i>收起');
                            oCon.stop().slideDown();
                        } else {
                            $(this).html('<i class="icon">+</i>展开');
                            oCon.stop().slideUp();
                        }
                    break;
                case '.view':
                    var oCon = $(this).parent().siblings('.info-wrap');

                    if(oCon.is(":hidden")){
                        var aImg = $(this).parent().siblings('.info-wrap').find('img');

                        aImg.each(function(){
                            $(this).attr('src',$(this).data('url'));
                        })

                        $(this).html('<i class="icon">-</i>收起');
                        oCon.stop().slideDown();
                    } else {
                        $(this).html('<i class="icon">+</i>展开');
                        oCon.stop().slideUp();
                    }
                    break;
            }
        })
    }

    // 增加车辆信息
    function addCar() {
        $('.add-btn').on('click', function() {
            listNum ++;
            var carWrap = $('.car-info-all'),
                // listNum = $('.auto-info-item-edit').length,
                carList = '<li class="auto-info-item auto-info-item-edit" id="carinfo_ul_'+ listNum +'">'+
                                '<div class="auto-info-title clearfix">'+
                                '<input type="hidden" value="0" class="newCarId">'+
                                    '<ol class="car-info">'+
                                        '<li class="info-item">车辆资料信息</li>'+
                                        '<li class="info-item">车牌号：<input type="text" class="car-info-input plateNum"></li>'+
                                        '<li class="info-item">车架号：<input type="text" class="car-info-input frameNum"></li>'+
                                        '<li class="info-item">发动机号：<input type="text" class="car-info-input engineNum"></li>'+
                                    '</ol>'+
                                    '<a class="show-btn view" href="javascript:;"><i class="icon">+</i>展开</a>'+
                                '</div>'+
                                '<ul class="info-wrap">'+
                                    '<li class="listrow">'+
                                        '<div class="wd-30">'+
                                            '<span class="info-title">车辆类型：</span>'+
                                            '<label>'+
                                                '<select class="car-info-input carType">'+
                                                    '<option value="0">二手车</option>'+
                                                    '<option value="1">国产准新车</option>'+
                                                    '<option value="2">进口准新车</option>'+
                                                '</select>'+
                                            '</label>'+
                                        '</div>'+
                                    '</li>'+
                                    '<li class="listrow">'+
                                        '<div class="wd-30">'+
                                            '<span class="info-title">汽车厂商：</span>'+
                                            '<label><input type="text" class="car-info-input firm"></label>'+
                                        '</div>'+
                                        '<div class="wd-35">'+
                                            '<span class="info-title">品牌型号：</span>'+
                                            '<label><input type="text" class="car-info-input brand"></label>'+
                                        '</div>'+
                                        '<div class="wd-35">'+
                                            '<span class="info-title">车辆注册地：</span>'+
                                            '<label><input type="text" class="car-info-input address"></label>'+
                                        '</div>'+
                                    '</li>'+
                                    '<li class="listrow">'+
                                        '<div class="wd-30">'+
                                            '<span class="info-title">客户购车年份：</span>'+
                                            '<label><input type="text" onclick="WdatePicker({maxDate:\'%y-%M-%d\',isShowClear:false,isShowToday:false,readOnly:true})" class="car-info-input buy" ></label>'+
                                        '</div>'+
                                        '<div class="wd-35">'+
                                            '<span class="info-title">里程数：</span>'+
                                            '<label><input type="text" class="car-info-input mileage"></label>'+
                                            '<span>万公里</span>'+
                                        '</div>'+
                                        '<div class="wd-35">'+
                                            '<span class="info-title">官方指导价：</span>'+
                                            '<label><input type="text" class="car-info-input price"></label>'+
                                        '</div>'+
                                    '</li>'+
                                    '<li class="listrow">'+
                                        '<div class="wd-30">'+
                                            '<span class="info-title">车易拍评估价：</span>'+
                                            '<label><input type="text" class="car-info-input cypValue"></label>'+
                                        '</div>'+
                                        '<div class="wd-35">'+
                                            '<span class="info-title">当地评估价：</span>'+
                                            '<label><input type="text" class="car-info-input assess"></label>'+
                                        '</div>'+
                                        '<div class="wd-35">'+
                                            '<span class="info-title">验车员姓名：</span>'+
                                            '<label><input type="text" class="car-info-input staffName"></label>'+
                                        '</div>'+
                                    '</li>'+
                                    '<li class="listrow">'+
                                        '<div class = "wd-30">'+
                                            '<span class="info-title">车辆出厂年份：</span>'+
                                            '<label><input type="text" onclick="WdatePicker({maxDate:\'%y-%M-%d\',isShowClear:false,isShowToday:false,readOnly:true})" class="car-info-input factory"></label>'+
                                        '</div>'+
                                        '<div class = "wd-35">'+
                                            '<span class="info-title">检测报告号：</span>'+
                                            '<label><input type="text" class="car-info-input reportNo"></label>'+
                                        '</div>'+
                                        '<div class="wd-35">'+
                                            '<span class="info-title">验车员来源：</span>'+
                                            '<label>'+
                                                '<select class="car-info-input staffSource">'+
                                                    '<option value="0">本地验车员</option>'+
                                                    '<option value="1">车易拍验车员</option>'+
                                                '</select>'+
                                            '</label>'+
                                        '</div>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="机动车基本资料" id="regCardImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">机动车基本资料</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    /*'<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="行驶证" id="divCardImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">行驶证</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+*/
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="机动车钥匙" id="carKeyImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">机动车钥匙</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="二手车交易协议书" id="tranAgreeImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">二手车交易协议书</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="车辆交易委托授权书" id="tranAuthorImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">车辆交易委托授权书</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="机动车转移登记申请表" id="tranRegImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">机动车转移登记申请表</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="机动车评估价值截图" id="carValueImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">机动车评估价值截图</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="仪表盘照片" id="dashboardImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">仪表盘照片</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="原机动车所有人身份证" id="oriIdCardImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">原机动车所有人身份证</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="原机动车交易协议书" id="oriTranImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">原机动车交易协议书</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="员工身份证" id="staffIdCardImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">员工身份证</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="员工关系证明" id="staffRelImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">员工关系证明</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="car-img-wrap clearfix">'+
                                        '<ul class="pic-upload-wrap clearfix">'+
                                            '<li class="pic-item">'+
                                                '<div class="upload car-upload" img-text="其他相关资质" id="carImgs_'+ listNum +'0">'+
                                                    '<form class="upload-form" enctype="multipart/form-data" target="uploadframe" action="" method="post">'+
                                                        '<span><img src=""></span>'+
                                                        '<input type="file" value="点击上传图片" name="theFile">'+
                                                    '</form>'+
                                                '</div>'+
                                                '<p class="pic-info">其他相关资质</p>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</li>'+
                                    '<li class="listrow btn-row">'+
                                        '<a href="javascript:;" serailNo="0" class="btn del-btn">删除</a>'+
                                    '</li>'+
                                '</ul>'+
                            '</li>';
            $(carWrap).append(carList);
            commonUpload(listNum);
            $('#carinfo_ul_'+ listNum).find('.pic-upload-wrap').on('click', '.del-pic', function() {
                $(this).closest('.pic-item').remove();
            })
        })
    }

    // 删除汽车信息
    function removeCar() {
        $('.car-info-all').on('click', '.del-btn', function(){
            var obj = $(this);
            $.dialog.confirm('您是否要删除该车辆信息？',
                    function(){
                        var serailNo = obj.attr("serailNo");
                        if(delCarInfo(serailNo)){
                            obj.closest('.auto-info-item-edit').remove();
                        }
                    },function(){});
        })
    }

    function delCarInfo(id){
        var flag = false;
        if(id>0){
            $.ajax({
                url: domain + '/carmortgage/swap/patch/delete',
                type: 'post',
                data: {'serialNo':id,'swapId':swapId},
                dataType: 'json',
                async: false,
                success: function(r) {
                    if (r.suc) {
                        $.dialog.success('删除成功');
                        flag = true;
                    } else {
                        $.dialog.error(r.msg);
                    }
                }
            });
        }else{
            flag = true;
        }
        return flag;
    }
    // 校验输入的值是否为数字
    function checkNum(obj) {
        bPass = true;
        errorTip = '';

        switch(obj) {
            case '.mileage':
                errorTip = '请输入正确的里程数';
            break;
            case '.price':
                errorTip = '请输入正确的官方指导价';
            break;
            case '.assess':
                errorTip = '请输入正确的当地评估价';
            break;
        }

        $(obj).each(function(index,item) {
            if(!regNum.test($(item).val())) {
                bPass = false;
                return false;
            }
        })
        return bPass;
    }
    function confirmSubmit(){
        $.dialog.confirm('您是否要提交补件信息？',function(){submitCarInfo();},function(){})
    }
    // 提交汽车信息
    function submitCarInfo() {
        var carInfoArry = [],
            picArry = [],
            sUrl = '',
            jsonData = {},
            sData = '',
            aCarId = [],
            oData = '',
            sCarId = '';

        // 获取录入的车辆信息
        $('.auto-info-item-edit').each(function(index,item) {

            sData = '';
            imgArry = [];
            sUrl = '';
            imgData = '';

            // 组织图片URL
            /*$(this).find('.car-img-wrap img').each(function(index,item) {
                if($(this).attr('src') != '')
                picArry.push($(this).attr('src'));
            })
            sUrl = picArry.join(';');*/

            // 组织图片URL
            $(this).find('.car-img-wrap').each(function() {
                //if($(this).attr('src') != '')
                //picArry.push($(this).attr('src'));
                picArry = [];
                var key = $(this).find('.car-upload').eq(0).attr('id').split('_')[0];
                $(this).find('img').each(function(){
                    if($(this).attr('src') != ''){
                        picArry.push($(this).attr('src'));
                    }
                });
                sUrl = picArry.join(';');
                imgArry.push('"'+ key + '":"'+ sUrl+'"');
            })
            imgData = imgArry.join(',');


            sData = "{"+
                        "\"license\":" + "\"" + $.trim($('.plateNum').eq(index).val()) + "\"" + ","+                //车牌号
                        "\"vin\":" + "\"" + $.trim($('.frameNum').eq(index).val()) + "\"" + ","+                    //车架号
                        "\"eno\":" + "\"" + $.trim($('.engineNum').eq(index).val()) + "\"" + ","+                   //发动机号
                        "\"carFirm\":" + "\"" + $('.firm').eq(index).val() + "\"" + ","+                    //汽车厂商
                        "\"carModel\":" + "\"" + $('.brand').eq(index).val() + "\"" + ","+                  //品牌型号
                        "\"registerAddress\":" + "\"" + $('.address').eq(index).val() + "\"" + ","+       //车辆注册地
                        "\"buyTime\":" + "\"" + $('.buy').eq(index).val() + "\"" + ","+                   //客户购车年份
                        "\"mileage\":" + "\"" + $('.mileage').eq(index).val() + "\"" + ","+             //里程数
                        "\"carPrice\":" + "\"" + $('.price').eq(index).val() + "\"" + ","+                  //官方指导价
                        "\"factoryTime\":" + "\"" + $('.factory').eq(index).val() + "\"" + ","+         //车辆出厂年份
                        "\"localValuation\":" + "\"" + $('.assess').eq(index).val() + "\"" + ","+           //当地评估价
                        "\"cypValuation\":" + "\"" + $('.cypValue').eq(index).val() + "\"" + ","+           //当地评估价
                        "\"checkUser\":" + "\"" + $('.staffName').eq(index).val() + "\"" + ","+         //验车员姓名
                        "\"checkUserChannel\":" + "\"" + $('.staffSource').eq(index).val() + "\"" + ","+   //验车员来源
                        "\"reportNo\":" + "\"" + $('.reportNo').eq(index).val() + "\"" + ","+   //报告号
                        "\"carType\":" + "\"" + $('.carType').eq(index).val() + "\"" + ","+   //验车员来源
                        imgData +                                                //车辆图片链接
                     "}";

            carInfoArry.push(sData);
        })

        // 勾选置换的车辆id信息
        $('.check-car').each(function(index,item) {
            if($(this).is(':checked')) {
                aCarId.push($(this).data('carid'));
            }
            sCarId  = aCarId.join(';');
        })

        oData = 'swapId='+swapId+'&choice=pass&comment='+$("#exampletextarea").val()+'&serialNos=' + sCarId + '&jsonData=[' + String(carInfoArry) + "]" ;
        
        if(checkNum('.mileage') && checkNum('.price') && checkNum('.assess')) {
            $.ajax({
                url: domain + '/carmortgage/swap/patch/approve',
                type: 'post',
                data: oData,
                dataType: 'json',
                async: true,
                success: function(r) {
                    if (r.suc) {
                        $.dialog.merchantSuccess('提交成功');
                    } else {
                        $.dialog.error(r.msg);
                    }
                }
            });
        } else {
            $.error.dialog(errorTip);
        }

    }

    // 显示大图片
    function showBig() {
        var oDiv = '';
        $('.auto-info-item').on('click', '.J-view', function() {
            if(!($(this).parent().siblings('img').attr('src') == '')) {
                imgUrl = $(this).parent().siblings('img').attr('src');
                oDiv = "<div class='wrap'>"+
                            "<div class='ba-wrap'></div>"+
                            "<div class='img-wrap'>"+
                                "<img src="+imgUrl+">"+
                            "</div>"+
                        "</div>";
                $('body').append(oDiv);

                var oCodeTip = $('.img-wrap');
                    winH = $(window).height(),
                    scrollT = $(document).scrollTop(),
                    oTop = '';

                oTop = (winH - $(oCodeTip).outerHeight()) / 2 + scrollT;
                $(oCodeTip).css({
                    top: (oTop > 0 ? oTop : 0)+'px'
                });
                return false;
            }
        })

        $('body').on('click', function() {
            if($('.wrap').length > 0){
                $('.wrap').remove();
            }
        })
    }
});

// 图片跟随滚动
$(window).scroll(function(){
    var winH = $(window).height(),
        winW = $(window).width(),
        scrollT = $(document).scrollTop(),
        oTapeTip = $('.img-wrap'),
        oTop = '',
        oLeft = '';


    oTop = (winH - $(oTapeTip).outerHeight()) / 2 + scrollT;

    $(oTapeTip).css({
        top: (oTop > 0 ? oTop : 0)+'px'
    });

});