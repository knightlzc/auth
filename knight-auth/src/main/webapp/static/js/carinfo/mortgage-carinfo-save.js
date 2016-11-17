
/*
 concat from: rrloan/js/plugin/fileupload.js
*/

/**
 * @name: 上传图片
 * @author: zicui.liu
 * @dependent: base-all.js
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



/*
 concat from: rrloan/modules/review/js/mortgage-save.js
*/

 /*
 * @name: 后台车抵押
 * @author: zicui.liu
 * @date: 2016-01-12
 */

(function($) {
	$(function () {
		var domain = 'http://' + rrloan.env.subdomain + '.' + rrloan.env.domain,
	        urls = {
	        	caruserSave: '/risk/caruser/save',
	        	caruserUpdate: '/risk/caruser/update',
	        	carInfoSave: '/risk/carinfo/save',
	        	carInfoUpdate: '/risk/carinfo/update',
	        	bankInfoSave: '/risk/bank/save',
	        	bankInfoUpdate: '/risk/bank/update',
	        	procedureInfoSave: '/risk/formality/save',
	        	procedureInfoUpdate: '/risk/formality/update',
	        	contractInfoSave: '/risk/contract/save',
	        	contractInfoUpdate: '/risk/contract/update'
	        },

	        // 上传照片的域名
	        picUrl = 'http://fmn.rrfmn.com/',

	        // 上传正在加载中图标
	        loadingImg = 'http://s.xnimg.cn/rrloan/img/loading32.gif',

	        // 1.用户信息
	        uploadUserDomArr = [], // 获取上传用户信息照片DOM数组
	        userInfoBtn = $('#user_info_btn'), // 用户信息提交按钮
	        userImgs = $('#user_imgs'), // 拼接图片的隐藏域
	        // userIncomeImgs = $('#user_income_imgs'), // 收入证明
	        // nextUserIncomeIndex = -1,
	        // newUserIncomeDom = [],
	        userCreditImgs = $('#user_credit_imgs')// 个人征信报告图片
	        nextUserCreditIndex = -1,
	        newUserCreditDom = [],

	        // 2.车辆资料信息
	        uploadCarDomArr = [], // 获取上传用户信息照片DOM数组
	        nextCarIndex = -1,
	        newCarDom = [],
	        // carInfoBtn = $('#car_info_btn'), // 用户信息提交按钮
	        // carImgs = $('#car_imgs'), // 拼接图片的隐藏域

	        // 3.银行卡信息
	        uploadBankDomArr = [], // 获取上传用户信息照片DOM数组
	        newBankDom = [],
	        nextBankIndex = -1, //新添上传图片索引
	        bankInfoBtn = $('#bank_info_btn'), // 用户信息提交按钮
	        bankImgs = $('#bank_imgs'), // 拼接图片的隐藏域

	        // 4.手续资料
	        uploadProcedureDomArr = [], // 获取上传用户信息照片DOM数组
	        newProcedureDom = [],
	        nextProcedureIndex = -1, //新添上传图片索引
	        procedureInfoBtn = $('#procedure_info_btn'), // 用户信息提交按钮
	        procedureImgs = $('#procedure_imgs'), // 拼接图片的隐藏域

	        // 5.合同信息
	        uploadContractDomArr = [], // 获取上传用户信息照片DOM数组
	        newContractDom = [],
	        nextContractIndex = -1, //新添上传图片索引
	        contractInfoBtn = $('#contract_info_btn'), // 用户信息提交按钮
	        contractImgs = $('#contract_imgs'); // 拼接图片的隐藏域

	        // 车抵押修改  2015-06-23 xxj
		    var oTitle = $('.auto-info-title');
		    var lineIndex = '';
		    var editBtn = $('.edit');
			var infoItem = $('.rv-car-wrap');

	    init();

	    // 初始化
		function init(){

			// 1.用户信息
			userUploadEvent();
			userInfoSubmit();

			// 2.车辆资料信息  //调整到加载之后执行
			// carUploadEvent();
			// carInfoSubmit();

			// 3.银行卡信息
			bankUploadEvent();
			bankInfoSubmit();

			// 4.手续资料
			procedureUploadEvent();
			procedureInfoSubmit();

			// 5.合同信息
			contractUploadEvent();
			contractInfoSubmit();

			// 查看大图
			// if ($('.J-view').length > 0) {
			// 	$('.upload-wrap').delegate('.J-view', 'click', function(){
			// 		imgUrl = $(this).parents('li').find('img').attr('src');
			// 		window.open(imgUrl);
			// 	});
			// }

			showCon('.edit');
			showCon('.view');
			// 缩略图查看大图
			showBig($('body'));
			// showBig($('.img-mod'));
			// 原图查看大图
			// showBig($('.img-original'));
			// 删除图片
			deletePic();
		}

		// 1.上传用户信息身份证照片
		function userUploadEvent() {
			var uploadUserIncomeDomArr = [],
				uploadUserCreditDomArr = [];
			if ($('.upload-user').length > 0) {
				//获取上传照片DOM数组，证件照片
		        uploadUserDomArr = $('.upload-user img');

		        //给每个DOM元素添加上传照片事件，传入唯一img标志位，相当于ID，对应数组索引
		        for(var i = 0, l = uploadUserDomArr.length; i < l; i++) {
		            $('#upload_user' + i).fileUpload({img: 'user' + i});
		            $('#upload_user' + i + ' input').change(function() {
		                $(this).closest('.upload-user').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

		         // 个人收入证明
	           // uploadUserIncomeDomArr = $('.upload-user-income img');
	            //给每个DOM元素添加上传照片事件，传入唯一img标志位，相当于ID，对应数组索引
		        /*for(var j = 0, m = uploadUserIncomeDomArr.length; j < m; j++) {
		            $('#upload_user_income' + j).fileUpload({img: 'income' + j});
		            $('#upload_user_income' + j + ' input').change(function() {
		                $(this).closest('.upload-user-income').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }*/

	            // 个人征信报告
	            uploadUserCreditDomArr = $('.upload-user-credit img');
		        //给每个DOM元素添加上传照片事件，传入唯一img标志位，相当于ID，对应数组索引
		        for(var k = 0, n = uploadUserCreditDomArr.length; k < n; k++) {
		            $('#upload_user_credit' + k).fileUpload({img: 'credit' + k});
		            $('#upload_user_credit' + k + ' input').change(function() {
		                $(this).closest('.upload-user-credit').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

	             // 删除照片的操作
	            $('#upload_userIncome_list').delegate('.J-del', 'click', function(){
	            	// 把相应的dom元素删除
	            	$(this).closest('li').remove();
	            });
	            $('#upload_userCredit_list').delegate('.J-del', 'click', function(){
	            	// 把相应的dom元素删除
	            	$(this).closest('li').remove();
	            });
			}
		}

		// 1.用户信息提交
		function userInfoSubmit() {
			var inputLen = 0,
				inputArr = [],
				flag = true,
				update = 0,
				ajaxUrl = '',
				tempUploadUrlArr = [],
				tempUploadUrlArr1 = [],
				tempUploadUrlArr2 = [],
				index = 0,
				picObj = null,
			//	incomeObj = null,
				creditObj = null;

			if (userInfoBtn.length > 0) {
				userInfoBtn.click(function(){
					// 重置变量
					index = 0;
					tempUploadUrlArr = [];
					tempUploadUrlArr1 = [];
					tempUploadUrlArr2 = [];
					flag = true;
					// 判断输入框不为空
					inputLen = $('#userForm .text').length;
					inputArr = $('#userForm .text');

					for(var i = 0; i < inputArr.length; i++ ) {
						// 最后一个学习层次不用验证空
						if ($(inputArr[i]).val() == '') {
							$.dialog.error('请填写' + $(inputArr[i]).prev().find('i').text());
							flag = false;
	 						break;
						}
					}

					if (!flag) {
						return false;
					}

					// 组织表单上传图片的数据，拼接成字符串
					$('.upload-user .pic').each(function(){

						picObj = $(this);
						if (picObj.hasClass('uploaded')) {
							tempUploadUrlArr.push(picObj.find('img').attr('data-url'));
							index++;
						} else {
							if (index == 0) {
								$.dialog.error('请上传身份证正面照片');
								flag = false;
								index++;
								return false;
							}

							if (index == 1) {
								$.dialog.error('请上传身份证反面照片');
								flag = false;
								index++;
								return false;
							}
						}
					});

					if (!flag) {
						return false;
					}
					userImgs.val(tempUploadUrlArr.join(';'));


					// 判断个人收入证明
					// 循环照片路径
					// $('.upload-user-income .pic').each(function(){
					// 	incomeObj = $(this);
					// 	if (incomeObj.hasClass('uploaded')) {
					// 		tempUploadUrlArr1.push(incomeObj.find('img').attr('src'));
					// 	}
					// });
					// userIncomeImgs.val(tempUploadUrlArr1.join(';'));

					// 个人征信报告
					$('.upload-user-credit .pic').each(function(){
						creditObj = $(this);
						if (creditObj.hasClass('uploaded')) {
							tempUploadUrlArr2.push(creditObj.find('img').attr('data-url'));
						}
					});
					userCreditImgs.val(tempUploadUrlArr2.join(';'));


					update = $('#caruserUpdate').val();
					if (update == '0') { // 1修改
						ajaxUrl = domain + urls.caruserSave;
					} else {
						ajaxUrl = domain + urls.caruserUpdate;
					}

					$.ajax({
		                url: ajaxUrl,
		                type: 'post',
		                data: $('#userForm').serialize(),
		                dataType: 'json',
		                success: function(r) {
		                    if (r.code == 0) {
		                    	$.dialog.successReload('成功');
		                    } else {
		                    	$.dialog.error(r.msg);
		                    }
		                }
		            });
				});
			}
		}

		// 2.上传车辆资料信息照片
		function carUploadEvent(num) {
			if ($('.upload-car-other').length > 0) {

		        // 上传车辆资料信息照片
		        uploadCarDomArr = $('.upload-car-other img');

			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_' + num) + '_' + j).fileUpload({img: 'car_' + num + '_' + j});
		            $(('#upload_car_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-other').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

	            // 删除照片的操作
	            // $('#upload_car_list').delegate('.J-del', 'click', function(){
	            // 	// 把相应的dom元素删除
	            // 	$(this).closest('li').remove();
	            // });
			}
		}

		// 2-1.机动车登记证照片
		function carRegCardUploadEvent(num) {
			if ($('.upload-car-reg-card').length > 0) {
		        // 上传车辆资料信息照片
		        uploadCarDomArr = $('.upload-car-reg-card img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_reg_card_' + num) + '_' + j).fileUpload({img: 'regcard_' + num + '_' + j});
		            $(('#upload_car_reg_card_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-reg-card').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}
		// 2-2.行驶证照片
		function carDivCardUploadEvent(num) {
			if ($('.upload-car-div-card').length > 0) {
		        // 上传车辆资料信息照片
		        uploadCarDomArr = $('.upload-car-div-card img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_div_card_' + num) + '_' + j).fileUpload({img: 'divcard_' + num + '_' + j});
		            $(('#upload_car_div_card_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-div-card').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}
		// 2-3.机动车钥匙息照片
		function carKeyUploadEvent(num) {
			if ($('.upload-car-key').length > 0) {
		        // 上传车辆资料信息照片
		        uploadCarDomArr = $('.upload-car-key img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_key_' + num) + '_' + j).fileUpload({img: 'key_' + num + '_' + j});
		            $(('#upload_car_key_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-key').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-4.二手车交易协议书
		function carAgreementUploadEvent(num) {
			if ($('.upload-car-agreement').length > 0) {
		        // 上传车辆资料信息照片
		        uploadCarDomArr = $('.upload-car-agreement img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_agreement_' + num) + '_' + j).fileUpload({img: 'agreement_' + num + '_' + j});
		            $(('#upload_car_agreement_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-agreement').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-5.车辆交易委托授权书
		function carAuthorUploadEvent(num) {
			if ($('.upload-car-author').length > 0) {
		        // 上传车辆资料信息照片
		        uploadCarDomArr = $('.upload-car-author img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_author_' + num) + '_' + j).fileUpload({img: 'author_' + num + '_' + j});
		            $(('#upload_car_author_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-author').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-6.机动车转移登记申请表
		function carRegtabUploadEvent(num) {
			if ($('.upload-car-regtab').length > 0) {
		        // 上传车辆资料信息照片
		        uploadCarDomArr = $('.upload-car-regtab img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_regtab_' + num) + '_' + j).fileUpload({img: 'regtab_' + num + '_' + j});
		            $(('#upload_car_regtab_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-regtab').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-7.机动车评估价值截图
		function carPriceUploadEvent(num) {
			if ($('.upload-car-price').length > 0) {
		        // 上传车辆资料信息照片
		        uploadCarDomArr = $('.upload-car-price img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_price_' + num) + '_' + j).fileUpload({img: 'price_' + num + '_' + j});
		            $(('#upload_car_price_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-price').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-8.仪表盘照片
		function carDashboardUploadEvent(num) {
			if ($('.upload-car-dashboard').length > 0) {
		        uploadCarDomArr = $('.upload-car-dashboard img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_dashboard_' + num) + '_' + j).fileUpload({img: 'dashboard_' + num + '_' + j});
		            $(('#upload_car_dashboard_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-dashboard').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-9.原机动车所有人身份证
		function carOriIdCardUploadEvent(num) {
			if ($('.upload-car-oriIdCard').length > 0) {
		        uploadCarDomArr = $('.upload-car-oriIdCard img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_oriIdCard_' + num) + '_' + j).fileUpload({img: 'oriIdCard_' + num + '_' + j});
		            $(('#upload_car_oriIdCard_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-oriIdCard').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-10.原机动车交易协议书
		function carOriTranUploadEvent(num) {
			if ($('.upload-car-oriTran').length > 0) {
		        uploadCarDomArr = $('.upload-car-oriTran img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_oriTran_' + num) + '_' + j).fileUpload({img: 'oriTran_' + num + '_' + j});
		            $(('#upload_car_oriTran_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-oriTran').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-11.员工身份证
		function carStaffIdCardUploadEvent(num) {
			if ($('.upload-car-staffIdCard').length > 0) {
		        uploadCarDomArr = $('.upload-car-staffIdCard img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_staffIdCard_' + num) + '_' + j).fileUpload({img: 'staffIdCard_' + num + '_' + j});
		            $(('#upload_car_staffIdCard_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-staffIdCard').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2-12.员工关系证明
		function carStaffRelUploadEvent(num) {
			if ($('.upload-car-staffRel').length > 0) {
		        uploadCarDomArr = $('.upload-car-staffRel img');
			    for(var j = 0, k = uploadCarDomArr.length; j < k; j++) {
			    	$(('#upload_car_staffRel_' + num) + '_' + j).fileUpload({img: 'staffRel_' + num + '_' + j});
		            $(('#upload_car_staffRel_' + num) + '_' + j + ' input').change(function() {
		                $(this).closest('.upload-car-staffRel').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

			}
		}

		// 2.车辆资料信息提交
		function carInfoSubmit(obj,num) {
	        var autoInfoItem = $('.auto-info-item'), // 用户信息提交按钮父级
	        	carInfoBtn = $('#car_info_btn'); // 用户信息提交按钮

			if (carInfoBtn.length > 0) {
				var inputLen = 0,
					inputArr = [],
					update = 0,
					ajaxUrl = '',
					tempUploadUrlArr = [],
					carForm = '';

				// autoInfoItem.on('click', '.submit-btn',function(){
				$('.submit-btn').on('click', function(){
					// 判断输入框不为空
					carForm = $(this).parent().siblings('#carForm_' + num);
					carImgs = carForm.children('#car_imgs');
					inputLen = carForm.find('.text').length;
					inputArr = carForm.find('.text');

					for(var i = 0; i < inputArr.length; i++ ) {
						if ($(inputArr[i]).val() == '') {
							$.dialog.error('请填写' + $(inputArr[i]).prev().find('i').text());
	 						break;
						}
					}

					// 循环照片路径
					/*var curFormPic = carForm.siblings('.upload-car-wrap').find('.pic');
					curFormPic.each(function(){
						var self = $(this);
						if (self.hasClass('uploaded')) {
							tempUploadUrlArr.push(self.find('img').attr('data-url'));
						}
					});

					carImgs.val(tempUploadUrlArr.join(';'));*/
					carForm.children('.img-submit').each(function(){
						var form_id = $(this).attr('form_id');
						var curFormPic = carForm.parent().find('#'+form_id).find('.pic');
						tempUploadUrlArr = [];
						curFormPic.each(function(){
							var self = $(this);
							if (self.hasClass('uploaded')) {
								tempUploadUrlArr.push(self.find('img').attr('data-url'));
							}
						});
						$(this).val(tempUploadUrlArr.join(';'));
					});
					ajaxUrl = domain + urls.carInfoSave;
					/*update = $('#carinfoUpdate_' + num).val();
					if (update == '0') { // 1修改
						ajaxUrl = domain + urls.carInfoSave;
					} else {
						ajaxUrl = domain + urls.carInfoUpdate;
					}*/

		            // 提交表单
		            $.ajax({
		                url: ajaxUrl,
		                type: 'post',
		                data: carForm.serialize(),
		                dataType: 'json',
		                success: function(r) {
		                    if (r.code == 0) {
		                    	$.dialog.autoSuccess('成功',obj);
		                    } else {
		                    	$.dialog.error(r.msg);
		                    }
		                }
		            });

				})
			}
		}

		// 4.上传近三个月流水
		function bankUploadEvent() {
			if ($('.upload-bank').length > 0) {
				//获取上传照片DOM数组，证件照片
		        uploadBankDomArr = $('.upload-bank img');

			    for(var i = 0, l = uploadBankDomArr.length; i < l; i++) {
			    	$('#upload_bank' + i).fileUpload({img: 'bank' + i});
		            $('#upload_bank' + i + ' input').change(function() {
		                $(this).parents('.upload-bank').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

		        // 删除照片的操作
	            $('#upload_bank_list').delegate('.J-del', 'click', function(){
	            	// 把相应的dom元素删除
	            	$(this).parents('li').remove();
	            });
			}
		}

		// 4.银行卡信息提交
		function bankInfoSubmit() {
			if (bankInfoBtn.length > 0) {
				var inputLen = 0,
					inputArr = [],
					flag = true,
					tempUploadUrlArr = [],
					update = 0,
					ajaxUrl = '';

				bankInfoBtn.click(function(){
					// 重置
					flag = true;
					tempUploadUrlArr = [];
					// 判断输入框不为空
					inputLen = $('#bankForm .text').length;
					inputArr = $('#bankForm .text');

					for(var i = 0; i < inputArr.length; i++ ) {
						if ($(inputArr[i]).val() == '') {
							$.dialog.error('请填写' + $(inputArr[i]).prev().find('i').text());
							flag = false;
	 						break;
						}
					}

					if (!flag) {
						return false;
					}

					// 循环照片路径
					$('.upload-bank .pic').each(function(){
						picObj = $(this);
						if (picObj.hasClass('uploaded')) {
							tempUploadUrlArr.push(picObj.find('img').attr('data-url'));
						}
					});

		            // 组织表单上传图片的数据，拼接成字符串
					bankImgs.val(tempUploadUrlArr.join(';'));

		            update = $('#bankUpdate').val();
					if (update == '0') { // 1修改
						ajaxUrl = domain + urls.bankInfoSave;
					} else {
						ajaxUrl = domain + urls.bankInfoUpdate;
					}

		            // 提交表单
		            $.ajax({
		                url: ajaxUrl,
		                type: 'post',
		                data: $('#bankForm').serialize(),
		                dataType: 'json',
		                success: function(r) {
		                    if (r.code == 0) {
		                    	$.dialog.successReload('成功');
		                    } else {
		                    	$.dialog.error(r.msg);
		                    }
		                }
		            });
		        });
			}
		}

		// 5.手续资料信息
		function procedureUploadEvent() {
			if ($('.upload-procedure').length > 0) {
				//获取上传照片DOM数组，证件照片
		        uploadProcedureDomArr = $('.upload-procedure img');

			    for(var i = 0, l = uploadProcedureDomArr.length; i < l; i++) {
			    	$('#upload_procedure' + i).fileUpload({img: 'procedure' + i});
		            $('#upload_procedure' + i + ' input').change(function() {
		                $(this).parents('.upload-procedure').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

		        // 删除照片的操作
	            $('#upload_procedure_list').delegate('.J-del', 'click', function() {
	            	// 把相应的dom元素删除
	            	$(this).parents('li').remove();
	            });
			}
		}

		function procedureInfoSubmit() {
			var tempUploadUrlArr = [],
				update = 0,
				ajaxUrl = '',
				picObj = null;

			if (procedureInfoBtn.length > 0) {
				procedureInfoBtn.click(function(){
					// 重置
					tempUploadUrlArr = [];

					// if ($('#procedure_id').val() == '') {
					// 	$.dialog.error('合同号不能为空');
					// 	return false;
					// }

					$('.upload-procedure .pic').each(function(){
						picObj = $(this);
						if (picObj.hasClass('uploaded')) {
							tempUploadUrlArr.push(picObj.find('img').attr('data-url'));
						}
					});

		            // 组织表单上传图片的数据，拼接成字符串
					procedureImgs.val(tempUploadUrlArr.join(';'));

		           	update = $('#formalityUpdate').val();
					if (update == '0') { // 1修改
						ajaxUrl = domain + urls.procedureInfoSave;
					} else {
						ajaxUrl = domain + urls.procedureInfoUpdate;
					}

		            // 提交表单
		            $.ajax({
		                url: ajaxUrl,
		                type: 'post',
		                data: $('#procedureForm').serialize(),
		                dataType: 'json',
		                success: function(r) {
		                    if (r.code == 0) {
		                    	$.dialog.successReload('成功');
		                    } else {
		                    	$.dialog.error(r.msg);
		                    }
		                }
		            });
		        });
			}
		}

		// 6.合同信息
		function contractUploadEvent() {
			if ($('.upload-contract').length > 0) {
				//获取上传照片DOM数组，证件照片
		        uploadContractDomArr = $('.upload-contract img');

			    for(var i = 0, l = uploadContractDomArr.length; i < l; i++) {
			    	$('#upload_contract' + i).fileUpload({img: 'contract' + i});
		            $('#upload_contract' + i + ' input').change(function() {
		                $(this).parents('.upload-contract').css('background','#ddd url(' + loadingImg + ') center center no-repeat');
		            });
		        }

		        // 删除照片的操作
	            $('#upload_contract_list').delegate('.J-del', 'click', function() {
	            	// 把相应的dom元素删除
	            	$(this).parents('li').remove();
	            });
			}
		}

		function contractInfoSubmit() {
			var tempUploadUrlArr = [],
				update = 0,
				ajaxUrl = '',
				picObj = null;

			if (contractInfoBtn.length > 0) {
				contractInfoBtn.click(function(){
					// 重置
					tempUploadUrlArr = [];

					if ($('#contract_id').val() == '') {
						$.dialog.error('合同号不能为空');
						return false;
					}

					$('.upload-contract .pic').each(function(){
						picObj = $(this);
						if (picObj.hasClass('uploaded')) {
							tempUploadUrlArr.push(picObj.find('img').attr('data-url'));
						}
					});

		            // 组织表单上传图片的数据，拼接成字符串
					contractImgs.val(tempUploadUrlArr.join(';'));

		           	update = $('#contractUpdate').val();
					if (update == '0') { // 1修改
						ajaxUrl = domain + urls.contractInfoSave;
					} else {
						ajaxUrl = domain + urls.contractInfoUpdate;
					}

		            // 提交表单
		            $.ajax({
		                url: ajaxUrl,
		                type: 'post',
		                data: $('#contractForm').serialize(),
		                dataType: 'json',
		                success: function(r) {
		                    if (r.code == 0) {
		                    	$.dialog.successReload('成功');
		                    } else {
		                    	$.dialog.error(r.msg);
		                    }
		                }
		            });
		        });
			}
		}

		// 上传之后的回调函数，处理各种表单上传的情况。
		window.handlePhotoData = function(r) {
	    	if(r.code == 0) {   //上传成功

	            var imgObj = r.files[0]['images'][1],   //返回的上传照片Object数据   小图
	            	imgObjBig = r.files[0]['images'][0],   //返回的上传照片Object数据   大图
	                imgIndex = String(r.uploadid).split('_',2)[1],  //截取拼接img标志位后的uploadid，获取索引
	                imgIndexTem = String(r.uploadid).split('_',4),  //截取拼接img标志位后的uploadid，获取索引,车辆比较特殊
	                imgIndexSpe = imgIndexTem[1] + '_' + imgIndexTem[2]; //截取拼接车辆img标志位后的uploadid
	                imgNumIndex = imgIndex.replace(/[^0-9]/ig, ''), //截取拼接img标志位后的uploadid，获取索引数字部分
	                imgNumIndexSpe = imgIndexTem[3], //截取车辆img后的uploadid，获取索引数字部分,车辆比较特殊
	                imgUrl = imgObj.url,    //获取上传后的地址  小图
	                imgUrlBig = imgObjBig.url,    //获取上传后的地址  大图
	                newDom = '',    //新添DOM内容
	                nextIndex = -1, //新添上传图片索引
	                uploadBankObj = null, // 银行卡流水当前上传照片的dom
	                uploadProcedureObj = null, // 合同当前上传照片的dom
	                uploadCarObj = null,  // 车辆资料信息
	                uploadUserIncomeObj = null,
	                uploadUserCreditObj = null;

	            // 1.1)用户信息
	            if (imgIndex.indexOf('user') > -1) {
		            //添加预览图显示类名
		            $(uploadUserDomArr[imgNumIndex]).parent().addClass('uploaded');
		            //改写img标签地址为上传照片的URL
		            uploadUserDomArr[imgNumIndex].src = picUrl + imgUrl;
		            $(uploadUserDomArr[imgNumIndex]).attr('data-url',picUrl + imgUrlBig);
	            }

	            // 1.2)用户收入证明
	            /*if (imgIndex.indexOf('income') > -1) {
	            	//改写img标签地址为上传照片的URL
		            uploadUserIncomeObj = $('#upload_user_income' + imgNumIndex).find('img');
		            uploadUserIncomeObj.attr('src', picUrl + imgUrl);

		            // 判断对已上传的照片进行修改
	                if (!uploadUserIncomeObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadUserIncomeObj.parent().addClass('uploaded');

			            uploadUserIncomeObj.closest('.upload-user-income').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextUserIncomeIndex =  parseInt(imgNumIndex) + 1;

		            	// 生成新的上传
		        		newUserIncomeDom =    '<li>' +
			        						'<div class="upload-user-income" id="upload_user_income' + nextUserIncomeIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">收入证明</p>' +
			                            '</li>';
		                $('#upload_userIncome_list').append(newUserIncomeDom);   //添加新DOM
		                $('#upload_user_income' + nextUserIncomeIndex).fileUpload({img: 'income' + nextUserIncomeIndex});  //给新添加的DOM添加事件
	                }
	            }  */

	            // 1.1)用户征信报告
	            if (imgIndex.indexOf('credit') > -1) {
	            	//改写img标签地址为上传照片的URL
		            uploadUserCreditObj = $('#upload_user_credit' + imgNumIndex).find('img');
		            uploadUserCreditObj.attr('src', picUrl + imgUrl);
		            uploadUserCreditObj.attr('data-url', picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadUserCreditObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadUserCreditObj.parent().addClass('uploaded');

			            uploadUserCreditObj.closest('.upload-user-credit').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextUserCreditIndex =  parseInt(imgNumIndex) + 1;

		            	// 生成新的上传
		        		newUserCreditDom = '<li>' +
			        						'<div class="upload-user-credit" id="upload_user_credit' + nextUserCreditIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">相关资质</p>' +
			                            '</li>';
		                $('#upload_userCredit_list').append(newUserCreditDom);   //添加新DOM
		                $('#upload_user_credit' + nextUserCreditIndex).fileUpload({img: 'credit' + nextUserCreditIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2.车辆资料信息
	            if (imgIndexSpe.indexOf('car_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">车辆资料</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_list').append(newCarDom);   //添加新DOM
		                $('#upload_car_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'car_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }
	            // 2-1.机动车基本资料信息
	            if (imgIndexSpe.indexOf('regcard_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_reg_card_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_reg_card_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">机动车基本资料</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_reg_card_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_reg_card').append(newCarDom);   //添加新DOM
		                $('#upload_car_reg_card_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'regcard_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-2.行驶证信息
	            if (imgIndexSpe.indexOf('divcard_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_div_card_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_div_card_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">行驶证</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_div_card_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_div_card').append(newCarDom);   //添加新DOM
		                $('#upload_car_div_card_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'divcard_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-3.钥匙信息
	            if (imgIndexSpe.indexOf('key_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_key_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_key_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">机动车钥匙</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_key_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_key').append(newCarDom);   //添加新DOM
		                $('#upload_car_key_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'key_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-4.二手车交易协议书
	            if (imgIndexSpe.indexOf('agreement_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_agreement_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_agreement_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">二手车交易协议书</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_agreement_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_agreement').append(newCarDom);   //添加新DOM
		                $('#upload_car_agreement_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'agreement_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-5.车辆交易委托授权书
	            if (imgIndexSpe.indexOf('author_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_author_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_author_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">车辆交易委托授权书</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_author_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_author').append(newCarDom);   //添加新DOM
		                $('#upload_car_author_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'author_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-6.机动车转移登记申请表
	            if (imgIndexSpe.indexOf('regtab_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_regtab_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_regtab_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">机动车转移登记申请表</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_regtab_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_regtab').append(newCarDom);   //添加新DOM
		                $('#upload_car_regtab_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'regtab_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-7.机动车评估价值截图
	            if (imgIndexSpe.indexOf('price_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_price_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_price_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">机动车评估价值截图</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_price_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_price').append(newCarDom);   //添加新DOM
		                $('#upload_car_price_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'price_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-8.仪表盘照片
	            if (imgIndexSpe.indexOf('dashboard_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_dashboard_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_dashboard_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">仪表盘照片</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_dashboard_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_dashboard').append(newCarDom);   //添加新DOM
		                $('#upload_car_dashboard_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'dashboard_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-9.原机动车所有人身份证
	            if (imgIndexSpe.indexOf('oriIdCard_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_oriIdCard_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_oriIdCard_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">原机动车所有人身份证</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_oriIdCard_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_oriIdCard').append(newCarDom);   //添加新DOM
		                $('#upload_car_oriIdCard_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'oriIdCard_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-10.原机动车交易协议书
	            if (imgIndexSpe.indexOf('oriTran_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_oriTran_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_oriTran_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">原机动车交易协议书</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_oriTran_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_oriTran').append(newCarDom);   //添加新DOM
		                $('#upload_car_oriTran_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'oriTran_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-11.员工身份证
	            if (imgIndexSpe.indexOf('staffIdCard_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_staffIdCard_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_staffIdCard_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">员工身份证</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_staffIdCard_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_staffIdCard').append(newCarDom);   //添加新DOM
		                $('#upload_car_staffIdCard_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'staffIdCard_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 2-12.员工关系证明
	            if (imgIndexSpe.indexOf('staffRel_' + lineIndex) > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadCarObj = $(('#upload_car_staffRel_' + lineIndex + '_') + imgNumIndexSpe).find('img');
		            uploadCarObj.attr('src', picUrl + imgUrl);
		            uploadCarObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadCarObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadCarObj.parent().addClass('uploaded');

			            uploadCarObj.parents('.upload-car').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextCarIndex =  parseInt(imgNumIndexSpe) + 1;

		            	// 生成新的上传
		        		newCarDom =    '<li>' +
			        						'<div class="upload-car" id="upload_car_staffRel_' + lineIndex + '_' + nextCarIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">员工关系证明</p>' +
			                            '</li>';
		                // $('#upload_car_list').append(newCarDom);   //添加新DOM
		                $(('#upload_car_staffRel_' + lineIndex + '_') + imgNumIndexSpe).closest('#upload_car_staffRel').append(newCarDom);   //添加新DOM
		                $('#upload_car_staffRel_' + lineIndex + '_' + nextCarIndex).fileUpload({img: 'staffRel_' + lineIndex + '_' + nextCarIndex});  //给新添加的DOM添加事件
	                }
	            }
	            // 3.银行卡信息 可以连续上传
	            if (imgIndex.indexOf('bank') > -1) {
		            //改写img标签地址为上传照片的URL
		            uploadBankObj = $('#upload_bank' + imgNumIndex).find('img');
		            uploadBankObj.attr('src', picUrl + imgUrlBig);
		            uploadBankObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadBankObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadBankObj.parent().addClass('uploaded');

			            uploadBankObj.parents('.upload-bank').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextBankIndex =  parseInt(imgNumIndex) + 1;

		            	// 生成新的上传
		        		newBankDom =    '<li>' +
			        						'<div class="upload-bank" id="upload_bank' + nextBankIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">银行流水</p>' +
			                            '</li>';
		                $('#upload_bank_list').append(newBankDom);   //添加新DOM
		                $('#upload_bank' + nextBankIndex).fileUpload({img: 'bank' + nextBankIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 4.手续资料信息 可以连续上传
	            if (imgIndex.indexOf('procedure') > -1) {

		            //改写img标签地址为上传照片的URL
		            uploadProcedureObj = $('#upload_procedure' + imgNumIndex).find('img');
		            uploadProcedureObj.attr('src', picUrl + imgUrlBig);
		            uploadProcedureObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadProcedureObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadProcedureObj.parent().addClass('uploaded');

			            uploadProcedureObj.parents('.upload-procedure').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextProcedureIndex =  parseInt(imgNumIndex) + 1;

		            	// 生成新的上传
		        		newProcedureDom =    '<li>' +
			        						'<div class="upload-procedure" id="upload_procedure' + nextProcedureIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">手续资料</p>' +
			                            '</li>';
		                $('#upload_procedure_list').append(newProcedureDom);   //添加新DOM
		                $('#upload_procedure' + nextProcedureIndex).fileUpload({img: 'procedure' + nextProcedureIndex});  //给新添加的DOM添加事件
	                }
	            }

	            // 5.合同信息 可以连续上传
	            if (imgIndex.indexOf('contract') > -1) {

		            //改写img标签地址为上传照片的URL
		            uploadContractObj = $('#upload_contract' + imgNumIndex).find('img');
		            uploadContractObj.attr('src', picUrl + imgUrlBig);
		            uploadContractObj.attr('data-url',picUrl + imgUrlBig);

		            // 判断对已上传的照片进行修改
	                if (!uploadContractObj.parent().hasClass('uploaded')) {
	                	//添加预览图显示类名
			            uploadContractObj.parent().addClass('uploaded');

			            uploadContractObj.parents('.upload-contract').next().append('<a href="javascript:void(0)" class="J-del">删除</a>');

		            	nextContractIndex =  parseInt(imgNumIndex) + 1;

		            	// 生成新的上传
		        		newContractDom =    '<li>' +
			        						'<div class="upload-contract" id="upload_contract' + nextContractIndex + '">' +
				                                '<form enctype="multipart/form-data" target="uploadframe" action="" method="post">' +
				                                '<span class="pic"><img data-url="" src="http://s.xnimg.cn/rrloan/img/a.gif"></span>' +
				                                '<input type="file" value="点击上传图片" name="theFile">' +
				                                '</form>' +
				                            '</div>' +
			                            	' <p class="pic-word">合同照片</p>' +
			                            '</li>';
		                $('#upload_contract_list').append(newContractDom);   //添加新DOM
		                $('#upload_contract' + nextContractIndex).fileUpload({img: 'contract' + nextContractIndex});  //给新添加的DOM添加事件
	                }
	            }

	        } else {
	        	$.dialog.loadFail('网络不给力，请稍后再试！');
	        }
	    }

	    $.widget("dialog.loadFail", $.ui.dialog, {
		    _createWidget: function (msg) {
		        this._super({
		            buttons: [
		                {
		                    "text": "确定",
		                    "class": "ui-button-blue",
		                    "click": function () {
		                        $(this).dialog("instance").destroy();
		                        window.location.reload();
		                    }
		                }
		            ],
		            dialogClass: "ui-dialog-alert",
		            width: 500,
		            minHeight: 0
		        }, "<div><span class=\"ui-dialog-alert-context\">" + msg + "</span></div>");
		    },

		    _create:function(){
		        if($('.ui-dialog-alert').length){
		            $('.ui-dialog-alert').dialog("instance").destroy();
		        }
		        this._super();
		    }

		});

	    $.widget('dialog.successReload', $.ui.dialog, {
	        _createWidget: function(msg) {
	            this._super({
	                beforeClose: function() {
	                    $(this).dialog('instance').destroy();
	                },
	                buttons: [
	                    {
	                        'text': '确定',
	                        'class': 'ui-button-blue',
	                        'click': function (e) {
	                        	var pfDom = document.getElementById('procedureFrame');
	                        	if (pfDom) {
	                        		pfDom.src = pfDom.src;
	                        	} else {
	                        		window.location.reload();
	                        	}
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

 		// 车辆资料提交成功
	    $.widget('dialog.autoSuccess', $.ui.dialog, {
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
	                        	var pfDom = document.getElementById('procedureFrame');
	                        	if (pfDom) {
	                        		pfDom.src = pfDom.src;
	                        	} else {
		                        	$(obj).data('handle','1');
	                        		window.location.reload();
	                        	}
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

 		// 车抵押修改  2015-06-23 xxj
 		function showCon(obj) {
 			oTitle.on('click',obj,function() {
				switch (obj) {
                    case '.edit':
		 				var oCon = $(this).parent().siblings('.info-wrap'),
							loadUrl = oCon.data('url'),
							handle = oCon.data('handle');

							lineIndex = oCon.data('index');

			 				if(oCon.is(":hidden")){
			 					$(this).html('<i class="icon">-</i>收起');
			 					oCon.stop().slideDown();
			 				} else {
			 					$(this).html('<i class="icon">+</i>展开');
			 					oCon.stop().slideUp();
			 				}
			 				if(handle == '0'){
								oCon.load(loadUrl,function() {
									// 上传照片
									carUploadEvent(lineIndex);
									carRegCardUploadEvent(lineIndex);
									carDivCardUploadEvent(lineIndex);
									carKeyUploadEvent(lineIndex);
									carAgreementUploadEvent(lineIndex);
									carAuthorUploadEvent(lineIndex);
									carRegtabUploadEvent(lineIndex);
									carPriceUploadEvent(lineIndex);
									carDashboardUploadEvent(lineIndex);
									carOriIdCardUploadEvent(lineIndex);
									carOriTranUploadEvent(lineIndex);
									carStaffIdCardUploadEvent(lineIndex);
									carStaffRelUploadEvent(lineIndex);
									// 提交信息
									carInfoSubmit(oCon,lineIndex);
								});
			 				}
                        break;
                    case '.view':
						var oCon = $(this).parent().siblings('.info-wrap');

	                    if(oCon.is(":hidden")){
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


 		function showBig(obj) {
	        var oDiv = '',
        		r = 0,
		        str = '<input id="rotateBtn" type="button" value="旋转" style="width: 60px;height: 30px;background: #3385FF;border: 0 none;border-radius:3px;font-size: 14px;color: #fff; position:fixed; top:50%; left:0; z-index: 99999;">';

	        obj.on('click', '.J-view', function() {
	        	$('body').append(str);
	        	oImg = $(this).parent('p').siblings('div').find('img');
	            if(oImg.attr('data-url') != '') {
	                imgUrl = oImg.attr('data-url');
	                oDiv = "<div class='wrap'>\
					        	<div class='ba-wrap'></div>\
					        	<div class='img-wrap'>\
					        		<img src="+imgUrl+">\
					        	</div>\
					        </div>";
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

	        $('body').on('click', '#rotateBtn', function() {
	            r+=90;
	            if(r==360)r=0;
	            $('.img-wrap img').css({
	                '-moz-transform': 'rotate('+ r +'deg)',
	                '-ms-transform': 'rotate('+ r +'deg)',
	                '-webkit-transform': 'rotate('+ r +'deg)',
	                'transform': 'rotate('+ r +'deg)'
	            });
	            return false;
	        });

	        $('body').on('click', function() {
	            if($('.wrap').length > 0){
	                $('.wrap').remove();
	                $('#rotateBtn').remove();
	            }
	        })
	    }


 		// 删除图片 车抵押  2015-06-23 xxj
 		function deletePic() {
			// 删除图片
			infoItem.on('click', '.J-del', function(){
            	$(this).closest('li').remove();
			})
 		}

	});
})( jQuery );

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

