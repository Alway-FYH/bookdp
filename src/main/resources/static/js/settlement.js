$(function(){
    //计算总价
    var array = $(".qprice");
    var totalCost = 0;
    for(var i = 0;i < array.length;i++){
        var val = parseInt($(".qprice").eq(i).html().substring(1));
        totalCost += val;
    }
    $("#totalprice").html("￥"+totalCost);
    //settlement2使用
    $("#settlement2_totalCost").val(totalCost);
});


//购物车更改数量++
function addQuantity(obj) {
    console.log(obj)
    var index = $(".car_btn_2").index(obj);
    var quantity = parseInt($(".car_ipt").eq(index).val());
    var stock = parseInt($(".productStock").eq(index).val());
    if (quantity == stock){
        alert("对不起,库存不足!!!");
        return false;
    }
    quantity++;
    //取出price cost id
    var price = parseFloat($(".productPrice").eq(index).val());
    var cost = quantity * price;
    var id = parseInt($(".id").eq(index).val());


    //将最新的数量和总价发送给数据库进行动态修改
    //基于jQuery的AJAX
    $.ajax({
        url:"/cart/update/"+id+"/"+quantity+"/"+cost,
        type:"POST",
        success:function (data) {
           if (data == "success") {
               //计算总价
               $(".qprice").eq(index).text('￥'+cost);
               $(".car_ipt").eq(index).val(quantity);
               var array = $(".qprice");
               var totalCost = 0;
               for(var i = 0;i < array.length;i++){
                   var val = parseInt($(".qprice").eq(i).html().substring(1));
                   totalCost += val;
               }
               $("#totalprice").html("￥"+totalCost);
           }
        }
    });
}

//购物车更改数量--
function subQuantity(obj) {
    console.log(obj)
    var index = $(".car_btn_1").index(obj);
    var quantity = parseInt($(".car_ipt").eq(index).val());
    if (quantity == 1){
        alert("对不起,至少选择一件!!!");
        return false;
    }
    quantity--;
    var price = parseFloat($(".productPrice").eq(index).val());
    var cost = quantity * price;
    var id = parseInt($(".id").eq(index).val());
    //将最新的数量和总价发送给数据库进行动态修改
    //基于jQuery的AJAX
    $.ajax({
        url:"/cart/update/"+id+"/"+quantity+"/"+cost,
        type:"POST",
        success:function (data) {
            if (data == "success") {
                //计算总价
                $(".qprice").eq(index).text('￥'+cost);
                $(".car_ipt").eq(index).val(quantity);
                var array = $(".qprice");
                var totalCost = 0;
                for(var i = 0;i < array.length;i++){
                    var val = parseInt($(".qprice").eq(i).html().substring(1));
                    totalCost += val;
                }
                $("#totalprice").html("￥"+totalCost);
            }
        }
    });

}

//移出购物车
function removeCart(obj){
    var index = $(".delete").index(obj);
    var id = parseInt($(".id").eq(index).val());
    if (confirm("是否确定将其移除购物车")){
        window.location.href = "/cart/deleteById/"+id;
    }
}

function settlement2() {
    var totalCost = $("#totalCost").text();
    if (totalCost == "￥0") {
        alert("购物车为空，无法结算！！！");
    }else {
        window.location.href="/cart/settlement2"
    }

}

