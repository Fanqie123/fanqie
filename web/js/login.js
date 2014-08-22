/**
 * Created by test on 2014/8/10.
 */
/**
 * 登录注册页面 JS
 */
$('#signup').click(function () {                //登录和注册 页面切换 写的很烂。。。
    $('#c1').hide();
    $('#c2').fadeIn('slow');
});

$('#signin').click(function () {
    $('#c2').hide();
    $('#c1').fadeIn('slow');
});

function show_info(info) {
    $("#info").html(info);
    $('#myModal').modal('show');
}

$(function(){
    var params=window.location.search;
    if(params!=null){
        var array = params.split("=");
        switch (array[1]){
            case '1':
                show_info("账号密码错误");
                break;
        }
    }
});

$("#account1").blur(function () {
    var account = $("#account1").val();
    $.ajax({
        url: 'account_check',
        type: 'post',
        data: 'account=' + account,
        success: function (msg) {
            console.log(msg);
            if (msg == 'exist') {
                $('#alert').fadeIn();
                $('#account1').focus();
                $('#btn_sign_in').attr('disabled', 'disabled');
            } else {
                $('#alert').fadeOut();
                $('#btn_sign_in').removeAttr('disabled');
            }
        }
    });
});

$('#btn_sign_in').click(function () {
    var object = {};
    object.account = $('#account1').val();
    object.id = $('#id').val();
    object.name = $('#name').val();
    object.password = $('#password1').val();
    object.sex = $('input:radio:checked').val();
    if (object.account == '' || object.id == '' || object.name == '' || object.password == '')
        show_info("均不能为空");
    var s = JSON.stringify(object);
    $.ajax({
            url: 'signup',
            type: 'post',
            data: {info: s},
            success: function (msg) {
                switch (msg) {
                    case 'succeed':
                        $('#signin').click();
                        show_info('注册成功，请登录');
                        break;
                    case 'failed' :
                        show_info('注册失败');
                        break;
                    case  'exist':
                        show_info('该账号已被注册');
                        break;
                }

            }
        }
    );
})
;