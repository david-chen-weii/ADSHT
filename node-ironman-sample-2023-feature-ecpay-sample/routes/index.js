const express = require('express');
const cors = require('cors'); // 引入 cors
const router = express.Router();
const crypto = require('crypto');
require('dotenv').config();
//後端伺服器
const baseURL = 'http://localhost:8080'

// 引入 ECPay SDK
const ecpay_payment = require('ecpay_aio_nodejs');

const { MERCHANTID, HASHKEY, HASHIV, HOST } = process.env;

// 配置 CORS
const corsOptions = {
  origin: ['http://localhost:5173',
    'http://192.168.23.137', 'https://192.168.23.137:5987', 'https://192.168.23.137',
    'https://localhost/', "http://127.0.0.1:3000/"], // 允许的源
  methods: 'GET,POST', // 允许的 HTTP 方法
  allowedHeaders: 'Content-Type,Authorization,session-id' // 允许的请求头
};

const app = express();
app.use(cors(corsOptions)); // 使用 cors 中间件

// SDK 配置
const options = {
  OperationMode: 'Test', // Test 或 Production
  MercProfile: {
    MerchantID: MERCHANTID,
    HashKey: HASHKEY,
    HashIV: HASHIV,
  },
  IgnorePayment: [],
  IsProjectContractor: false,
};

const generateCheckMacValue = (data) => {
  const sortedKeys = Object.keys(data).sort();

  let queryString = `HashKey=${HASHKEY}`;
  for (const key in sortedData) {
    queryString += `&${key}=${data[key]}`;
  }
  queryString += `&HashIV=${HASHIV}`;

  // URL encode and replace characters
  queryString = encodeURIComponent(queryString).toLowerCase()
    .replace(/%20/g, '+')
    .replace(/%21/g, '!')
    .replace(/%28/g, '(')
    .replace(/%29/g, ')')
    .replace(/%2a/g, '*');

  // Generate MD5 hash

  console.log('Query String:', queryString);
  const hash = crypto.createHash('md5').update(queryString).digest('hex').toUpperCase();
  return hash;
};

router.get('/', (req, res) => {
  const { amount, itemName } = req.query;

  // 解码 itemName
  const decodedItemName = decodeURIComponent(itemName);

  if (!amount) {
    return res.status(400).send('Amount is required');
  }

  const MerchantTradeDate = new Date().toLocaleString('zh-TW', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
    timeZone: 'UTC',
  });

  const TradeNo = 'test' + new Date().getTime();

  let base_param = {
    MerchantTradeNo: TradeNo,
    MerchantTradeDate,
    TotalAmount: amount, // 动态金额
    TradeDesc: '測試交易描述',
    ItemName: decodedItemName,
    ReturnURL: `${baseURL}/return`,
    ClientBackURL: `${baseURL}/clientReturn`,
  };

  const create = new ecpay_payment(options);
  const html = create.payment_client.aio_check_out_all(base_param);
  console.log(html);

  res.send({ html });
});

router.post('/return', async (req, res) => {
  console.log('req.body:', req.body);

  const { CheckMacValue } = req.body;
  const data = { ...req.body };
  delete data.CheckMacValue;

  const checkValue = generateCheckMacValue(data);

  console.log('Generated CheckMacValue:', checkValue);
  console.log('Received CheckMacValue:', CheckMacValue);

  console.log(
    '確認交易正確性：',
    CheckMacValue === checkValue,
    CheckMacValue,
    checkValue,
  );

  res.send('1|OK');
});

router.get('/clientReturn', (req, res) => {
  console.log('clientReturn:', req.query);
  res.render('return', { query: req.query });
});

app.use('/', router);
// 添加這段代碼來啟動伺服器
const PORT = process.env.PORT || 3000;
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Server is running on port ${PORT}`);
});

module.exports = app;
