<html>
<head>
    <#include "../taglib.ftl">
</head>
<body>
<div class="row">
    <div class="col-xs-12">
        <div class="box">
            <div class="box-body">
                <!--数据概览-->
                <div class="data-tab clearfix">
                    <#assign classfmt="data-cont pull-left">
                    <div class="${classfmt}">
                        <div class="data-num">${amountCurr!'0'}</div>
                        <div class="data-title">今日总营收</div>
                    </div>
                    <div class="${classfmt}">
                        <div class="data-num">${netpayCurr!'0'}</div>
                        <div class="data-title">现金收入</div>
                    </div>
                    <div class="${classfmt}">
                        <div class="data-num">${coinCurr!'0'}</div>
                        <div class="data-title">积分收入</div>
                    </div>
                    <div class="${classfmt}" style="border-left:4px solid #ffffff">
                        <div class="data-num">${billAmount!'0'}</div>
                        <div class="data-title">账单结余</div>
                    </div>
                </div>
                <!--快捷入口-->
                <div class="func-title">快捷入口</div>
                <div class="func-tab clearfix">
                    <div class="func-cont" onClick="fAjaxTest()">
                        <a href="javascript:void(0)" class="dropdown-toggle">
                            <img class="func-icon" src="${envData.staticRoot!''}/images/custom/icon-hg.png"/>会员接待
                        </a>
                    </div>
                    <div class="func-cont">
                        <a href="#" target="blank"
                           class="dropdown-toggle">
                            <img class="func-icon" src="${envData.staticRoot!''}/images/custom/icon-dd.png"/>会员验码
                        </a>
                    </div>
                </div>
                <!--关键指数-->
                <div class="func-title selectDay">关键指数<span class="func-date active ml-16" day="7">  7天  </span><i>|</i><span
                            class="func-date" day="30">  30天  </span></div>
                <div class="info-tab clearfix">
                    <div class="info-cont">
                        <span class="info-title">访问人数</span>
                        <span class="info-num" id="fwNum">0</span>
                        <span class="info-increase" id="fwNumPercent">0.00%</span>
                    </div>
                    <div class="info-cont">
                        <span class="info-title">新增粉丝</span>
                        <span class="info-num" id="fansNum">0</span>
                        <span class="info-increase" id="fansNumPercent">0.00%</span>
                    </div>
                    <div class="info-cont">
                        <span class="info-title">成交客户</span>
                        <span class="info-num" id="finishNum">0</span>
                        <span class="info-increase" id="finishNumPercent">0.00%</span>
                    </div>
                    <div class="info-cont">
                        <span class="info-title">支付订单</span>
                        <span class="info-num" id="payNum">0</span>
                        <span class="info-increase" id="payNumPercent">0.00%</span>
                    </div>
                    <div class="info-cont">
                        <span class="info-title">支付金额</span>
                        <span class="info-num" id="payAmount">0</span>
                        <span class="info-increase" id="payAmountPercent">0.00%</span>
                    </div>
                    <div class="info-cont">
                        <span class="info-title">上架商品</span>
                        <span class="info-num" id="sellCount">0</span>
                    </div>
                    <div class="info-cont">
                        <span class="info-title">审核商品</span>
                        <span class="info-num" id="waitCount">0</span>
                    </div>
                </div>
            </div><!-- /.box-body -->
        </div><!-- /.box -->
    </div><!-- /.col -->
</div><!-- /.row -->
<script>
    function fAjaxTest() {
        $.ajax({
            url: "/autoCode/ajaxTest",
            type: "POST",
            data: {id:2},
            success: function(retData){
                try{
                    if (retData){
                        msgBox(retData.msg);
                    }else{
                        msgBox("网络错误···");
                    }
                }catch(err){msgBox(err);}
            },
            error: function(){
                msgBox("网络错误···");
            }
        });
    }
</script>
</body>
</html>