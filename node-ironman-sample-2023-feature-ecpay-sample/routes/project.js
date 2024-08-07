var express = require('express');
var router = express.Router();
const ECPayPayment = require('ecpay-payment');

// 初始化 ECPay 配置
const ecpay = new ECPayPayment({
    hashKey: 'pwFHCqoQZGmho4w6', // 從 ECPay 獲取
    hashIV: 'EkRm7iFT261dpevs',   // 從 ECPay 獲取
    merchantID: '3002607', // 從 ECPay 獲取
    environment: 'development' // 使用測試環境
  });

// 定義 /api/create-order 路由
router.post('/create-order', function(req, res, next) {
  const orderData = {
    MerchantTradeNo: 'unique_trade_no', // 生成唯一訂單號
    MerchantTradeDate: new Date().toISOString(),
    TotalAmount: 1000, // 訂單總金額
    TradeDesc: 'Test Order',
    ItemName: 'Item 1',
    ReturnURL: 'http://yourdomain.com/ecpay/return'
  };

  try {
    const paymentData = ecpay.generatePaymentData(orderData);
    res.json({ paymentData });
  } catch (error) {
    console.error('Error generating payment data:', error);
    res.status(500).send('Error generating payment data');
  }
});

// 處理回調的路由
router.post('/return', function(req, res, next) {
  const { paymentResult } = req.body;

  try {
    const isValid = ecpay.verifyPaymentResult(paymentResult);

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

module.exports = router;
