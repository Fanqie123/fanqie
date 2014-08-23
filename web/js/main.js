/**
 * Created by test on 2014/7/28.
 */
var date = new Date();
var current_date = curentDate();
$('#start_date_search').val(current_date);
$('#end_date_search').val(current_date);
$('#start_date_order').val(current_date);
$('#end_date_order').val(current_date);

$('.sidebar ul li').click(function () {        //侧边栏切换容器
    $('.sidebar ul li').removeClass('active');
    $(this).addClass('active');
    $('.main').hide();
    switch ($(this).val()) {
        case 0:
            $('#container0').fadeIn();
            break;
        case 1:
            $('#container1').fadeIn();
            break;
        case 2:
            $("#btn_my_order").click();
            $('#container2').fadeIn();
            break;
        case 3:
            $('#container3').fadeIn();
            user_info();
    }
});

$('.form_date').datetimepicker({      //日期选择器
    language: 'zh-CN',
    format: 'yyyy-mm-dd',
    weekStart: 1,
    todayBtn: 1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    minView: 2,
    forceParse: 0
});

$('#room_no_order').blur(function () {
    var xml = new XMLHttpRequest();
    var room_no = $('#room_no_order').val();
    xml.open('POST', 'order_pro', true);
    xml.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xml.send('room_no=' + room_no);
    xml.onreadystatechange = function () {
        if (xml.readyState == 4 && xml.status == 200) {
            var text = xml.responseText;
            console.log(text);
            if (text == 'illegal_room_no') {
                $('#alert_order').fadeIn();
                $('#room_no_order').focus();
                $('#room_order').attr('disabled', 'disabled');
            } else {
                $('#alert_order').hide();
                $('#room_order').removeAttr('disabled');
            }
        }
    }
});

$('#btn_order').click(function () {           //查询 预定按钮
    $('#list_order').click();
    $('#room_no_order').val($('#table_body tr.selected').children().first().html());
    $('#start_date_order').val($('#start_date_search').val());
    $('#end_date_order').val($('#end_date_search').val());
});

$('#room_order').click(function () {           //预定按钮
    var xml = new XMLHttpRequest();
    var room_no = $('#room_no_order').val();
    var start_date = $('#start_date_order').val();
    var end_date = $('#end_date_order').val();
    var start = new Date(start_date.replace(/-/g, '/'));
    var end = new Date(end_date.replace(/-/g, '/'));
    var today = new Date(current_date.replace(/-/g, '/'));
    if(room_no==null) {
        show("房间号码不能为空");
        $('#room_no_order').focus();
        return;
    }
    if (start > end || start < today) {
        show('开始日期应小于等于结束日期');
        return;
    }
    xml.open('POST', 'order_pro', true);
    xml.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xml.send('room_no=' + room_no + '&start=' + start_date + '&end=' + end_date);
    xml.onreadystatechange = function () {
        if (xml.readyState == 4 && xml.status == 200)
            var text = xml.responseText;
            switch (text) {
                case 'illegal_room_no':
                    show('错误的房间号码');
                    break;
                case 'ordered_room':
                    show('该房间已被预订');
                    break;
                case 'succeed':
                    show('预订成功');
                    break;
                case 'failed':
                    show('预定失败');
                    break;
            }


        }
    }

);

$("#btn_my_order").click(function () {
    var xml = new XMLHttpRequest();
    xml.open('POST', 'order_pro', true);
    xml.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xml.send();
    xml.onreadystatechange = function () {
        xml.onreadystatechange = function () {
            if (xml.readyState == 4 && xml.status == 200) {
                var text = xml.responseText;
                var json = eval(text);
                if (text == null) {
                    show("error");
                    return;
                }
                $('#table_body1').empty();
                for (var i = 0; i < json.length; i++) {
                    var tr = document.createElement('tr');
                    $(tr).append('<td>' + json[i].order_no + '</td>');
                    $(tr).append('<td>' + json[i].room_no + '</td>');
                    $(tr).append('<td>' + json[i].start_date + '</td>');
                    $(tr).append('<td>' + json[i].end_date + '</td>');
                    $(tr).append('<td>' + json[i].order_date + '</td>');
                    $('#table_body1').append(tr);
                }
                $('#table_body1 tr').click(function () {                                 //table tr选中样式
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                        $('#btn_order_cancel').attr('disabled', 'disabled');
                    } else {
                        $('#table_body1 tr').removeClass('selected');
                        $(this).addClass('selected');
                        $('#btn_order_cancel').removeAttr('disabled');
                    }
                })
            }
        }
    }
});

$('#btn_order_cancel').click(function () {
    var order_no = $('#table_body1 tr.selected').children().first().html();
    $.ajax({
        url: "order_pro",
        type: "post",
        data: "order_no="+order_no,
        success:function(info){
            console.log(info);
        }
    });
});


function show(info) {
    $("#info").html(info);
    $('#myModal').modal('show');
}

function curentDate() {
    var now = new Date();
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
    var clock = year + "-";
    if (month < 10)
        clock += "0";
    clock += month + "-";
    if (day < 10)
        clock += "0";
    clock += day;
    return(clock);
}

$('#modify').click(function () {
    $('#modify').attr('disabled', 'disabled');
    $('#save').removeAttr('disabled');
    $("[id$='i']").removeAttr('disabled')
});

$('#save').click(function () {
    var object = {};
    object.id = $('#id_i').val();
    object.name = $('#name_i').val();
//    object.password = $('#password_i').val();
    object.sex = $('input:radio:checked').val();
    var json = JSON.stringify(object);
    $.ajax({
        url: 'user_update',
        type: 'post',
        data: {user: json},
        success: function (msg) {
            if(msg=='success') {
                $('#modify').removeAttr('disabled', 'disabled');
                $('#save').attr('disabled','disabled');
                $("[id$='i']").attr('disabled','disabled')
                show('保存成功');
            }
        }
    });
});

function user_info() {
    $.ajax({
        url: 'user_info',
        type: 'post',
        dataType: 'json',
        success: function (user) {
            $('#account').val(user.account);
            $('#password_i').val(user.password);
            $('#name_i').val(user.name);
            $('#id_i').val(user.id);
            $('[value=' + user.sex + ']').attr('checked', 'checked');
        }
    })
}

function select() {
    var xml = new XMLHttpRequest();
    var room_type = $('select').val();
    var start_date = $('#start_date_search').val();
    var end_date = $('#end_date_search').val();
    var start = new Date(start_date.replace(/-/g, '/'));
    var end = new Date(end_date.replace(/-/g, '/'));
    var today = new Date(current_date.replace(/-/g, '/'));
    if (start > end || start < today) {
        show('开始日期应小于等于结束日期');
        return;
    }
    console.log(room_type + start_date + end_date);
    xml.open('POST', 'order_pro', true);
    xml.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xml.send('type=' + room_type + '&start=' + start_date + '&end=' + end_date);
    xml.onreadystatechange = function () {
        if (xml.readyState == 4 && xml.status == 200) {
            if (xml.responseText == 'error') {
                show("日期错误");
                return;
            }
            var json = eval('(' + xml.responseText + ')');
            var count = json.length;
            $('#result').html('搜索到' + count + '条记录');
            $('#buttons').empty();
            for (var i = 0; i < Math.ceil(count / 10); i++) {            //按记录数量生成按钮
                $('#buttons').append(' <button type=\'button\' class=\'btn btn-info\' value=' + i + '>' + (i + 1) + '</button>');
            }

            $('.btn-info').click(function () {                          //根据按钮生成对应tables
                var start = $(this).val() * 10;
                $('#table_body').empty();
                for (i = 0; i < 10; i++) {
                    if (json[start + i] == null) break;
                    var tr = document.createElement('tr');
                    $(tr).append('<td>' + json[start + i].room_no + '</td>');
                    $(tr).append('<td>' + json[start + i].room_type + '</td>');
                    $(tr).append('<td>' + json[start + i].room_price + '</td>');
                    $(tr).append('<td>' + json[start + i].room_info + '</td>');
                    $('#table_body').append(tr);
                }

                $('#table_body tr').click(function () {                                 //table tr选中样式
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                        $('#btn_order').attr('disabled', 'disabled');
                    } else {
                        $('#table_body tr').removeClass('selected');
                        $(this).addClass('selected');
                        $('#btn_order').removeAttr('disabled', 'disabled');
                    }
                })

            });

            $('[value=0]').trigger('click');                            //默认显示第一页
        }
    }
}
