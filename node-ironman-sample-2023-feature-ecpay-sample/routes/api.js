// routes/api.js
var express = require('express');
var router = express.Router();
const ECPayPayment = require('ecpay-payment');

// 初始化 ECPay 配置
const ecpay = new ECPayPayment({
    hashKey: 'HwiqPsywG1hLQNuN', // 示例测试用 hashKey
    hashIV: 'YqITWD4TyKacYXpn', // 示例测试用 hashIV
    merchantID: '3085340', // 示例测试用 merchantID
});

// 定義 /api/create-order 路由
router.post('/create-order', async function (req, res, next) {
    const orderData = {
        MerchantTradeNo: 'unique_trade_no_' + new Date().getTime(), // 生成唯一訂單號
        MerchantTradeDate: new Date().toISOString().slice(0, 19).replace('T', ' '), // 綠界需要特定格式的日期
        TotalAmount: 1000, // 訂單總金額
        TradeDesc: 'Test Order',
        ItemName: 'Item 1',
        ReturnURL: 'http://yourdomain.com/ecpay/return'
    };

    try {
        const baseParams = {
            MerchantID: '3003008', // 使用你的測試商店代號
            MerchantTradeNo: orderData.MerchantTradeNo,
            MerchantTradeDate: orderData.MerchantTradeDate,
            PaymentType: 'aio',
            TotalAmount: orderData.TotalAmount,
            TradeDesc: orderData.TradeDesc,
            ItemName: orderData.ItemName,
            ReturnURL: orderData.ReturnURL,
            ChoosePayment: 'ALL'
        };

        const ecpayClient = new ecpay.paymentClient(); // 初始化支付客户端
        const paymentData = await ecpayClient.aioCheckOut(baseParams);
        res.json({ paymentData });
    } catch (error) {
        console.error('Error generating payment data:', error);
        res.status(500).send('Error generating payment data');
    }
});

// 處理回調的路由
router.post('/return', function (req, res, next) {
    const paymentResult = req.body;

    try {
        const isValid = ecpay.paymentClient.verifyPayment(paymentResult);

        if (isValid) {
            res.send('Payment successful');
        } else {
            res.send('Payment failed');
        }
    } catch (error) {
        console.error('Error verifying payment result:', error);
        res.status(500).send('Error verifying payment result');
    }
});

// 定義 /api/call-node 路由
router.get('/call-node', function (req, res, next) {
    console.log('Received request for /api/call-node');
    res.send('Hello from /api/call-node');
});

module.exports = router;
